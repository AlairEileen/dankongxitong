package yktong.com.godofdog.bean.jurisdiction_beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import yktong.com.godofdog.bean.ResponseBaseBean;
import yktong.com.godofdog.bean.user_beans.UserRequestBean;

/**
 * Created by Eileen on 2017/9/7.
 */

public class UserListResponseBean extends ResponseBaseBean {

    @SerializedName("users")
    private List<UserRequestBean> userRequestBeanList;

    public List<UserRequestBean> getUserRequestBeanList() {
        return userRequestBeanList;
    }

    public void setUserRequestBeanList(List<UserRequestBean> userRequestBeanList) {
        this.userRequestBeanList = userRequestBeanList;
    }

    public void doResponse(UserListResponseStatus userListResponseStatus) {
        super.doResponse(userListResponseStatus);
        if (getCode() == UserListResponseStatus.not_admin) {
            userListResponseStatus.notAdmin(UserListResponseStatus.not_admin_text);
        }
    }

    public static interface UserListResponseStatus extends ResponseStatus {
        int not_admin = 4;
        String not_admin_text = "没有权限";

        void notAdmin(String msg);
    }
}
