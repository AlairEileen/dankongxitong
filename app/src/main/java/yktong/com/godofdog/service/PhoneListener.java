package yktong.com.godofdog.service;

import android.content.ContentResolver;
import android.media.MediaRecorder;
import android.os.Environment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.tool.CallUtil;
import yktong.com.godofdog.tool.thread.ThreadPool;

/**
 * Created by vampire on 2017/9/1.
 */

public class PhoneListener extends PhoneStateListener {
    private String incomeNumber;   //来电号码
    private MediaRecorder mediaRecorder;
    private File file;


    @Override
    public void onCallStateChanged(int state, String incomingNumber){
        try
        {
            switch(state)
            {
                case TelephonyManager.CALL_STATE_RINGING:   //来电
                    this.incomeNumber = incomingNumber;
                    Log.d("PhoneListener", "来电");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:   //接通电话
                    file = new File(Environment.getExternalStorageDirectory(), this.incomeNumber + System.currentTimeMillis() + ".amr");
                    Log.d("PhoneListener", file.getPath());
                    Log.d("PhoneListener", file.getName());
                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);   //获得声音数据源
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);   // 按amr格式输出
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mediaRecorder.setOutputFile(file.getAbsolutePath());   //输出文件
                    mediaRecorder.prepare();    //准备
                    mediaRecorder.start();
                    break;
                case TelephonyManager.CALL_STATE_IDLE:  //挂掉电话
                    Log.d("PhoneListener", "挂断");
                    //获取通话记录
                    ContentResolver cr;
                    cr = MyApp.getmContext().getContentResolver();
                    ThreadPool.thredP.execute(() -> {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (file!=null){
                            String callHistoryListStr = CallUtil.StringgetCallHistoryList(MyApp.getmContext(), cr,file.getPath());
                            Log.d("PhoneListener", callHistoryListStr);
                        }else {
                            String s = CallUtil.StringgetCallHistoryList(MyApp.getmContext(), cr, null);
                            Log.d("PhoneListener", s);
                        }
                    });
                    if(mediaRecorder != null) {
                        mediaRecorder.stop();
                        mediaRecorder.release();
                        mediaRecorder = null;
                    }
                    break;
            }
        }
        catch (IllegalStateException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}


