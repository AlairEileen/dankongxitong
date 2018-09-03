package yktong.com.godofdog.service.task;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.EventbusValueBean;
import yktong.com.godofdog.manager.WorkManger;
import yktong.com.godofdog.util.PerformClickUtils;
import yktong.com.godofdog.util.SPUtil;
import yktong.com.godofdog.util.ShellUtils;
import yktong.com.godofdog.util.SupportUtil;
import yktong.com.godofdog.value.SpValue;

/**
 * Created by vampire on 2017/7/28.
 */

public class ShareMiniProject {
    private static ShareMiniProject ourInstance;

    public static ShareMiniProject getInstance() {
        if (ourInstance == null) {
            synchronized (ShareMiniProject.class) {
                if (ourInstance == null) {
                    ourInstance = new ShareMiniProject();
                }
            }
        }
        return ourInstance;

    }

    private ShareMiniProject() {
    }

    public void sendMiniProject(AccessibilityService mService, SupportUtil supportUtil, String atyName, AccessibilityEvent event) {
        List<CharSequence> text = event.getText();
        for (CharSequence charSequence : text) {
            if (charSequence.toString().equals("已转发")) {
                sleep(500);
                PerformClickUtils.findViewIdAndClick(mService, "com.tencent.mm:id/j4");
            }
        }
        if (atyName.equals("com.tencent.mm.plugin.appbrand.ui.AppBrandLauncherUI")) {
            PerformClickUtils.findTextAndClick(mService, "搜索");
        } else if (atyName.equals("com.tencent.mm.plugin.appbrand.ui.AppBrandSearchUI")) {
            String projectName = (String) SPUtil.get(MyApp.getmContext(), SpValue.MINI_PROJCET_NAME, "星巴克用星说");
            PerformClickUtils.setText(mService, "com.tencent.mm:id/gz", projectName);
            sleep(2000);
            Log.d("ShareMiniProject", "搜索");
            ShellUtils.execCommand("input tap 700 1240", true);
            sleep(4000);
            Log.d("ShareMiniProject", "点击");
            ShellUtils.execCommand("input tap 360 360", true);
            sleep(15000);
            PerformClickUtils.findViewIdAndClick(mService, "com.tencent.mm:id/j4");

        } else if (atyName.equals("android.support.design.widget.c")) {
            sleep(1000);
            PerformClickUtils.findViewIdAndClick(mService, "com.tencent.mm:id/mj");
        } else if (atyName.equals("com.tencent.mm.ui.transmit.SelectConversationUI")) {
            sleep(500);
            String shareName = (String) SPUtil.get(MyApp.getmContext(), SpValue.SHARE_NAME, "");
            PerformClickUtils.setText(mService, "com.tencent.mm:id/ahu", shareName);
            sleep(1000);
            PerformClickUtils.findViewIdAndClick(mService, "com.tencent.mm:id/by5");
        } else if (atyName.equals("com.tencent.mm.ui.base.h")) {
            PerformClickUtils.findViewIdAndClick(mService, "com.tencent.mm:id/ad8");
            EventBus.getDefault().post(new EventbusValueBean("下一个"));
            PerformClickUtils.findTextAndClick(mService, "确定");
            PerformClickUtils.findTextAndClick(mService, "接受");
            PerformClickUtils.findViewIdAndClick(mService,"com.tencent.mm:id/nr");
        } else if (atyName.equals("com.tencent.mm.ui.base.i")) {
            // 微信授权操作
            PerformClickUtils.findViewIdAndClick(mService, "com.tencent.mm:id/nr");
        } else if (atyName.equals(supportUtil.getLauncherUI())) {
            //未开启小程序
            sleep(300);
            PerformClickUtils.findTextAndClick(mService, "搜索");
        } else if (atyName.equals(supportUtil.getSearchUI())) {
            PerformClickUtils.setText(mService, "com.tencent.mm:id/gz", "小程序");
            sleep(2000);
            Log.d("ShareMiniProject", "搜索");
            ShellUtils.execCommand("input tap 700 1240", true);
            sleep(1000);
            ShellUtils.execCommand("input tap 360 300", true);
            sleep(6000);
            ShellUtils.execCommand("input tap 450 360", true);
            sleep(5000);
            WorkManger.getInstence().doTask(SpValue.STATE_SHARE_MINI_PROJECT);
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