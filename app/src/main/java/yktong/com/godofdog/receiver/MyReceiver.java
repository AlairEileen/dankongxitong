package yktong.com.godofdog.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import cn.jpush.android.api.JPushInterface;
import yktong.com.godofdog.activity.MainActivity;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.EventbusValueBean;
import yktong.com.godofdog.manager.WorkManger;
import yktong.com.godofdog.service.MyService;
import yktong.com.godofdog.util.SPUtil;
import yktong.com.godofdog.value.SpValue;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by vampire on 2017/6/13.
 */

public class MyReceiver extends BroadcastReceiver {
    private final static String TAG = "vampire_MyReceiver";
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "onReceive - " + intent.getAction());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            EventBus.getDefault().post(new EventbusValueBean("[MyReceiver] 接收Registration Id : " + regId));
        }else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            Log.d(TAG, "收到了自定义消息。消息内容是：" + content);
            EventBus.getDefault().post(new EventbusValueBean("收到了自定义消息。消息内容是：" + content));
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
            if (content.contains("101")){
                Intent intent1 = new Intent(context, MyService.class);
                context.stopService(intent1);
                ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
                Log.d(TAG, context.getPackageName());
                am.restartPackage(context.getPackageName());
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "收到了通知");
            // 在这里可以做些统计，或者做些其他工作
            EventBus.getDefault().post(new EventbusValueBean("收到了通知"));
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为
            Intent i = new Intent(context, MainActivity.class);  //自定义打开的界面
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.d("MyReceiver", "get");
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }else if (intent.getAction().equals("new_user")){
            Log.d("MyReceiver", intent.getAction());
            String username = intent.getStringExtra(SpValue.USER_ID);
            SPUtil.putAndApply(MyApp.getmContext(),SpValue.USER_ID,username);
            WorkManger.getInstence().doTask(SpValue.STATE_GET_NICK);
            Toast.makeText(context, intent.getAction(), Toast.LENGTH_SHORT).show();
        }else if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            // TODO: 2017/9/1  自启动
            Intent service =  new Intent(context,MyService.class);
            context.startService(service);
        }
    }
}
