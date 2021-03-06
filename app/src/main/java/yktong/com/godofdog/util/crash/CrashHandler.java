package yktong.com.godofdog.util.crash;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by vampire on 2017/7/31.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final CrashHandler sHandler = new CrashHandler();
    private static final Thread.UncaughtExceptionHandler sDefaultHandler = Thread
            .getDefaultUncaughtExceptionHandler();
    private static final ExecutorService THREAD_POOL = Executors.newSingleThreadExecutor();
    private Future<?> future;
    private CrashListener mListener;
    private File mLogFile;

    public static CrashHandler getInstance() {
        return sHandler;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        CrashLogUtil.writeLog(mLogFile, "CrashHandler", ex.getMessage(), ex);
        future = THREAD_POOL.submit(new Runnable() {
            public void run() {
                if (mListener != null) {
                    mListener.afterSaveCrash(mLogFile);
                }
            };
        });
        if (!future.isDone()) {
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sDefaultHandler.uncaughtException(thread, ex);
    }

    public void init(File logFile, CrashListener listener) {
        mLogFile = logFile;
        mListener = listener;
    }

}