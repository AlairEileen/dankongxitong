package yktong.com.godofdog.bean.map_beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import yktong.com.godofdog.bean.ResponseBaseBean;

/**
 * Created by Eileen on 2017/8/25.
 */

public class UserLocationResponseBean extends ResponseBaseBean {
    @SerializedName("cLocusfrees")
    private List<UserLocationBean> userLocationBeanList;

    public List<UserLocationBean> getUserLocationBeanList() {
        return userLocationBeanList;
    }

    public void setUserLocationBeanList(List<UserLocationBean> userLocationBeanList) {
        this.userLocationBeanList = userLocationBeanList;
    }
}
