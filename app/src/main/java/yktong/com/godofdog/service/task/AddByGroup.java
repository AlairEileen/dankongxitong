package yktong.com.godofdog.service.task;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.manager.WorkManger;
import yktong.com.godofdog.service.WeService;
import yktong.com.godofdog.tool.OverTask;
import yktong.com.godofdog.util.PerformClickUtils;
import yktong.com.godofdog.util.SPUtil;
import yktong.com.godofdog.util.ShellUtils;
import yktong.com.godofdog.util.SupportUtil;
import yktong.com.godofdog.value.SpValue;

/**
 * Created by vampire on 2017/6/27.
 */

public class AddByGroup implements SpValue {
    private static AddByGroup instence;
    private String groupName;
    private boolean isSendRequest;

    private AddByGroup() {
    }

    public static AddByGroup getInstence() {
        if (instence == null) {
            synchronized (AddByGroup.class) {
                if (instence == null) {
                    instence = new AddByGroup();
                }
            }
        }
        return instence;
    }

    public void addGroupFriends(WeService mService, SupportUtil supportUtil, String atyName, AccessibilityEvent event) {
        AccessibilityNodeInfo rootInActiveWindow = mService.getRootInActiveWindow();
        if (rootInActiveWindow == null) return;
        if (atyName.equals(supportUtil.getGroupListUi())) {
            Log.d("AddByGroup", "群组列表");
            List<AccessibilityNodeInfo> nameList = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/a2u");
            if (null != nameList && !nameList.isEmpty()) {
                for (int i = 0; i < nameList.size(); i++) {
                    groupName = (String) SPUtil.get(MyApp.getmContext(), GROUP_NAME, "ghhhhhhhhhh他特特特特LOL看看LO…");
                    if (nameList.get(i).getText().toString().equals(groupName)) {
                        SPUtil.putAndApply(MyApp.getmContext(), "GROUP_INDEX", i);
                    }
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            float itemH = (float) (mService.getHeight() * 104.0);
            float heightBegin = (float) (246.0 * mService.getHeight());
            float weigh = (float) (360.0 * mService.getWight());
            int index = (int) SPUtil.get(MyApp.getmContext(), "GROUP_INDEX", 1);
            float heigh = heightBegin + (itemH * index);
            String[] cleanEtCmd = new String[]{"input tap " + weigh + " " + heigh};
            ShellUtils.execCommand(cleanEtCmd, true);
            // 选择用户所求群组名
        } else if (atyName.equals("com.tencent.mm.ui.chatting.En_5b8fbb1e")) {
            Log.d("AddByGroup", "聊天页面");
            float wight = mService.getWight();
            float height = mService.getHeight();
            float screenW = (float) (674.0 * wight);
            float screenH = (float) (98.0 * height);
            String[] cleanEtCmd = new String[]{"input tap " + screenW + " " + screenH};
            ShellUtils.execCommand(cleanEtCmd, true);
        } else if (atyName.equals("com.tencent.mm.plugin.chatroom.ui.ChatroomInfoUI")) {
            takeGroupINfo(mService, rootInActiveWindow);
        } else if (atyName.equals("com.tencent.mm.plugin.profile.ui.ContactInfoUI")) {
            String nick = "";

            Log.d("AddByGroup", atyName);
            String sendMessage = PerformClickUtils.getText(mService, "com.tencent.mm:id/aek");
            if (null != sendMessage && sendMessage.equals("发消息")) {
                PerformClickUtils.performBack(mService);
            } else {
                if (isSendRequest) {
                    PerformClickUtils.performBack(mService);
                } else {
                    PerformClickUtils.findViewIdAndClick(mService, "com.tencent.mm:id/aej");
                }
            }
            String text = PerformClickUtils.getText(mService, "com.tencent.mm:id/aeq");
            if (text!=null){
            nick += text + "\n";
            }
            String temp = PerformClickUtils.getText(mService, "com.tencent.mm:id/mh");
            if (temp!=null){
            nick += ("昵称:" +temp);
            }
            Log.d("AddByGroup_s", nick);
        } else if (atyName.equals(supportUtil.getSayHiWithSnsPermissionUI())) {
            int num = (int) SPUtil.get(MyApp.getmContext(), ADD_NUM, 0);
            int i = num - 1;
            if (i == -1) {
                PerformClickUtils.performBack(mService);
                OverTask.getInstance().finishTask();
                WorkManger.getInstence().doTask(SpValue.STATE_NORMAL);
            } else {
                SPUtil.putAndApply(MyApp.getmContext(), ADD_NUM, i);

            }
            PerformClickUtils.findViewIdAndClick(mService, supportUtil.getSendRequestBtnId());
            isSendRequest = true;
        } else if (atyName.equals(supportUtil.getDialog())) {
            String hint = PerformClickUtils.getText(mService, "com.tencent.mm:id/bu5");
            if (null != hint && hint.contains("由于对方的隐私设置，你无法通过群聊将其添加至通讯录。")) {
                PerformClickUtils.performBack(mService);
                isSendRequest = true;
            }
        }
    }

    private void takeGroupINfo(WeService mService, AccessibilityNodeInfo rootInActiveWindow) {
        List<AccessibilityNodeInfo> itemList = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ce7");
        if (null != itemList && !itemList.isEmpty()) {
            CharSequence des = itemList.get(itemList.size() - 1).getContentDescription();
            if (des != null && des.toString().equals("删除成员")) {
                // TODO: 2017/6/28  获取的是全部好友
            }
            // 获取 群组人数
            int friendNum = itemList.size();
            SPUtil.putAndApply(MyApp.getmContext(), groupName + "Size", friendNum);
            int index = (int) SPUtil.get(MyApp.getmContext(), groupName + "index", 0);
            if (index < friendNum) {
                int line = index / 5;
                Log.d("AddByGroup", "line:" + line);
                int postion = index % 5;
                Log.d("AddByGroup", "postion:" + postion);
                float weigh = (float) ((88.0 + 136 * postion) * mService.getWight());
                float high = (254 + line * 180) * mService.getHeight();
                Log.d("AddByGroup", "weigh:" + weigh);
                Log.d("AddByGroup", "high:" + high);
                String[] cleanEtCmd = new String[]{"input tap " + weigh + " " + high};
                int temp = index + 1;
                SPUtil.putAndApply(MyApp.getmContext(), groupName + "index", temp);
                isSendRequest = false;
                ShellUtils.execCommand(cleanEtCmd, true);
            }

        }
    }
}
