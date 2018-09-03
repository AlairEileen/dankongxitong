package yktong.com.godofdog.service.task;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.util.PerformClickUtils;
import yktong.com.godofdog.util.SPUtil;
import yktong.com.godofdog.util.ShellUtils;
import yktong.com.godofdog.util.SupportUtil;
import yktong.com.godofdog.value.SpValue;

/**
 * Created by vampire on 2017/6/13.
 */

public class AddLinkman implements SpValue {
    private static AddLinkman instence;
    private float screenW;
    private float screenH;

    public static AddLinkman getInstence() {
        if (instence == null) {
            synchronized (AddLinkman.class) {
                if (instence == null) {
                    instence = new AddLinkman();
                }
            }
        }
        return instence;
    }

    public void addFriendByLink(SupportUtil supportUtil, String className, AccessibilityService mService) {
        if (className.equals(supportUtil.getFMesssageConversationUI())) {
            //联系人添加
            AccessibilityNodeInfo rootInActiveWindow = mService.getRootInActiveWindow();
            Log.d("AddLinkman", "rootInActiveWindow==null:" + (rootInActiveWindow == null));
            if (rootInActiveWindow == null) {
                return;
            }
            List<AccessibilityNodeInfo> addBtnList = rootInActiveWindow.findAccessibilityNodeInfosByViewId(supportUtil.getFMesssageConversationUI_ADD_BTN_ID());
            Log.d("AddLinkman", "addBtnList.isEmpty():" + addBtnList.isEmpty());
            if (!addBtnList.isEmpty()) {
                for (AccessibilityNodeInfo nodeInfo : addBtnList) {
                    if (nodeInfo != null && nodeInfo.getText().equals("添加")) {
                        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        sleep(1000);
                    }
                }
            }
            PerformClickUtils.findTextAndClick(mService, "接受");
            sleep(1000);
            AccessibilityNodeInfo lv = PerformClickUtils.getNode(mService, supportUtil.getFMesssageConversationUI_LV_ID());
            if (null != lv) {
                sleep(1000);
                lv.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            }

        } else if (className.equals(supportUtil.getContactInfoUI())) {
            String text = PerformClickUtils.getText(mService, supportUtil.getContactInfoUIaddBtnId());
            if (null != text && text.equals("添加到通讯录")) {
                // 没有Root权限 无法设置招呼语
                String wight = (String) SPUtil.get(MyApp.getmContext(), SCREEN_WIDE, "1.0");
                Log.d("AddLinkman", "wight:" + wight);
                String height = (String) SPUtil.get(MyApp.getmContext(), SCREEN_HEIGHT, "1.0");
                Log.d("AddLinkman", "height:" + height);
                if (wight == null || height == null) {
                    screenW = (float) (670.0 * 1);
                    screenH = (float) (369.0 * 1);
                } else {
                    float w = Float.parseFloat(wight);
                    float h = Float.parseFloat(height);
                    screenW = (float) (670.0 * w);
                    screenH = (float) (369.0 * h);
                }

                String[] cleanEtCmd = new String[]{"input tap " + screenW + " " + screenH};
                ShellUtils.execCommand(cleanEtCmd, true);
                String content = (String) SPUtil.get(MyApp.getmContext(), CONTENT, "你好，我们好像在哪见过。");
                PerformClickUtils.setText(mService, supportUtil.getSayHiContantEtId(), content);
                // TODO: 2017/6/14 设置昵称
                PerformClickUtils.findViewIdAndClick(mService, supportUtil.getSayHiNicknameEtId());
            } else {
                String content = PerformClickUtils.getText(mService, "com.tencent.mm:id/aek");
                if (null != content && content.equals("发消息")) {
                    PerformClickUtils.performBack(mService);
                }
            }


        }

    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
