package yktong.com.godofdog.bean.map_beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Eileen on 2017/8/28.
 */

public class UsersLocationViewBean {
    @SerializedName("depts")
    private List<DeptBean> deptBeanList;
    @SerializedName("userCountLocusfree")
    private List<UsersLocationBean> usersLocationBeanList;

    public List<DeptBean> getDeptBeanList() {
        return deptBeanList;
    }

    public void setDeptBeanList(List<DeptBean> deptBeanList) {
        this.deptBeanList = deptBeanList;
    }

    public List<UsersLocationBean> getUsersLocationBeanList() {
        return usersLocationBeanList;
    }

    public void setUsersLocationBeanList(List<UsersLocationBean> usersLocationBeanList) {
        this.usersLocationBeanList = usersLocationBeanList;
    }
}
