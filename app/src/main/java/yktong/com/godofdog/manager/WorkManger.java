package yktong.com.godofdog.manager;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.io.DataOutputStream;
import java.util.Timer;
import java.util.TimerTask;

import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.EventbusValueBean;
import yktong.com.godofdog.service.task.InteractFans;
import yktong.com.godofdog.service.task.QueryFriendNum;
import yktong.com.godofdog.service.task.SnsDaily;
import yktong.com.godofdog.tool.OverTask;
import yktong.com.godofdog.tool.datebase.DBTool;
import yktong.com.godofdog.util.SPUtil;
import yktong.com.godofdog.util.ShellUtils;
import yktong.com.godofdog.value.SpValue;

/**
 * Created by vampire on 2017/6/13.
 */

public class WorkManger implements SpValue {
    private static WorkManger instence;

    private WorkManger() {
    }

    public static WorkManger getInstence() {
        if (instence == null) {
            synchronized (WorkManger.class) {
                if (instence == null) {
                    instence = new WorkManger();
                }
            }
        }
        return instence;
    }

    public void doTask(int task) {
        boolean isRoot = getRootAhth();
        int state = (int) SPUtil.get(MyApp.getmContext(), WORK_STATE, STATE_NORMAL);
//        if (state != STATE_NORMAL && task != STATE_NORMAL && state != task) {
//            // 如果当前状态不是默认状态
//            SPUtil.putAndApply(MyApp.getmContext(), WORK_STATE, task);
//        } else if (state == STATE_NORMAL) {
        switch (task) {
            case STATE_NORMAL:
                // TODO: 2017/7/27
                break;
            case STATE_ADD_LINK:
//                if (isRoot) {
                SPUtil.putAndApply(MyApp.getmContext(),WORK_STATE,STATE_ADD_LINK);
                String[] cmd = new String[]{"am start -n com.tencent.mm/com.tencent.mm.plugin.subapp.ui.friend.FMessageConversationUI"};
                ShellUtils.execCommand(cmd, true);
                new Thread(()->{
                    try {
                        Thread.sleep(12000);
                        OverTask.getInstance().finishTask();
                        WorkManger.getInstence().doTask(SpValue.STATE_NORMAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
                break;
            case STATE_PRAISE:
                //点赞
                String[] praise = new String[]{"am start -n com.tencent.mm/com.tencent.mm.plugin.sns.ui.En_424b8e16"};
//                Looper.prepare();
//                Toast.makeText(MyApp.getmContext(), "点赞", Toast.LENGTH_SHORT).show();
//                Looper.loop();
                ShellUtils.execCommand(praise, true);
                break;
            case STATE_QUERY_FRIENDS_NUM:
                // 统计好友数
                EventBus.getDefault().post(new EventbusValueBean("统计好友"));
                QueryFriendNum.refreshList();
                String[] query = new String[]{"am start -n com.tencent.mm/com.tencent.mm.ui.LauncherUI"};
                ShellUtils.execCommand(query, true);
                break;
            case STATE_CHECK_GROUP:
                EventBus.getDefault().post(new EventbusValueBean("统计群组"));
                DBTool.getInstance().cleanGroup();
                // 统计用户群组
                String[] checkGroup = new String[]{"am start -n com.tencent.mm/com.tencent.mm.ui.contact.ChatroomContactUI"};
                ShellUtils.execCommand(checkGroup, true);
                break;
            case STATE_ADD_GROUP_FRIEND:
                // 社群拓客
                checkGroup = new String[]{"am start -n com.tencent.mm/com.tencent.mm.ui.contact.ChatroomContactUI"};
                ShellUtils.execCommand(checkGroup, true);
                break;
            case STATE_INTERACT:
                // 聊天数统计
                EventBus.getDefault().post(new EventbusValueBean("聊天数统计"));
                String[] interact = new String[]{"am start -n com.tencent.mm/com.tencent.mm.ui.LauncherUI"};
                InteractFans.refreshList();//初始化列表
                SPUtil.putAndApply(MyApp.getmContext(), IS_SCROLL, true);
                ShellUtils.execCommand(interact, true);
                break;
            case STATE_COMMENT:
                // 点赞评论
                EventBus.getDefault().post(new EventbusValueBean("互动统计"));
                SnsDaily.refreshList();
                String[] snsMsgUi = new String[]{"am start -n com.tencent.mm/com.tencent.mm.plugin.sns.ui.SnsMsgUI"};
                ShellUtils.execCommand(snsMsgUi, true);
                SPUtil.putAndApply(MyApp.getmContext(), SNS_DAILY_SCROLL, true);
                break;
            case STATE_SEND_SNS:
                //发朋友圈
                EventBus.getDefault().post(new EventbusValueBean("发朋友圈"));
                break;
            case STATE_SHARE_MINI_PROJECT:
                //分享小程序
                String[] shareMiniProject = new String[]{"am start -n com.tencent.mm/com.tencent.mm.plugin.appbrand.ui.AppBrandLauncherUI"};
                ShellUtils.execCommand(shareMiniProject, true);
                EventBus.getDefault().post(new EventbusValueBean("分享小程序"));
                break;
            case STATE_GET_NICK :
                //获取微信昵称
                String username = (String) SPUtil.get(MyApp.getmContext(),USER_ID ,"");
                if (username.isEmpty()) return;

                break;
        }
        SPUtil.putAndApply(MyApp.getmContext(), WORK_STATE, task);
        EventBus.getDefault().post(new EventbusValueBean(task));
//        }

    }

    //判断 是否获取Root权限
    public static synchronized boolean getRootAhth() {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();
            int exitValue = process.waitFor();
            if (exitValue == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.d("*** DEBUG ***", "Unexpected error - Here is what I know: "
                    + e.getMessage());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
