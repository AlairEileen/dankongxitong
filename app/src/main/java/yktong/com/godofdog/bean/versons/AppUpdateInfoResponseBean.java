package yktong.com.godofdog.bean.versons;

import com.google.gson.annotations.SerializedName;

import yktong.com.godofdog.bean.ResponseBaseBean;

import static yktong.com.godofdog.bean.versons.AppUpdateInfoResponseBean.AppUpdateResponseStatus.no_update;
import static yktong.com.godofdog.bean.versons.AppUpdateInfoResponseBean.AppUpdateResponseStatus.no_update_text;

/**
 * Created by Eileen on 2017/8/8.
 */

public class AppUpdateInfoResponseBean extends ResponseBaseBean {
    @SerializedName("cVersion")
    private AppUpdateInfoBean appUpdateInfoBean;

    public AppUpdateInfoBean getAppUpdateInfoBean() {
        return appUpdateInfoBean;
    }

    public void setAppUpdateInfoBean(AppUpdateInfoBean appUpdateInfoBean) {
        this.appUpdateInfoBean = appUpdateInfoBean;
    }

    public void doResponse(AppUpdateResponseStatus appUpdateResponseStatus) {
        super.doResponse(appUpdateResponseStatus);
        switch (getCode()) {
            case no_update:
                appUpdateResponseStatus.no_update(no_update_text);
                break;
        }
    }

    public static interface AppUpdateResponseStatus extends ResponseStatus {
        int no_update = 20;
        String no_update_text = "没有更新";

        void no_update(String msg);
    }
}
