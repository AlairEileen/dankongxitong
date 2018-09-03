package yktong.com.godofdog.service;

import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import yktong.com.godofdog.activity.MainActivity;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.EventbusValueBean;
import yktong.com.godofdog.bean.FriendNickListBean;
import yktong.com.godofdog.bean.NumberBean;
import yktong.com.godofdog.bean.TaskListBean;
import yktong.com.godofdog.bean.UserBean;
import yktong.com.godofdog.bean.wechat.WxAvatarBean;
import yktong.com.godofdog.manager.WorkManger;
import yktong.com.godofdog.notification.WindowNotification;
import yktong.com.godofdog.receiver.AlarmReciver;
import yktong.com.godofdog.tool.OverTask;
import yktong.com.godofdog.tool.datebase.DBTool;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.tool.thread.ThreadPool;
import yktong.com.godofdog.util.AccessWatcher;
import yktong.com.godofdog.util.time.DateUtils;
import yktong.com.godofdog.util.InsertLinkMan;
import yktong.com.godofdog.util.SPUtil;
import yktong.com.godofdog.util.VoiceUtil;
import yktong.com.godofdog.value.SpValue;
import yktong.com.godofdog.value.Strings;
import yktong.com.godofdog.value.UrlValue;

import static android.icu.text.DateTimePatternGenerator.DAY;

/**
 * Created by vampire on 2017/6/13.
 */

public class MyService extends Service implements SpValue {

    private static final String TAG = "vampire_MyService";

