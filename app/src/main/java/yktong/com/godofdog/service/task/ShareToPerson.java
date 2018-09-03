package yktong.com.godofdog.service.task;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;

import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.manager.WorkManger;
import yktong.com.godofdog.tool.OverTask;
import yktong.com.godofdog.util.PerformClickUtils;
import yktong.com.godofdog.util.SPUtil;
import yktong.com.godofdog.util.ShellUtils;
import yktong.com.godofdog.util.SupportUtil;
import yktong.com.godofdog.value.SpValue;

/**
 * Created by vampire on 2017/7/31.
 */

public class ShareToPerson {
    private static ShareToPerson ourInstance;

    public static ShareToPerson getInstance() {
        if (ourInstance == null) {
            synchronized (ShareToPerson.class) {
                if (ourInstance == null) {
                    ourInstance = new ShareToPerson();
                }
            }
        }
        return ourInstance;

    }

    private ShareToPerson() {

    }

    public void  share(AccessibilityService mService , SupportUtil supportUtil , String atyName){
        if (atyName.equals(supportUtil.getSendPersonUi())){
            String name = (String) SPUtil.get(MyApp.getmContext(), SpValue.SHARE_NAME, "");
            PerformClickUtils.setText(mService,"com.tencent.mm:id/ahu",name);
            sleep(500);
            ShellUtils.execCommand("input tap 360 360", true);

        }else if (atyName.equals("com.tencent.mm.ui.base.h")){
            sleep(300);
            String content = (String) SPUtil.get(MyApp.getmContext(),SpValue.SNS_CONTENT,"");
            PerformClickUtils.setText(mService,"com.tencent.mm:id/adg",content);
            sleep(500);
            PerformClickUtils.findViewIdAndClick(mService,"com.tencent.mm:id/ad8");
            sleep(500);
            PerformClickUtils.findViewIdAndClick(mService,"com.tencent.mm:id/ad8");
            WorkManger.getInstence().doTask(SpValue.STATE_NORMAL);
            OverTask.getInstance().finishTask();
        }
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}