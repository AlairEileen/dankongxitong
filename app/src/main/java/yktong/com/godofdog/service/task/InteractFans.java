package yktong.com.godofdog.service.task;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.ChatHistoryBean;
import yktong.com.godofdog.bean.NormalResultBean;
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
 * Created by vampire on 2017/6/29.
 */

public class InteractFans {
    private static InteractFans ourInstance;
    private static ArrayList<ChatHistoryBean> maps = new ArrayList<>();
    private boolean needScroll = true;

    public static InteractFans getInstance() {
        if (ourInstance == null) {
            synchronized (InteractFans.class) {
                if (ourInstance == null) {
                    ourInstance = new InteractFans();
                }
            }
        }
        return ourInstance;
    }

    private InteractFans() {

    }

    public void takeInteractNum(WeService mService, SupportUtil supportUtil, String atyName) {
        AccessibilityNodeInfo rootInActiveWindow = mService.getRootInActiveWindow();
        if (rootInActiveWindow == null) return;
        if (atyName.equals(supportUtil.getLauncherUI())) {
            while (needScroll) {
                needScroll = (boolean) SPUtil.get(MyApp.getmContext(), SpValue.IS_SCROLL, true);
                List<AccessibilityNodeInfo> itemList = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/agu");
                if (null!=itemList){
                    if (itemList.size()<6){
                        SPUtil.putAndApply(MyApp.getmContext(), SpValue.IS_SCROLL, false);
                    }
                }
                List<AccessibilityNodeInfo> nicknameList = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/agw");//昵称
                if (null != nicknameList && !nicknameList.isEmpty()) {
                    List<AccessibilityNodeInfo> timeList = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/agx");
                    for (int i = 0; i < timeList.size(); i++) {
                        String time = timeList.get(i).getText().toString();
                        if (!time.contains(":")) {
                            SPUtil.putAndApply(MyApp.getmContext(), SpValue.IS_SCROLL, false);
                        } else {
                            try{
                                ChatHistoryBean bean = new ChatHistoryBean(time);
                                AccessibilityNodeInfo nickItem = nicknameList.get(i);
                                bean.setName(nickItem.getText().toString());
                                maps.add(bean);
                            }catch (IndexOutOfBoundsException e){
                                e.printStackTrace();
                            }

                        }
                    }
                }
                if (needScroll) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    List<AccessibilityNodeInfo> listView = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/bny");
                    if (null != listView && !listView.isEmpty()) {
                        listView.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                    }
                }
            }
            List<ChatHistoryBean> historyBeen = removeDuplicate(maps);
            Log.d("InteractFans", "今日聊天数: " + historyBeen.size());
 
            NetTool.getInstance().startRequest("post", UrlValue.WECHAT_INFO + MyApp.userId + "&cWdtodaychat=" + historyBeen.size(), NormalResultBean.class, new OnHttpCallBack<NormalResultBean>() {
                @Override
                public void onSuccess(NormalResultBean response) {
                    PerformClickUtils.performBack(mService);
                    WorkManger.getInstence().doTask(SpValue.STATE_NORMAL);
                    SPUtil.putAndApply(MyApp.getmContext(),SpValue.IS_FIRST_TIME,false);
                    Log.d("InteractFans", "聊天数");
                }

                @Override
                public void onError(Throwable e) {

                }
            });
//            WorkManger.getInstence().doTask(SpValue.STATE_NORMAL);
            for (ChatHistoryBean bean : historyBeen) {
                Log.d("InteractFans", bean.toString());
            }


        }

    }

    public static void refreshList() {
        maps.clear();
    }

    public List<ChatHistoryBean> removeDuplicate(List<ChatHistoryBean> list) {
        ArrayList<ChatHistoryBean> tempList = new ArrayList<>();
        ArrayList<ChatHistoryBean> resultList = new ArrayList<>();
        for (ChatHistoryBean bean : list) {
            tempList.add(bean);
        }
        for (ChatHistoryBean bean : list) {
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

    public boolean checkList(List<ChatHistoryBean> list, ChatHistoryBean bean) {
        for (ChatHistoryBean chatHistoryBean : list) {
            if (chatHistoryBean.getName().equals(bean.getName())) {
                return true;
            }
        }
        return false;
    }

}