    private Handler mHandler =
            new Handler(Looper.getMainLooper());
    private Context mContext;
    private WindowNotification notification;
    private TaskListBean.TaskBean taskBean;
    private Timer workTimer;
    private List<String> friendname;
    private int index;
    private boolean isScreenOff;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(EventbusValueBean bean) {
        Log.d(TAG, "EVENT_BUS");
        try {
            if (bean.getO()instanceof Integer){
                int task = (int) bean.getO();
                Log.d(TAG, "task:" + task);
                String content = "";
                content = task+"";
                switch (task) {
                    case STATE_NORMAL:
                        content = "待机";
                        break;
                    case STATE_ADD_LINK:
                        content = "添加通讯录";
                        break;
                    case STATE_PRAISE:
                        content = "点赞";
                        break;
                    case STATE_QUERY_FRIENDS_NUM:
                        content = "统计好友数量";
                        break;
            }
                notification = new WindowNotification(mContext);
                Notification show = notification.setContent(content);
                startForeground(1, show);
            }else if (bean.getO()instanceof HashMap){
                HashMap<String ,String> hashMap = (HashMap<String, String>) bean.getO();
                String username = hashMap.get(SpValue.USER_ID);
                Log.d(TAG, username);
                SPUtil.putAndApply(MyApp.getmContext(),SpValue.USER_ID,username);
                WorkManger.getInstence().doTask(STATE_GET_NICK);
//                SPUtil.putAndApply(MyApp.getmContext(),SpValue.IS_CHECK_NAME,true);


            }
        } catch (Exception e) {

            String o = (String) bean.getO();
            if (o.equals("下一个")){
                index++;
                if (index == friendname.size()){
                    OverTask.getInstance().finishTask();
                    WorkManger.getInstence().doTask(SpValue.STATE_NORMAL);
                }else {
                    String nick = friendname.get(index);
                    SPUtil.putAndApply(MyApp.getmContext(),SHARE_NAME,nick);
                }
            }else {
                Toast.makeText(mContext, o, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void saveAvatar(WxAvatarBean avatarBean){
        Log.d(TAG, avatarBean.getFilePath());
        SPUtil.putAndApply(MyApp.getmContext(),avatarBean.getUsername(),avatarBean.getFilePath());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        mContext = this;
        workTimer = new Timer();

        bindNotification();

        checkAccessibility();

        checkUserState();

        checkTask();

        weakup();

        updateDate();

        bindAlarm();

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new PhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);  //注册监听器 监听电话状态

        final IntentFilter filter = new IntentFilter();

        BroadcastReceiver mBatInfoReceiver = screenInfo(filter);

        registerReceiver(mBatInfoReceiver, filter);

//        installPlugin();
    }

    private void installPlugin() {
        String s = "/assets/plugin.apk";
        String path = "file://android_asset/plugin.apk";
        try {
            InputStream inputStream = mContext.getAssets().open(path);
            FileOutputStream fos = new FileOutputStream(new File("/storage/emulated/0/MyPlugin/plugin.apk"));
            byte[] buffer = new byte[1024];
            int byteCount=0;
            while ((byteCount = inputStream.read(buffer))!=-1){
                fos.write(buffer,0,byteCount);
            }
            fos.flush();
            inputStream.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    @NonNull
    private BroadcastReceiver screenInfo(IntentFilter filter) {
        // 屏幕灭屏广播
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        // 屏幕亮屏广播
        filter.addAction(Intent.ACTION_SCREEN_ON);
        // 屏幕解锁广播
        filter.addAction(Intent.ACTION_USER_PRESENT);
        // 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播
        // example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
        // 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

        return new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                Log.d(TAG, "onReceive");
                String action = intent.getAction();

                if (Intent.ACTION_SCREEN_ON.equals(action)) {
                    isScreenOff = false;
                    Log.d(TAG, "screen on");
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    isScreenOff = true;
                    Log.d(TAG, "screen off");
                } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                    isScreenOff = true;
                    Log.d(TAG, "screen unlock");
                } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
                    Log.i(TAG, " receive Intent.ACTION_CLOSE_SYSTEM_DIALOGS");
                }
            }
        };
    }

    private void bindAlarm() {
        Intent intent = new Intent(mContext, AlarmReciver.class);
        PendingIntent sender = PendingIntent.getBroadcast(mContext, 0, intent, 0);

        long firstTime = SystemClock.elapsedRealtime(); // 开机之后到现在的运行时间(包括睡眠时间)
        long systemTime = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        // 这里时区需要设置一下，不然会有8个小时的时间差
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.MINUTE, 01);
        calendar.set(Calendar.HOUR_OF_DAY, 01);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // 选择的定时时间
        long selectTime = calendar.getTimeInMillis();
        // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if (systemTime > selectTime) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }
        // 计算现在时间到设定时间的时间差
        long time = selectTime - systemTime;
        firstTime += time;
        // 进行闹铃注册
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                firstTime, DAY, sender);
        Log.i(TAG, "time ==== " + time + ", selectSecond ===== "
                + selectTime + ", systemTime ==== " + systemTime + ", firstTime === " + firstTime);
    }

    private void updateDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");//小写的mm表示的是分钟
        String dstr = "08:45";
        Date date = null;
        try {
            date = sdf.parse(dstr);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "e:" + e);
        }
        Log.d(TAG, "date==null:" + (date == null));
    }

    private void checkTask() {
        ThreadPool.thredP.execute(() -> {
            for (; ; ) {
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                NetTool.getInstance().startRequest("get", UrlValue.GET_TASK_LIST + MyApp.userId, TaskListBean.class, new OnHttpCallBack<TaskListBean>() {
                    @Override
                    public void onSuccess(TaskListBean response) {
                        if (response.getCode().equals("1")) {
                            long time = System.currentTimeMillis();
                            Log.d(TAG, "time:" + time);
                            List<TaskListBean.TaskBean> task = response.getTask();

                            takeTask(task, time);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
            }
        });
    }

    private void takeTask(List<TaskListBean.TaskBean> task, long time) throws NullPointerException{
        long temp = Integer.MAX_VALUE;
        int position = 0;
        for (int i = 0; i < task.size(); i++) {
            long cStarttime = task.get(i).getCStarttime();
            if (cStarttime < time) continue;
            if (cStarttime - time < temp) {
                temp = cStarttime - time;
                position = i;
            }
        }
        final TaskListBean.TaskBean taskBean = task.get(position);
        long cStarttime = taskBean.getCStarttime();
        Log.d(TAG, DateUtils.times(cStarttime));
        if (cStarttime<time) return;
        Date date1=new Date(cStarttime);
        Date date2 = new Date(time);
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
        String time1 =formatter.format(date1);
        String time2 = formatter.format(date2);
        Log.d(TAG, "time1.equals(time2):" + time1.equals(time2));
        if (time1.equals(time2)){
            Log.d(TAG, "启动时间 : "+DateUtils.times(cStarttime));
            workTimer.cancel();
            workTimer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    wakeUpAndUnlock(mContext);
                    SPUtil.putAndApply(MyApp.getmContext(),SpValue.TASK_ID,taskBean.getCTaskid());
                    switch (taskBean.getCTasktype()) {
                        case 1: //精准拓客
                            Log.d(TAG, "System.currentTimeMillis():" + System.currentTimeMillis());
                            Log.d(TAG, "精准拓客");
                            tokerAccurate(taskBean);
                            break;
                        case 2: // 社群拓客
                            Log.d(TAG, "社群拓客");
                            tokerGroup(taskBean);
                            break;
                        case 3:
                            //营销
                            Log.d(TAG, "定时营销");
                            shareInfo(taskBean);
                            break;
                        case 4:
                            // 互动
                            Log.d(TAG, "定时互动");
                            interactInfo(taskBean);
                            break;
                        case 5:
                            // 朋友圈
                            Log.d(TAG, "朋友圈");
                            shareInfo(taskBean);
                            break;
                        case 6:
                            // 分享小程序
                            Log.d(TAG, "分享小程序");
//                            SPUtil.putAndApply(MyApp.getmContext(), SpValue.IS_FIRST_TIME, true);

                            String miniName = taskBean.getcCxcname();
                            SPUtil.putAndApply(MyApp.getmContext(),SpValue.MINI_PROJCET_NAME,miniName);
                            NetTool.getInstance().startRequest("get", UrlValue.GET_TASK_NICK_NAME + taskBean.getCTaskid(), FriendNickListBean.class, new OnHttpCallBack<FriendNickListBean>() {
                                @Override
                                public void onSuccess(FriendNickListBean response) {
                                    if (response.getCode().equals("1")){
                                        friendname = response.getFriendname();
                                        index = 0;
                                        String name = friendname.get(index);
                                        for (String s : friendname) {
                                            Log.d(TAG, s);
                                        }
                                        SPUtil.putAndApply(MyApp.getmContext(),SpValue.SHARE_NAME,name);
                                        WorkManger.getInstence().doTask(SpValue.STATE_SHARE_MINI_PROJECT);
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {

                                }
                            });

                        case 7:
                            break;
                    }
                }
            };

            workTimer.schedule(timerTask, date1);
        }

    }

    private void shareInfo(TaskListBean.TaskBean taskBean) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Strings.IS_SHARE, taskBean.getCTasktype());
        intent.putExtra(Strings.PASSAGE_ID, taskBean.getcLibraryid());
        intent.putExtra(Strings.TASK_ID,taskBean.getCTaskid());
        intent.setClass(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    //定时互动
    private void interactInfo(TaskListBean.TaskBean taskBean) {
        int cNum = taskBean.getCNum();
        SPUtil.putAndApply(MyApp.getmContext(), SpValue.INTERACT_NUM, cNum);
        WorkManger.getInstence().doTask(SpValue.STATE_PRAISE);
    }

    //社群拓客
    private void tokerGroup(TaskListBean.TaskBean taskBean) {
        SPUtil.putAndApply(MyApp.getmContext(), SpValue.GROUP_NAME, taskBean.getcWechatqun());
        int cNum = taskBean.getCNum();
        SPUtil.putAndApply(MyApp.getmContext(), SpValue.ADD_NUM, cNum);
        SPUtil.putAndApply(MyApp.getmContext(), SpValue.CONTENT, taskBean.getcCheckmage());
        WorkManger.getInstence().doTask(SpValue.STATE_ADD_GROUP_FRIEND);

    }

    //精准拓客
    private void tokerAccurate(TaskListBean.TaskBean taskBean) {
        NetTool.getInstance().startRequest("post", UrlValue.QUERY_PHONE + taskBean.getCTaskid(), NumberBean.class, new OnHttpCallBack<NumberBean>() {
            @Override
            public void onSuccess(NumberBean response) {
                if (response.getCode().equals("1")) {
                    for (String num : response.getPhone()) {
                        InsertLinkMan.insert(num, "动销-" + num);
                    }
                    String content = taskBean.getcCheckmage();
                    if (null == content) content = "你好，我们好像在哪见过。";
                    SPUtil.putAndApply(MyApp.getmContext(), CONTENT, content);
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            WorkManger.getInstence().doTask(SpValue.STATE_ADD_LINK);
                        }
                    }, 60000);
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    //校验用户状态
    private void checkUserState() {
        ThreadPool.thredP.execute(() -> {
            for (; ; ) {
                try {
                    Thread.sleep(1000);
                    if (MyApp.userId == -1) {
                        UserBean userBean = DBTool.getInstance().queryUser();
                        MyApp.bindPersonInfo(userBean);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //语音唤醒
    private void weakup() {
        VoiceUtil.getInstance().wakeup(mContext);
        // 非空判断，防止因空指针使程序崩溃
        Timer timer = new Timer();
        final int[] temp = {0};
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "temp[0]++:" + temp[0]++);
                wakeUpAndUnlock(mContext);
                SPUtil.putAndApply(MyApp.getmContext(),SpValue.IS_FIRST_TIME,true);
                boolean o = (boolean) SPUtil.get(MyApp.getmContext(), SpValue.IS_FIRST_TIME, false);
                if (o){
                    if (AccessWatcher.getInstence().isAccessibilitySettingsOn(mContext)) {
                        WorkManger.getInstence().doTask(SpValue.STATE_QUERY_FRIENDS_NUM);
                    }
                }
            }
        };
        timer.schedule(task,1000,3600000);
    }

    /**
     * 绑定前台服务
     */
    private void bindNotification() {
        notification = new WindowNotification(mContext);
        Notification show = notification.show();
        startForeground(1, show);
    }

    /**
     * 检查辅助功能是否开启 每分钟检查一次
     */
    private void checkAccessibility() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isScreenOff){
                    wakeUpAndUnlock(mContext);
                }
                if (!AccessWatcher.getInstence().isAccessibilitySettingsOn(mContext)) {
                    EventBus.getDefault().post(new EventbusValueBean("请开启<营客通智能管理>"));
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        }, 0, 60000);
    }

    public static void wakeUpAndUnlock(Context context){
        //屏锁管理器
        KeyguardManager km= (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        //获取电源管理器对象
        PowerManager pm=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,"bright");
        //点亮屏幕
        wl.acquire();
        //释放
        wl.release();
    }

    private boolean isAvilible(Context context,String packageName){
        final PackageManager packageManager = context.getPackageManager(); //获取packagemanager
        List <PackageInfo> pinfo = packageManager.getInstalledPackages(0); //获取所有已安装程序的包信息
        List pName = new ArrayList();//用于存储所有已安装程序的包名
        //从pinfo中将包名字逐一取出，压入pName list中
        if(pinfo != null){
            for(int i = 0; i < pinfo.size(); i++){
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);//判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }
}
