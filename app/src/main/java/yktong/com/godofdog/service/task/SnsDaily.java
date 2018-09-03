package yktong.com.godofdog.service.task;

import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.NormalResultBean;
import yktong.com.godofdog.bean.SnsDailyBean;
import yktong.com.godofdog.manager.WorkManger;
import yktong.com.godofdog.service.WeService;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.util.PerformClickUtils;
import yktong.com.godofdog.util.SPUtil;
import yktong.com.godofdog.util.SupportUtil;
import yktong.com.godofdog.value.SpValue;
import yktong.com.godofdog.value.UrlValue;

/**
 * 朋友圈 交互统计
 * Created by vampire on 2017/6/30.
 */

public class SnsDaily implements SpValue {
    private static SnsDaily ourInstance;
    private boolean needScroll = true;
    private static ArrayList<SnsDailyBean> been;
    private int temp = 0;
    private static int index = 0;

    public static SnsDaily getInstance() {
        if (ourInstance == null) {
            synchronized (SnsDaily.class) {
                if (ourInstance == null) {
                    ourInstance = new SnsDaily();
                }
            }
        }
        return ourInstance;

    }

    private SnsDaily() {
    }

    public static void refreshList() {
        index = 0;
        been = new ArrayList<>();
        been.clear();
    }

