package yktong.com.godofdog.service.task;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.manager.WorkManger;
import yktong.com.godofdog.tool.OverTask;
import yktong.com.godofdog.util.PerformClickUtils;
import yktong.com.godofdog.util.SPUtil;
import yktong.com.godofdog.util.ShellUtils;
import yktong.com.godofdog.util.SupportUtil;
import yktong.com.godofdog.value.SpValue;

/**
 * Created by vampire on 2017/6/16.
 */

public class PraiseInSns {
    private static PraiseInSns ourInstance;
    private SupportUtil mSupportUtil;
    private AccessibilityService mService;

    public static PraiseInSns getInstance(SupportUtil supportUtil, AccessibilityService mService) {
        if (ourInstance == null) {
            synchronized (PraiseInSns.class) {
                if (ourInstance == null) {
                    ourInstance = new PraiseInSns(supportUtil, mService);
                }
            }
        }
        return ourInstance;
    }

    private PraiseInSns(SupportUtil supportUtil, AccessibilityService mService) {
        this.mService = mService;
        this.mSupportUtil = supportUtil;
    }

    public void praiseInSns(String className) {
        if (className.equals(mSupportUtil.getSnsUi())) {
            Log.d("PraiseInSns", "朋友圈页面");
            ShellUtils.execCommand("input swipe 360 825 331 1240",true);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            AccessibilityNodeInfo rootInActiveWindow = mService.getRootInActiveWindow();
            if (rootInActiveWindow == null) {
                return;
            }
            praise(rootInActiveWindow);
            AccessibilityNodeInfo node = PerformClickUtils.getNode(mService, mSupportUtil.getSnsListViewId());
            int praiseNum = (int) SPUtil.get(MyApp.getmContext(), SpValue.INTERACT_NUM,0);
            Log.d("PraiseInSns", "praiseNum:" + praiseNum);
            while (praiseNum>0 && node!=null){
                sleep(200);
                Log.d("PraiseInSns", "滚动");
                node.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);// 向下滚动
                praise(rootInActiveWindow);
            }
        }

    }

    private void praise(AccessibilityNodeInfo rootInActiveWindow) {
        List<AccessibilityNodeInfo> commentBtnList = rootInActiveWindow.findAccessibilityNodeInfosByViewId(mSupportUtil.getSnsCommentBtnId());
        if (null != commentBtnList && !commentBtnList.isEmpty()) {
            for (AccessibilityNodeInfo commentBtn : commentBtnList) {
                commentBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);//点击评论  弹出选项
                sleep(500);
                int praiseNum = (int) SPUtil.get(MyApp.getmContext(), SpValue.INTERACT_NUM,0);
                String text = PerformClickUtils.getText(mService, "com.tencent.mm:id/co2");
                if (null!=text && text.equals("赞")) {
                    if (praiseNum>0){
                        int temp = praiseNum -1;
                        Log.d("PraiseInSns", "temp:" + temp);
                        SPUtil.putAndApply(MyApp.getmContext(),SpValue.INTERACT_NUM,temp);
                        PerformClickUtils.findViewIdAndClick(mService, mSupportUtil.getSnsPraiseBtnId());
                    }else {
                        PerformClickUtils.performBack(mService);
                        OverTask.getInstance().finishTask();
                        WorkManger.getInstence().doTask(SpValue.STATE_NORMAL);
                    }
                }

            }
        }
    }

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
