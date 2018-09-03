package yktong.com.godofdog.bean.jurisdiction_beans;


import yktong.com.godofdog.bean.ResponseBaseBean;

/**
 * Created by Eileen on 2017/9/12.
 */

public class ResponseUserRoleBean extends ResponseBaseBean {


    public void doResponse(ResponseUserRoleStatus responseUserRoleStatus) {
        super.doResponse(responseUserRoleStatus);
        switch (getCode()) {
            case ResponseUserRoleStatus.not_admin:
                responseUserRoleStatus.not_admin(ResponseUserRoleStatus.not_admin_text);
                break;
            case ResponseUserRoleStatus.cannot_do:
                responseUserRoleStatus.cannot_do(ResponseUserRoleStatus.cannot_do_text);
                break;
            default:
                break;
        }
    }

    public static interface ResponseUserRoleStatus extends ResponseStatus {
        int not_admin = 4;
        String not_admin_text = "没有权限";

        void not_admin(String msg);

        int cannot_do = 25;
        String cannot_do_text = "不能执行";

        void cannot_do(String msg);
    }
}
