package yktong.com.godofdog.tool;

import android.util.Log;

import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.NormalResultBean;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.util.SPUtil;
import yktong.com.godofdog.value.SpValue;
import yktong.com.godofdog.value.UrlValue;

/**
 * Created by vampire on 2017/8/3.
 */

public class OverTask {
    private static OverTask ourInstance;

    public static OverTask getInstance() {
        if (ourInstance == null) {
            synchronized (OverTask.class) {
                if (ourInstance == null) {
                    ourInstance = new OverTask();
                }
            }
        }
        return ourInstance;

    }

    private OverTask() {
    }

    public void finishTask(){
        int taskId = (int) SPUtil.get(MyApp.getmContext(),SpValue.TASK_ID,0);
        Log.d("OverTask", "taskId:" + taskId);
        NetTool.getInstance().startRequest("get", UrlValue.UPDATE_TASK + taskId, NormalResultBean.class, new OnHttpCallBack<NormalResultBean>() {
            @Override
            public void onSuccess(NormalResultBean response) {
                if (response.getCode().equals(1)){
                    Log.d("OverTask", "报告成功");
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

}