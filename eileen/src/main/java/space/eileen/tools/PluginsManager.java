package space.eileen.tools;

import android.content.Context;

import java.io.File;

/**
 * Created by Eileen on 2017/9/19.
 */

public class PluginsManager {

    private final static String voicePlugin = "com.ketian.android.silkv3";

    protected static boolean verifyVoicePlugin(Context context) {
        if (AppTools.isAvilible(context, voicePlugin)) {

            return true;
        } else {
            return false;
        }
    }
    /**
     * 安装apk
     *
     * @param apkFile
     */
    protected static void install(Context context, File apkFile) {
        VersionManager.doInstall(context, apkFile);
    }
}
