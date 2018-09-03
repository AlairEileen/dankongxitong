package yktong.com.godofdog.manager;

import android.content.Intent;

import space.eileen.tools.VersionManager;
import yktong.com.godofdog.activity.MainActivity;
import yktong.com.godofdog.bean.versons.AppUpdateInfoResponseBean;
import yktong.com.godofdog.service.AppUpdateService;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OkHttpUtil;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.value.UrlValue;

/**
 * Created by Eileen on 2017/9/21.
 */

public class AppUpdateManager extends VersionManager {
    private static AppUpdateManager appUpdateManager;
    private AppUpdateManager() {

    }

    public static void doCheck(MainActivity mainActivity) {
        if (appUpdateManager==null)appUpdateManager=new AppUpdateManager();
        appUpdateManager.activity=mainActivity;
        appUpdateManager.verifyVersion();

//        mainActivity. startService(new Intent(mainActivity, AppUpdateService.class));

    }

    @Override
    protected void doDownloadApk(String url, String apkName) {
        Intent intent = new Intent(activity, AppUpdateService.class);
        intent.putExtra("url",url);
        intent.putExtra("apkName",apkName);
        activity. startService(intent);
    }

    @Override
    public int getVersionCode() {
        return super.getVersionCode();
    }

    private void verifyVersion() {
        NetTool.getInstance().startRequest(OkHttpUtil.GET, UrlValue.APP_UPDATE_CHECK + getVersionCode(), AppUpdateInfoResponseBean.class, new OnHttpCallBack<AppUpdateInfoResponseBean>() {
            @Override
            public void onSuccess(AppUpdateInfoResponseBean response) {
                response.doResponse(new AppUpdateInfoResponseBean.AppUpdateResponseStatus() {
                    @Override
                    public void no_update(String msg) {

                    }

                    @Override
                    public void doSuccess() {
                        if (!AppUpdateService.isUpdating)
                        showUpdateMsgView(activity, response.getAppUpdateInfoBean().getcUpdatecontent(), response.getAppUpdateInfoBean().getcVercode(), response.getAppUpdateInfoBean().getcDownloadurl());
                    }
                });
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }
}
