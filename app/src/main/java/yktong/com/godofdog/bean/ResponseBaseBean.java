package yktong.com.godofdog.bean;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Eileen on 2017/7/11.
 */

public class ResponseBaseBean {
    protected String code;

    public int getCode() {
        return Integer.valueOf(code);
    }

    public void setCode(String code) {
        this.code = code;

    }

    public <T extends ResponseBaseBean.ResponseStatus> void doResponse(T t) {
        if (getCode() == ResponseBaseBean.ResponseStatus.SUCCESS)
            t.doSuccess();
    }

    public void showErrorInfo(Activity activity, String msg) {
        activity.runOnUiThread(() -> Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show());
    }

    public static interface ResponseStatus {
        int SUCCESS = 1;

        void doSuccess();
    }
}
