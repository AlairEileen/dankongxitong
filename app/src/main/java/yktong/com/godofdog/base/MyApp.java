package yktong.com.godofdog.base;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.qihoo.linker.logcollector.BuildConfig;

import cn.bmob.v3.Bmob;
import cn.jpush.android.api.JPushInterface;
import yktong.com.godofdog.BuildConfig;
import yktong.com.godofdog.bean.UserBean;
import yktong.com.godofdog.hook.SettingsHelper;
import yktong.com.godofdog.tool.datebase.DBTool;
import yktong.com.godofdog.util.crash.CrashEmailReport;

/**
 * Created by vampire on 2017/6/13.
 */

public class MyApp extends Application {


    public static Context mContext;
    public static int userId = -1;
    public static int companyDd = 1;
    public static String userPhone;
    public static String userName;
    public static String companyAddress;
    public static String companyName;
    public static String cDutyname;
    public static int version;
    public static SettingsHelper mSettingHelper; // used by xposed

    public static Context getmContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Bmob.initialize(this, "d6dab5d78840e7aa0b0000e05b231eb5");
        UserBean userBean = DBTool.getInstance().queryUser();
        bindPersonInfo(userBean);
        if (!BuildConfig.DEBUG) {
            new CrashEmailReport(mContext).setReceiver("1192236125@qq.com");
        }
//        LogCollector.init(getApplicationContext(), UPLOAD_URL, params);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        JPushInterface.resumePush(this);
        mSettingHelper = new SettingsHelper(mContext, "yktong.com.godofdog");
    }


    public static void bindPersonInfo(UserBean userBean) {
        if (userBean != null) {
            userId = userBean.getcId();
            companyDd = userBean.getcCompanyid();
            userPhone = userBean.getcLoginname();
            userName = userBean.getcName();
            companyAddress = userBean.getcAddress();
            companyName = userBean.getcCompanyname();
            version = userBean.getcVersion();
            cDutyname = userBean.getcDutyname();
        }
    }

     /*获取本地软件版本号
     */

    public static int getLocalVersion(Context ctx) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext().getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
            Log.d("MyApp", "本软件的版本号。。" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * 获取本地软件版本号名称
     */
    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
            Log.d("MyApp", "本软件的版本号。。" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }
}
