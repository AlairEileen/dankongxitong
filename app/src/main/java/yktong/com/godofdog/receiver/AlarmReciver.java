package yktong.com.godofdog.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.util.SPUtil;
import yktong.com.godofdog.value.SpValue;

/**
 * Created by vampire on 2017/7/28.
 */

public class AlarmReciver extends BroadcastReceiver implements SpValue {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReciver", "闹钟");
        SPUtil.putAndApply(MyApp.getmContext(),SNS_NUM,0);
        SPUtil.putAndApply(MyApp.getmContext(),MARKETING_NUM,0);
    }
}
