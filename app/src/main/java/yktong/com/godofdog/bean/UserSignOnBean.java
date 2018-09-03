package yktong.com.godofdog.bean;

import static yktong.com.godofdog.bean.UserSignOnBean.SignOnResponseStatus.FAILED_VERIFY_CODE;
import static yktong.com.godofdog.bean.UserSignOnBean.SignOnResponseStatus.HAS_USER;
import static yktong.com.godofdog.bean.UserSignOnBean.SignOnResponseStatus.MOST_USER;
import static yktong.com.godofdog.bean.UserSignOnBean.SignOnResponseStatus.NO_COMPANY;
import static yktong.com.godofdog.bean.UserSignOnBean.SignOnResponseStatus.TIMEOUT_VERIFY_CODE;

/**
 * Created by Eileen on 2017/7/10.
 */

public class UserSignOnBean extends ResponseBaseBean {


    private UserBean cUser;

    public UserSignOnBean(String code, UserBean cUser) {
        this.code = code;
        this.cUser = cUser;
    }

    public UserSignOnBean() {

    }

    public UserBean getcUser() {
        return cUser;
    }

    public void setcUser(UserBean cUser) {
        this.cUser = cUser;
    }

    public void doResponse(SignOnResponseStatus signOnResponseStatus) {
        super.doResponse(signOnResponseStatus);
        switch (getCode()) {
            case MOST_USER:
                signOnResponseStatus.onMostUser(signOnResponseStatus.MOST_USER_TEXT);
                break;
            case HAS_USER:
                signOnResponseStatus.onHasUser(signOnResponseStatus.HAS_USER_TEXT);
                break;
            case NO_COMPANY:
                signOnResponseStatus.onNoCompany(signOnResponseStatus.NO_COMPANY_TEXT);
                break;
            case FAILED_VERIFY_CODE:
                signOnResponseStatus.onFailedVerifyCode(signOnResponseStatus.FAILED_VERIFY_CODE_TEXT);
                break;
            case TIMEOUT_VERIFY_CODE:
                signOnResponseStatus.onTimeoutVerifyCode(signOnResponseStatus.TIMEOUT_VERIFY_CODE_TEXT);
                break;
            case SignOnResponseStatus.NO_USER:
                signOnResponseStatus.noUser(signOnResponseStatus.NO_USER_TEXT);
                break;
            case SignOnResponseStatus.error_other:
                signOnResponseStatus.error_other(SignOnResponseStatus.error_other_text);
                break;
            case SignOnResponseStatus.error_manager_verify_code:
                signOnResponseStatus.error_manager_verify_code(SignOnResponseStatus.error_manager_verify_code_text);
                break;
            case SignOnResponseStatus.timeout_manager_verify_code:
                signOnResponseStatus.timeout_manager_verify_code(SignOnResponseStatus.timeout_manager_verify_code_text);
                break;
        }

    }

    public interface SignOnResponseStatus extends ResponseBaseBean.ResponseStatus {
        int MOST_USER = 11;
        String MOST_USER_TEXT = "用户名额已满";

        int HAS_USER = 12;
        String HAS_USER_TEXT = "用户已存在";

        int NO_COMPANY = 10;
        String NO_COMPANY_TEXT = "公司不存在";

        int NO_USER = 8;
        String NO_USER_TEXT = "账号不存在";

        int FAILED_VERIFY_CODE = 6;
        String FAILED_VERIFY_CODE_TEXT = "个人验证码有误";

        int TIMEOUT_VERIFY_CODE = 14;
        String TIMEOUT_VERIFY_CODE_TEXT = "个人验证码超时";


        int error_manager_verify_code = 16;
        String error_manager_verify_code_text = "管理员验证码不正确";

        int timeout_manager_verify_code = 15;
        String timeout_manager_verify_code_text = "管理员验证码超时 ";

        int error_other = 5;
        String error_other_text = "未知异常";

        void onMostUser(String msg);

        void onHasUser(String msg);

        void onNoCompany(String msg);

        void onFailedVerifyCode(String msg);

        void onTimeoutVerifyCode(String msg);

        void noUser(String msg);

        void error_other(String msg);

        void error_manager_verify_code(String msg);

        void timeout_manager_verify_code(String msg);
    }
}
