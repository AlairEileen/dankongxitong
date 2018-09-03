package yktong.com.godofdog.bean.versons;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import yktong.com.godofdog.bean.ResponseBaseBean;

/**
 * Created by Eileen on 2017/8/16.
 */

public class VersionListResponseBean extends ResponseBaseBean {
    @SerializedName("versions")
    private List<AppUpdateInfoBean> appUpdateInfoBeanList;

    public List<AppUpdateInfoBean> getAppUpdateInfoBeanList() {
        return appUpdateInfoBeanList;
    }

    public void setAppUpdateInfoBeanList(List<AppUpdateInfoBean> appUpdateInfoBeanList) {
        this.appUpdateInfoBeanList = appUpdateInfoBeanList;
    }
}
