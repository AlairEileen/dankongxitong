package yktong.com.godofdog.util.crash;

import java.io.File;

/**
 * Created by vampire on 2017/7/31.
 */

public interface CrashListener {
    /**
     * 保存异常的日志。
     *
     * @param file
     */
    public void afterSaveCrash(File file);
}
