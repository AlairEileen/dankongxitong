package yktong.com.godofdog.manager;


import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.Date;

import space.eileen.free_util.DialogPluginDownload;
import yktong.com.godofdog.bean.PluginDownloadResponseBean;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OkHttpUtil;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.value.UrlValue;

/**
 * Created by Eileen on 2017/9/19.
 */

public class PluginsManager extends space.eileen.tools.PluginsManager {
    private static final String baseDir = Environment.getExternalStorageDirectory() + "/dx_temp/";

    public static boolean checkAddInstallVoicePlugin(Context context) {
        if (verifyVoicePlugin(context)) {
            return true;
        } else {
            new DialogPluginDownload(context, "成功下载此插件后，才能正常使用语音播放功能。\n是否立即下载？", new DialogPluginDownload.OkCancelDialogListener() {
                @Override
                public void onOkClick() {
                    doDownload(context);
                }

                @Override
                public void onCancelClick() {

                }
            }).show();
            return false;
        }
    }

    private static void doDownload(Context context) {
        NetTool.getInstance().startRequest(OkHttpUtil.GET, UrlValue.DOWNLOAD_VOICE_PLUGIN, PluginDownloadResponseBean.class, new OnHttpCallBack<PluginDownloadResponseBean>() {
            @Override
            public void onSuccess(PluginDownloadResponseBean response) {
                response.doResponse(()->{
                    NetTool.getInstance().downLoadFile(response.getUrl(), baseDir, new OkHttpUtil.ReqProgressCallBack<File>() {
                        @Override
                        public void onProgress(long total, long current) {

                        }

                        @Override
                        public void onSuccess(File response) {
                            install(context, response);
                        }

                        @Override
                        public void onFailed(String msg) {

                        }
                    },new Date().getTime()+"_plugin_voice.apk");
                });

            }

            @Override
            public void onError(Throwable e) {

            }
        });

    }

}
