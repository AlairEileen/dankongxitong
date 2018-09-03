package yktong.com.godofdog.bean;



/**
 * Created by Eileen on 2017/7/10.
 */

public class UserLoginBean extends ResponseBaseBean{
    private UserBean user;

    public UserLoginBean() {

    }

    public UserLoginBean(String code, UserBean user) {

        this.code = code;
        this.user = user;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public void doResponse(LoginResponseStatus loginResponseStatus) {
        super.doResponse(loginResponseStatus);
        switch (getCode()) {
            case LoginResponseStatus.ERROR_USER:
                loginResponseStatus.errorUser(loginResponseStatus.ERROR_USER_TEXT);
                break;
            case LoginResponseStatus.NO_USER:
                loginResponseStatus.noUser(loginResponseStatus.NO_USER_TEXT);
                break;
            case LoginResponseStatus.NO_COMPANY:
                loginResponseStatus.onNoCompany(loginResponseStatus.NO_COMPANY_TEXT);
                break;
        }
    }

    public interface LoginResponseStatus extends ResponseBaseBean.ResponseStatus {
        int ERROR_USER = 9;
        String ERROR_USER_TEXT = "账号或者密码错误";
        int NO_USER = 8;
        String NO_USER_TEXT = "账号不存在";
        int NO_COMPANY = 10;
        String NO_COMPANY_TEXT = "公司不存在";

        void errorUser(String msg);
        void noUser(String msg);

        void onNoCompany(String msg);
    }
}
