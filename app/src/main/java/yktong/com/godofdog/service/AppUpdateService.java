package yktong.com.godofdog.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;

import space.eileen.tools.AppUpdateNotification;
import space.eileen.tools.VersionManager;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OkHttpUtil;

public class AppUpdateService extends Service {
    public AppUpdateService() {
    }

    public static boolean isUpdating;

    private AppUpdateNotification<AppUpdateService> appUpdateNotification;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("url");
        String apkName = intent.getStringExtra("apkName");
        if (!isUpdating) {
            autoUpdate(url, apkName);
            isUpdating = true;
        }
        return super.onStartCommand(intent, flags, startId);

    }


    private void autoUpdate(String url, String apkName) {

        if (appUpdateNotification == null)
            appUpdateNotification = new AppUpdateNotification<>(this, AppUpdateService.class);
        int notificationId = 100;
        appUpdateNotification.showNotification(notificationId);
        NetTool.getInstance().downLoadFile(url, Environment.getExternalStorageDirectory() + "/dx_temp/", new OkHttpUtil.ReqProgressCallBack<File>() {

            @Override
            public void onProgress(long total, long current) {
                int progress = (int) (((float) current / total) * 100);
                Log.d("#####tocal:", "total:" + total + ",current:" + current + ",progress:" + progress);
                appUpdateNotification.updateProgress(notificationId, progress);
                if (progress == 100) {
                    appUpdateNotification.cancel(notificationId);
                }
            }

            @Override
            public void onSuccess(File response) {
                VersionManager.doInstall(AppUpdateService.this, response);
            }

            @Override
            public void onFailed(String msg) {

            }
        }, apkName);


    }
}
