package yktong.com.godofdog.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.EventbusValueBean;
import yktong.com.godofdog.bean.NormalResultBean;
import yktong.com.godofdog.manager.WorkManger;
import yktong.com.godofdog.service.task.AddByGroup;
import yktong.com.godofdog.service.task.AddLinkman;
import yktong.com.godofdog.service.task.CheckGroupList;
import yktong.com.godofdog.service.task.GetNick;
import yktong.com.godofdog.service.task.InteractFans;
import yktong.com.godofdog.service.task.PraiseInSns;
import yktong.com.godofdog.service.task.QueryFriendNum;
import yktong.com.godofdog.service.task.ShareMiniProject;
import yktong.com.godofdog.service.task.ShareToPerson;
import yktong.com.godofdog.service.task.ShareToSns;
import yktong.com.godofdog.service.task.SnsDaily;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.util.PerformClickUtils;
import yktong.com.godofdog.util.SPUtil;
import yktong.com.godofdog.util.SupportUtil;
import yktong.com.godofdog.value.SpValue;
import yktong.com.godofdog.value.UrlValue;

/**
 * Created by vampire on 2017/6/13.
 */

public class WeService extends AccessibilityService implements SpValue {
    private SupportUtil supportUtil;
    private WeService mService;
    public float wight;
    public float height;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        mService = this;
        String version = getVersion(this);
        supportUtil = new SupportUtil(version);
        Log.d("WeService", version);
        Toast.makeText(mService, "微信版本 : " + version, Toast.LENGTH_SHORT).show();
        String wightS = (String) SPUtil.get(MyApp.getmContext(), SCREEN_WIDE, "1.0");
        String heightS = (String) SPUtil.get(MyApp.getmContext(), SCREEN_HEIGHT, "1.0");
        wight = Float.parseFloat(wightS);
        height = Float.parseFloat(heightS);
        SPUtil.putAndApply(MyApp.getmContext(),SNS_UI,true); // 开始统计 朋友圈
        SPUtil.putAndApply(MyApp.getmContext(),MARKET_UI , true);// 开始营销统计
        EventBus.getDefault().post(new EventbusValueBean("检查"));
        Timer update = new Timer();
        update.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("WeService", "ssssss");
//                WorkManger.getInstence().doTask(SpValue.STATE_COMMENT);
                SPUtil.putAndApply(MyApp.getmContext(),SpValue.IS_FIRST_TIME,true);
                boolean o = (boolean) SPUtil.get(MyApp.getmContext(), SpValue.IS_FIRST_TIME, false);
//        Log.d(TAG, "o:" + o);
                if (o){
                    Looper.prepare();
                    WorkManger.getInstence().doTask(SpValue.STATE_QUERY_FRIENDS_NUM);
                    Looper.loop();
                }
            }
        },10000);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        String atyName = event.getClassName().toString();
        if (event.getEventType() == 32) {
            Log.d("WeService", "类名：" + atyName);
        }
        // TODO: 2017/6/29  状态改变
        int workState = (int) SPUtil.get(MyApp.getmContext(), WORK_STATE, STATE_NORMAL);
        boolean isChecking = MyApp.mSettingHelper.getBoolean(SpValue.IS_CHECK_NAME, false);
        if (isChecking) workState = STATE_GET_NICK;
        switch (workState) {
            case STATE_NORMAL:
                // TODO: 2017/6/13  doNothing
                break;
            case STATE_ADD_LINK:
                // TODO: 2017/6/13 通讯录添加
                AddLinkman.getInstence().addFriendByLink(supportUtil, atyName, mService);
                break;
            case STATE_PRAISE:
                // TODO: 2017/6/16  点赞
                PraiseInSns.getInstance(supportUtil, mService).praiseInSns(atyName);
                break;
            case STATE_QUERY_FRIENDS_NUM:
                // 统计好友数量
                QueryFriendNum.getInstance(mService, supportUtil).statisticsNum(atyName);
                break;
            case STATE_CHECK_GROUP:
                // 统计群列表
                CheckGroupList.getInstence().checkGroup(mService, supportUtil, atyName);
                break;
            case STATE_ADD_GROUP_FRIEND:
                //添加群组好友
                AddByGroup.getInstence().addGroupFriends(mService, supportUtil, atyName, event);
                break;
            case STATE_INTERACT:
                // 获取今日聊天数
                InteractFans.getInstance().takeInteractNum(mService, supportUtil, atyName);
                break;
            case STATE_COMMENT:
                // 互动统计
                SnsDaily.refreshList();
                SnsDaily.getInstance().snsDaily(mService, supportUtil, atyName);
                break;
            case STATE_SEND_SNS:
                //发圈
                ShareToSns.getInstance().share(mService,supportUtil,atyName);
                break;
            case STATE_SEND_PERSON:
                // 营销
                ShareToPerson.getInstance().share(mService,supportUtil,atyName);
                break;
            case STATE_SHARE_MINI_PROJECT:
                ShareMiniProject.getInstance().sendMiniProject(mService,supportUtil,atyName,event);
                break;
            case STATE_GET_NICK:
                //获取昵称
                GetNick.getInstance().getWxNick(mService,supportUtil,atyName);
                break;
        }
        // TODO: 2017/6/16  新增好友  通知栏监听
        updateFriends(event);
        //刷新朋友圈和营销数 数据
        marketAndSnsData(atyName,event);
    }

    private void marketAndSnsData(String atyName , AccessibilityEvent event) {
        boolean needAddMarket = (boolean) SPUtil.get(MyApp.getmContext(),MARKET_UI,true);
        boolean needAddSns = (boolean) SPUtil.get(MyApp.getmContext(),SNS_UI,true);
        if (atyName.equals("com.tencent.mm.ui.transmit.SelectConversationUI")){
            if (needAddMarket){
                int marketingNum = (int) SPUtil.get(MyApp.getmContext(),MARKETING_NUM,0);
                marketingNum = marketingNum+1;
                SPUtil.putAndApply(MyApp.getmContext(),MARKETING_NUM,marketingNum);
                updateData(UrlValue.cWdtodaymarket,marketingNum);
                SPUtil.putAndApply(MyApp.getmContext(),MARKET_UI,false);
            }
            SPUtil.putAndApply(MyApp.getmContext(),SNS_UI,true);
        }else if (atyName.contains(supportUtil.getSendSnsUi())){
            if (needAddSns){
                int snsNum = (int) SPUtil.get(MyApp.getmContext(),SNS_NUM,0);
                snsNum = snsNum+1;
                SPUtil.putAndApply(MyApp.getmContext(),SNS_NUM,snsNum);
                updateData(UrlValue.cWdtodayquan,snsNum);
                SPUtil.putAndApply(MyApp.getmContext(),SNS_UI,false);
            }
            SPUtil.putAndApply(MyApp.getmContext(),MARKET_UI,true);
        }else if (atyName.equals(supportUtil.getLauncherUI())){
            SPUtil.putAndApply(MyApp.getmContext(),MARKET_UI,true);
            SPUtil.putAndApply(MyApp.getmContext(),SNS_UI,true);
        }else if (atyName.equals("com.tencent.mm.ui.base.h")){
            String text = PerformClickUtils.getText(mService, "com.tencent.mm:id/bu1");
            if (null!= text&&text.equals("删除联系人")){
                PerformClickUtils.findViewIdAndClick(mService,"com.tencent.mm:id/ad7");
                EventBus.getDefault().post(new EventbusValueBean("删除好友操作已取消"));
            }
            for (CharSequence charSequence : event.getText()) {
                if (charSequence.toString().equals("退出当前帐号")){
                    EventBus.getDefault().post(new EventbusValueBean("退出微信操作已取消"));
                    PerformClickUtils.performBack(mService);
                }else if (charSequence.toString().equals("退出后不会删除任何历史数据，下次登录依然可以使用本帐号。")){
                    PerformClickUtils.findViewIdAndClick(mService,"com.tencent.mm:id/ad7");
                    EventBus.getDefault().post(new EventbusValueBean("退出微信操作已取消"));
                }
            }
        }
    }

    private void updateData(String param, int data) {
        NetTool.getInstance().startRequest("post", UrlValue.WECHAT_INFO+MyApp.userId+"&" + param + data, NormalResultBean.class, new OnHttpCallBack<NormalResultBean>() {
            @Override
            public void onSuccess(NormalResultBean response) {
                if (response.getCode().equals("1")){
                    Log.d("WeService", response.toString()) ;
                }
                Log.d("WeService", param + " ++" + "    " + data);
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void updateFriends(AccessibilityEvent event) {
//        for (CharSequence charSequence : event.getText()) {
//            Log.d("WeService", "charSequence:" + charSequence);
//            if (charSequence.toString().equals("已转发")){
//                WorkManger.getInstence().doTask(SpValue.STATE_NORMAL);
//            }
//        }
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                List<CharSequence> texts = event.getText();
                if (!texts.isEmpty()) for (CharSequence text : texts) {
                    String tex = (String) text;
                    Log.d("WeService", tex);
                    if (tex.contains("我通过了你的朋友验证请求，现在我们可以开始聊天了")) {
                        int i = tex.indexOf(":");
                        Log.d("WeService", tex.substring(0, i));
                        Log.d("WeService", "新加好友");
                        NetTool.getInstance().startRequest("get", UrlValue.ADD_WE_FRIEND + MyApp.userId, NormalResultBean.class, new OnHttpCallBack<NormalResultBean>() {
                            @Override
                            public void onSuccess(NormalResultBean response) {
                                int code = Integer.parseInt(response.getCode());
                                if (code == 1) {
                                    Log.d("WeService", "好友更新");
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
                    }else if (tex.contains("梁家涛")){
//                        int i = tex.indexOf(":");
//                        String miniName = tex.substring(i + 1, tex.length());
//                        Log.d("WeService", miniName);
//                        SPUtil.putAndApply(MyApp.getmContext(),SpValue.MINI_PROJCET_NAME,miniName);
//                        WorkManger.getInstence().doTask(SpValue.STATE_SHARE_MINI_PROJECT);
                    }
                }
                break;
        }
    }

    @Override
    public void onInterrupt() {

    }

    /**
     * 获取微信的版本号
     *
     * @param context
     * @return
     */
    private String getVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);

        for (PackageInfo packageInfo : packageInfoList) {
            if ("com.tencent.mm".equals(packageInfo.packageName)) {
                return packageInfo.versionName;
            }
        }
        return "6.5.4";
    }

    public float getWight() {
        return wight;
    }

    public float getHeight() {
        return height;
    }
}
