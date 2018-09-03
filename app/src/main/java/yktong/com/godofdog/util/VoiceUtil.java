package yktong.com.godofdog.util;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.GrammarListener;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.VoiceWakeuper;
import com.iflytek.cloud.WakeuperListener;
import com.iflytek.cloud.WakeuperResult;
import com.iflytek.cloud.util.ResourceUtil;
import com.iflytek.cloud.util.ResourceUtil.RESOURCE_TYPE;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

import yktong.com.godofdog.R;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.MyOrderBean;
import yktong.com.godofdog.manager.WorkManger;
import yktong.com.godofdog.util.util.JsonParser;
import yktong.com.godofdog.value.SpValue;

/**
 * Created by vampire on 2017/7/11.
 */

public class VoiceUtil {
    private static VoiceUtil ourInstance;

    private String TAG = "ivw";
    private Toast mToast;
    private VoiceWakeuper mIvw;
    // 唤醒结果内容
    private String resultString;

    // 设置门限值 ： 门限值越低越容易被唤醒
    private TextView tvThresh;
    private SeekBar seekbarThresh;
    private final static int MAX = 60;
    private final static int MIN = -20;
    private int curThresh = 10;
    private String threshStr = "门限值：";
    private String keep_alive = "1";
    private String ivwNetMode = "2";

    public static VoiceUtil getInstance() {
        if (ourInstance == null) {
            synchronized (VoiceUtil.class) {
                if (ourInstance == null) {
                    ourInstance = new VoiceUtil();
                }
            }
        }
        return ourInstance;

    }

    private VoiceUtil() {
    }

    public void wakeup(Context mContext) {
        SpeechUtility utility = SpeechUtility.createUtility(mContext, SpeechConstant.APPID + "=57e3982d");
        // 初始化唤醒对象

        //非空判断，防止因空指针使程序崩溃
        mIvw = VoiceWakeuper.getWakeuper();
        Log.d("VoiceUtil", "mIvw==null:" + (mIvw == null));
        if (mIvw != null) {
            setRadioEnable(false);
            resultString = "";
            Log.d("VoiceUtil", resultString);
            // 清空参数
            mIvw.setParameter(SpeechConstant.PARAMS, null);
            // 唤醒门限值，根据资源携带的唤醒词个数按照“id:门限;id:门限”的格式传入
            mIvw.setParameter(SpeechConstant.IVW_THRESHOLD, "0:" + curThresh);
            // 设置唤醒模式
            mIvw.setParameter(SpeechConstant.IVW_SST, "wakeup");
            // 设置持续进行唤醒
            mIvw.setParameter(SpeechConstant.KEEP_ALIVE, keep_alive);
            // 设置闭环优化网络模式
            mIvw.setParameter(SpeechConstant.IVW_NET_MODE, ivwNetMode);
            // 设置唤醒资源路径
            mIvw.setParameter(SpeechConstant.IVW_RES_PATH, getResource());
            // 设置唤醒录音保存路径，保存最近一分钟的音频
            mIvw.setParameter(SpeechConstant.IVW_AUDIO_PATH, Environment.getExternalStorageDirectory().getPath() + "/msc/ivw.wav");
            mIvw.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
            // 如有需要，设置 NOTIFY_RECORD_DATA 以实时通过 onEvent 返回录音音频流字节
            //mIvw.setParameter( SpeechConstant.NOTIFY_RECORD_DATA, "1" );

            // 启动唤醒
            mIvw.startListening(mWakeuperListener);
        } else {
            showTip("唤醒未初始化");
        }
    }

    private WakeuperListener mWakeuperListener = new WakeuperListener() {

        @Override
        public void onResult(WakeuperResult result) {
            Log.d(TAG, "onResult");
            if (!"1".equalsIgnoreCase(keep_alive)) {
                setRadioEnable(true);
            }
            try {
                String text = result.getResultString();

                JSONObject object;
                object = new JSONObject(text);
                StringBuffer buffer = new StringBuffer();
                buffer.append("【RAW】 " + text);
                buffer.append("\n");
                buffer.append("【操作类型】" + object.optString("sst"));
                buffer.append("\n");
                buffer.append("【唤醒词id】" + object.optString("id"));
                if (object.optString("id").equals("1")) {
                    WorkManger.getInstence().doTask(SpValue.STATE_INTERACT);
                } else if (object.optString("id").equals("0")) {
                    WorkManger.getInstence().doTask(SpValue.STATE_QUERY_FRIENDS_NUM);
                } else if (object.optString("id").equals("3")) {
                    WorkManger.getInstence().doTask(SpValue.STATE_PRAISE);
                }
                buffer.append("\n");
                buffer.append("【得分】" + object.optString("score"));
                buffer.append("\n");
                buffer.append("【前端点】" + object.optString("bos"));
                buffer.append("\n");
                buffer.append("【尾端点】" + object.optString("eos"));
                resultString = buffer.toString();
            } catch (JSONException e) {
                resultString = "结果解析出错";
                e.printStackTrace();
                Log.d("VoiceUtil", "e:" + e);
            }
            Log.d("VoiceUtil", resultString);
        }

        @Override
        public void onError(SpeechError error) {
            showTip(error.getPlainDescription(true));
            setRadioEnable(true);
        }

        @Override
        public void onBeginOfSpeech() {
        }

        @Override
        public void onEvent(int eventType, int isLast, int arg2, Bundle obj) {
            switch (eventType) {
                // EVENT_RECORD_DATA 事件仅在 NOTIFY_RECORD_DATA 参数值为 真 时返回
                case SpeechEvent.EVENT_RECORD_DATA:
                    final byte[] audio = obj.getByteArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                    Log.i(TAG, "ivw audio length: " + audio.length);
                    break;
            }
        }

        @Override
        public void onVolumeChanged(int volume) {

        }
    };

    private String getResource() {

        final String resPath = ResourceUtil.generateResourcePath(MyApp.getmContext(), RESOURCE_TYPE.assets, "ivw/" + MyApp.getmContext().getString(R.string.app_id) + ".jet");
        Log.d(TAG, "resPath: " + resPath);
        return resPath;
    }

    private void showTip(final String str) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mToast.setText(str);
//                mToast.show();
//            }
//        });
    }

    private void setRadioEnable(final boolean enabled) {
//        MyApp.getmContext().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                findViewById(R.id.ivw_net_mode).setEnabled(enabled);
//                findViewById(R.id.btn_start).setEnabled(enabled);
//                findViewById(R.id.seekBar_thresh).setEnabled(enabled);
//            }
//        });
    }
}