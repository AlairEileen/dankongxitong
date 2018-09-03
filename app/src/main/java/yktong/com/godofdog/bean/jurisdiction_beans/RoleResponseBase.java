package yktong.com.godofdog.bean.jurisdiction_beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import yktong.com.godofdog.bean.ResponseBaseBean;

/**
 * Created by Eileen on 2017/9/7.
 */

public class RoleResponseBase extends ResponseBaseBean {
    @SerializedName("roles")
    private List<RoleBean> roleBeanList;

    public List<RoleBean> getRoleBeanList() {
        return roleBeanList;
    }

    public void setRoleBeanList(List<RoleBean> roleBeanList) {
        this.roleBeanList = roleBeanList;
    }
    public void doResponse(RoleResponseStatus roleResponseStatus) {
        super.doResponse(roleResponseStatus);
        if (getCode() == RoleResponseStatus.not_admin) {
            roleResponseStatus.notAdmin(RoleResponseStatus.not_admin_text);
        }
    }

    public static interface RoleResponseStatus extends ResponseStatus {
        int not_admin = 4;
        String not_admin_text = "没有权限";

        void notAdmin(String msg);
    }
}