    public void snsDaily(WeService mService, SupportUtil supportUtil, String atyName) throws IndexOutOfBoundsException {
        AccessibilityNodeInfo rootInActiveWindow = mService.getRootInActiveWindow();
        if (rootInActiveWindow == null) return;
        // TODO: 2017/6/30 com.tencent.mm.plugin.sns.ui.SnsUserUI
        if (atyName.equals("com.tencent.mm.plugin.sns.ui.SnsUserUI")) {

        } else if (atyName.equals("com.tencent.mm.plugin.sns.ui.SnsMsgUI")) {
            needScroll = (boolean) SPUtil.get(MyApp.getmContext(), SNS_DAILY_SCROLL, true);
//            Log.d("SnsDaily", "needScroll:" + needScroll);
            synchronized (SnsDaily.class) {
                while (needScroll) {
                    String text = PerformClickUtils.getText(mService, "com.tencent.mm:id/crt");
                    if (null != text && text.equals("暂无消息")) {
                        SPUtil.putAndApply(MyApp.getmContext(), SNS_DAILY_SCROLL, false);
                        NetTool.getInstance().startRequest("post", UrlValue.WECHAT_INFO + MyApp.userId + UrlValue.cWdtodayinteract + 0, NormalResultBean.class,
                                new OnHttpCallBack<NormalResultBean>() {
                                    @Override
                                    public void onSuccess(NormalResultBean response) {
                                        PerformClickUtils.performBack(mService);
                                        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
                                        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
                                        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                                        MyApp.getmContext().startActivity(mHomeIntent);
                                        WorkManger.getInstence().doTask(SpValue.STATE_INTERACT);
                                        Log.d("SnsDaily", "互动数统计完成");
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }
                                });
                        break;
                    }
                    needScroll = (boolean) SPUtil.get(MyApp.getmContext(), SNS_DAILY_SCROLL, true);
                    if (!needScroll) break;
//                    Log.d("SnsDaily", "needScroll:" + needScroll);
                    List<AccessibilityNodeInfo> listview = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/crs");
                    List<AccessibilityNodeInfo> timeInfos = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/cro");
                    List<AccessibilityNodeInfo> contentList = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/crn");
                    List<AccessibilityNodeInfo> nickList = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/crl");
                    List<AccessibilityNodeInfo> items = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/pk");
                    if (null != timeInfos && !timeInfos.isEmpty()) {
                        for (int i = 0; i < timeInfos.size(); i++) {
                            String time = timeInfos.get(i).getText().toString();
                            String content = null;
                            Log.d("SnsDaily", time);
                            if (!time.contains("日") && !time.contains("昨天")) {
                                index++;
                                Log.d("SnsDaily", "index:" + index);
                                if (index >= 5){
                                    SPUtil.putAndApply(MyApp.getmContext(),SNS_DAILY_SCROLL,false);
                                    ArrayList<SnsDailyBean> snsDailyBeen = checkList(been);
                                    NetTool.getInstance().startRequest("post", UrlValue.WECHAT_INFO + MyApp.userId + UrlValue.cWdtodayinteract + snsDailyBeen.size(), NormalResultBean.class,
                                            new OnHttpCallBack<NormalResultBean>() {
                                                @Override
                                                public void onSuccess(NormalResultBean response) {
                                                    PerformClickUtils.performBack(mService);
                                                    Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
                                                    mHomeIntent.addCategory(Intent.CATEGORY_HOME);
                                                    mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                            | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                                                    MyApp.getmContext().startActivity(mHomeIntent);
                                                    WorkManger.getInstence().doTask(SpValue.STATE_INTERACT);
                                                    Log.d("SnsDaily", "互动数统计完成");
                                                }

                                                @Override
                                                public void onError(Throwable e) {

                                                }
                                            });
                                }
                                try {
                                    if (contentList.get(i).getText().toString().trim().isEmpty()) {
                                        content = "点赞";
                                    } else {
                                        content = contentList.get(i).getText().toString();
                                    }
                                    SnsDailyBean bean = new SnsDailyBean(nickList.get(i).getText().toString(), content, time);
                                    Log.d("SnsDaily", bean.toString());
                                    been.add(bean);
                                } catch (IndexOutOfBoundsException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                temp++;
                                SPUtil.putAndApply(MyApp.getmContext(), SNS_DAILY_SCROLL, false);
                                break;
                            }
                        }
                    }
                    if (listview != null && !listview.isEmpty()) {
                        listview.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                    }
                }
                Log.d("SnsDaily", "temp:" + temp);
                if (temp >= 1) {
                    ArrayList<SnsDailyBean> snsDailyBeen = checkList(been);
                    NetTool.getInstance().startRequest("post", UrlValue.WECHAT_INFO + MyApp.userId + UrlValue.cWdtodayinteract + snsDailyBeen.size(), NormalResultBean.class,
                            new OnHttpCallBack<NormalResultBean>() {
                                @Override
                                public void onSuccess(NormalResultBean response) {
                                    PerformClickUtils.performBack(mService);
                                    Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
                                    mHomeIntent.addCategory(Intent.CATEGORY_HOME);
                                    mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                            | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                                    MyApp.getmContext().startActivity(mHomeIntent);
                                    WorkManger.getInstence().doTask(SpValue.STATE_INTERACT);
                                    Log.d("SnsDaily", "互动数统计完成");
                                }

                                @Override
                                public void onError(Throwable e) {

                                }
                            });
                }

            }
        }

    }

    private ArrayList<SnsDailyBean> checkList(ArrayList<SnsDailyBean> been) {
        ArrayList<SnsDailyBean> resultList = new ArrayList<>();
        for (int i = 0; i < been.size(); i++) {
            if (i == 0) {
                resultList.add(been.get(i));
            } else {
                if (!checkList(been, resultList, i)) {
                    resultList.add(been.get(i));
                }
            }
        }
        return resultList;
    }

    private boolean checkList(ArrayList<SnsDailyBean> been, ArrayList<SnsDailyBean> resultList, int i) {
        for (SnsDailyBean bean : resultList) {
            try {
                SnsDailyBean bean1 = been.get(i);
                if (bean.getNickName().equals(bean1.getNickName())
                        && bean.getContent().equals(bean1.getContent())
                        && bean.getTime().equals(bean1.getTime())) {
                    return true;
                }
            } catch (IndexOutOfBoundsException e) {
                Log.d("SnsDaily", e.getMessage());
            }

        }
        return false;
    }


}