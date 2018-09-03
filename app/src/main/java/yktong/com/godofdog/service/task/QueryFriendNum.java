package yktong.com.godofdog.service.task;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.ChatHistoryBean;
import yktong.com.godofdog.bean.FriendBean;
import yktong.com.godofdog.bean.NormalResultBean;
import yktong.com.godofdog.manager.WorkManger;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.util.PerformClickUtils;
import yktong.com.godofdog.util.SPUtil;
import yktong.com.godofdog.util.SupportUtil;
import yktong.com.godofdog.value.SpValue;
import yktong.com.godofdog.value.UrlValue;

import static yktong.com.godofdog.service.task.PraiseInSns.sleep;

/**
 * Created by vampire on 2017/6/16.
 */

public class QueryFriendNum {
    private static QueryFriendNum ourInstance;
    private AccessibilityService mService;
    private SupportUtil mSupportUtil;
    private boolean isfanily;
    private String total;
    private static ArrayList<FriendBean.CWechatFriend> cWechatFriends = new ArrayList<>();

    public static QueryFriendNum getInstance(AccessibilityService mService, SupportUtil mSupportUtil) {
        if (ourInstance == null) {
            synchronized (QueryFriendNum.class) {
                if (ourInstance == null) {
                    ourInstance = new QueryFriendNum(mService, mSupportUtil);
                }
            }
        }
        return ourInstance;
    }

    private QueryFriendNum(AccessibilityService mService, SupportUtil mSupportUtil) {
        this.mService = mService;
        this.mSupportUtil = mSupportUtil;
    }

    public void statisticsNum(String className) {

        if (className.equals(mSupportUtil.getLauncherUI())) {
            AccessibilityNodeInfo rootInActiveWindow = mService.getRootInActiveWindow();
            if (rootInActiveWindow == null) return;
            AccessibilityNodeInfo node = PerformClickUtils.getNode(mService, "com.tencent.mm:id/asa");
            if (node != null) {
                node.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                Log.d("QueryFriendNum", "滑动");
            }
            sleep(200);
            AccessibilityNodeInfo friendList = PerformClickUtils.getNode(mService, "com.tencent.mm:id/hy");
            if (friendList != null) {
                Log.d("QueryFriendNum", "friendList == null:" + (friendList == null));
                total = PerformClickUtils.getText(mService, "com.tencent.mm:id/ae8");
                isfanily = (null != total) && !total.isEmpty();
                while (!isfanily) {
                    total = PerformClickUtils.getText(mService, "com.tencent.mm:id/ae8");
                    isfanily = (null != total) && !total.isEmpty();
                    List<AccessibilityNodeInfo> nickList = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ig");
                    if (null != nickList && !nickList.isEmpty()) {
                        for (AccessibilityNodeInfo accessibilityNodeInfo : nickList) {
                            Log.d("QueryFriendNum", accessibilityNodeInfo.getText().toString());
                            cWechatFriends.add(new FriendBean.CWechatFriend(accessibilityNodeInfo.getText().toString()));
                        }
                        // TODO: 2017/7/6  昵称
                    }
                    friendList.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                    sleep(300);
                }
                if (isfanily) {
                    int indexOf = total.indexOf("位");
                    int num = Integer.parseInt(total.substring(0, indexOf));
                    ArrayList<FriendBean.CWechatFriend> cWechatFriend = (ArrayList<FriendBean.CWechatFriend>) removeDuplicate(QueryFriendNum.cWechatFriends);
                    FriendBean bean = new FriendBean(cWechatFriend);
                    Gson gson = new Gson();
                    String json = gson.toJson(bean);
                    Log.d("QueryFriendNum", json);

                    NetTool.getInstance().postRequest( UrlValue.UPDATE_FRIEND_LIST+MyApp.userId,json, NormalResultBean.class, new OnHttpCallBack<NormalResultBean>() {
                        @Override
                        public void onSuccess(NormalResultBean response) {
                            Log.d("QueryFriendNum", "response:" + response.toString());
                            Log.d("QueryFriendNum", response.getCode());
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });

                    NetTool.getInstance().startRequest("post", UrlValue.WECHAT_INFO + MyApp.userId + UrlValue.cWdcountfriend + num, NormalResultBean.class, new OnHttpCallBack<NormalResultBean>() {
                                @Override
                                public void onSuccess(NormalResultBean response) {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }
                            });

                    Log.d("QueryFriendNum", "num:" + num);
                    Log.d("QueryFriendNum", total);
                    //统计完成  切换回常规状态
                    boolean o = (boolean) SPUtil.get(MyApp.getmContext(), SpValue.IS_FIRST_TIME, false);
                    if (o){
                        PerformClickUtils.performBack(mService);
                        Log.d("QueryFriendNum", "检查群组");
                        WorkManger.getInstence().doTask(SpValue.STATE_CHECK_GROUP);
                    }
                }

            }
        }else {
            PerformClickUtils.performBack(mService);
        }
    }

    public static void refreshList() {
        cWechatFriends = new ArrayList<>();
        cWechatFriends.clear();
    }

    private List<FriendBean.CWechatFriend> removeDuplicate(List<FriendBean.CWechatFriend> list) {
        ArrayList<FriendBean.CWechatFriend> tempList = new ArrayList<>();
        ArrayList<FriendBean.CWechatFriend> resultList = new ArrayList<>();
        for (FriendBean.CWechatFriend bean : list) {
            tempList.add(bean);
        }
        for (FriendBean.CWechatFriend bean : list) {
            if (resultList.isEmpty()) {
                resultList.add(bean);
            } else {
                if (!checkList(resultList, bean)) {
                    resultList.add(bean);
                }
            }
        }
        return resultList;
    }

    private boolean checkList(List<FriendBean.CWechatFriend> list, FriendBean.CWechatFriend bean) {
        for (FriendBean.CWechatFriend chatHistoryBean : list) {
            if (chatHistoryBean.getNickName().equals(bean.getNickName())) {
                return true;
            }
        }
        return false;
    }

}