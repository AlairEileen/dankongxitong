package yktong.com.godofdog.bean;

import java.util.List;

/**
 * Created by vampire on 2017/7/25.
 */

public class NumberBean {


    /**
     * phone : ["13400568325","13400634293","13400632236","13400630665","13400629422"]
     * code : 1
     */

    private String code;
    private List<String> phone;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getPhone() {
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }
}
