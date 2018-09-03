package yktong.com.godofdog.bean.map_beans;

import com.google.gson.annotations.SerializedName;

import yktong.com.godofdog.bean.ResponseBaseBean;

/**
 * Created by Eileen on 2017/8/28.
 */

public class UsersLocationResponseBean extends ResponseBaseBean {
    @SerializedName("cLocusfreeList")
    private UsersLocationViewBean usersLocationViewBean;

    public UsersLocationViewBean getUsersLocationViewBean() {
        return usersLocationViewBean;
    }

    public void setUsersLocationViewBean(UsersLocationViewBean usersLocationViewBean) {
        this.usersLocationViewBean = usersLocationViewBean;
    }
}
