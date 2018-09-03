package yktong.com.godofdog.service.task;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.GroupBean;
import yktong.com.godofdog.bean.NormalResultBean;
import yktong.com.godofdog.bean.PostGroupBean;
import yktong.com.godofdog.manager.WorkManger;
import yktong.com.godofdog.tool.datebase.DBTool;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.util.PerformClickUtils;
import yktong.com.godofdog.util.SPUtil;
import yktong.com.godofdog.util.SupportUtil;
import yktong.com.godofdog.value.SpValue;
import yktong.com.godofdog.value.UrlValue;

/**
 * Created by vampire on 2017/6/23.
 */

public class CheckGroupList implements SpValue {
    private static CheckGroupList instence;

    private CheckGroupList() {
    }

    public static CheckGroupList getInstence() {
        if (instence == null) {
            synchronized (CheckGroupList.class) {
                if (instence == null) {
                    instence = new CheckGroupList();
                }
            }
        }
        return instence;
    }

    public void checkGroup(AccessibilityService mService, SupportUtil mSupport, String className) {
        if (className.equals(mSupport.getGroupListUi())) {
            AccessibilityNodeInfo rootWindow = mService.getRootInActiveWindow();
            if (rootWindow == null) return;
            AccessibilityNodeInfo list = PerformClickUtils.getNode(mService, mSupport.getGroupListId());
            if (list == null) return;
            List<AccessibilityNodeInfo> names = rootWindow.findAccessibilityNodeInfosByViewId(mSupport.getGroupNameID());
            String text = PerformClickUtils.getText(mService, "com.tencent.mm:id/hz");
            if (null != text && text.contains("你可以通过群聊中的“保存到通讯录”选项，将其保存到这里")){
                Log.d("CheckGroupList", "没有群组");
                PerformClickUtils.performBack(mService);
                WorkManger.getInstence().doTask(SpValue.STATE_COMMENT);
            }
            if (null != names) {
                for (AccessibilityNodeInfo name : names) {
                    String groupName = name.getText().toString();
                    Log.d("CheckGroupList", groupName);
                    String dest = "";
                    if (groupName!=null) {
                        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                        Matcher m = p.matcher(groupName);
                        dest = m.replaceAll("");
                    }
                    DBTool.getInstance().saveGroup(new GroupBean(dest));
//                    Timer timer = new Timer();
//                    timer.schedule(new TimerTask() {
//                        @Override
//                        public void run() {

//                        }
//                    }, 1500);
                }
                DBTool.getInstance().queryGroup(new DBTool.QueryListener() {
                    @Override
                    public <T> void onQueryListener(List<T> list) {
                        List<GroupBean> been = (List<GroupBean>) list;
                        ArrayList<PostGroupBean.CWechatQun> groupName = new ArrayList<PostGroupBean.CWechatQun>();
                        for (GroupBean groupBean : been) {
                            groupName.add(new PostGroupBean.CWechatQun(groupBean.getGroupName(), MyApp.userId));
                        }
                        if (groupName.isEmpty()) return;
                        PostGroupBean bean = new PostGroupBean(groupName);
                        Gson gson = new Gson();
                        String json = gson.toJson(bean);
                        Log.d("CheckGroupList", json);
                        NetTool.getInstance().postRequest(UrlValue.REFRSH_GROUP, json, NormalResultBean.class, new OnHttpCallBack<NormalResultBean>() {
                            @Override
                            public void onSuccess(NormalResultBean response) {
//                                SPUtil.putAndApply(MyApp.getmContext(),IS_FIRST_TIME,false);
                                PerformClickUtils.performBack(mService);
                                WorkManger.getInstence().doTask(SpValue.STATE_COMMENT);
                            }

                            @Override
                            public void onError(Throwable e) {
                            }
                        });
                    }
                });

            }else {
                Log.d("CheckGroupList", "else");
                PerformClickUtils.performBack(mService);
                WorkManger.getInstence().doTask(SpValue.STATE_COMMENT);
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            list.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);

        }
    }
}
