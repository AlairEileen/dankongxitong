package yktong.com.godofdog.bean;

/**
 * Created by Eileen on 2017/8/18.
 */

public class SignOnNextResponse extends ResponseBaseBean {

    public void doResponse(SignOnNextResponseStatus signOnNextResponseStatus) {
        super.doResponse(signOnNextResponseStatus);
        switch (getCode()) {
            case SignOnNextResponseStatus.error_person_verify_code:
                signOnNextResponseStatus.error_person_verify_code(SignOnNextResponseStatus.error_person_verify_code_text);
                break;
            case SignOnNextResponseStatus.timeout_person_verify_code:
                signOnNextResponseStatus.timeout_person_verify_code(SignOnNextResponseStatus.timeout_person_verify_code_text);
                break;
            case SignOnNextResponseStatus.has_person:
                signOnNextResponseStatus.has_person(SignOnNextResponseStatus.has_person_text);
                break;
            case SignOnNextResponseStatus.no_company:
                signOnNextResponseStatus.no_company(SignOnNextResponseStatus.no_company_text);
                break;
            case SignOnNextResponseStatus.error_other:
                signOnNextResponseStatus.error_other(SignOnNextResponseStatus.error_other_text);
                break;
            case SignOnNextResponseStatus.MOST_USER:
                signOnNextResponseStatus.onMostUser(SignOnNextResponseStatus.MOST_USER_TEXT);
                break;
        }

    }

    public interface SignOnNextResponseStatus extends ResponseBaseBean.ResponseStatus {

        int error_person_verify_code = 6;
        String error_person_verify_code_text = "个人验证码错误";

        int timeout_person_verify_code = 14;
        String timeout_person_verify_code_text = "个人验证码超时";

        int has_person = 12;
        String has_person_text = "用户已存在";

        int no_company = 10;
        String no_company_text = "公司不存在";

        int error_other = 5;
        String error_other_text = "未知异常";

        int MOST_USER = 11;
        String MOST_USER_TEXT = "用户名额已满";

        void onMostUser(String msg);

        void error_person_verify_code(String msg);

        void timeout_person_verify_code(String msg);

        void has_person(String msg);

        void no_company(String msg);

        void error_other(String msg);
    }
}
