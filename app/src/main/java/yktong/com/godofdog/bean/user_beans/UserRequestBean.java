package yktong.com.godofdog.bean.user_beans;

import com.google.gson.annotations.SerializedName;

import yktong.com.godofdog.bean.BaseViewBean;

/**
 * Created by Eileen on 2017/8/30.
 */

public class UserRequestBean extends BaseViewBean{
    @SerializedName("cId")
    private Integer id;
    @SerializedName("cCompanyid")
    private Integer companyId;
    @SerializedName("cPassword")
    private String password;
    @SerializedName("cLoginname")
    private String phoneNum;
    @SerializedName("cName")
    private String name;
    @SerializedName("code")
    private String verifyCode;
    @SerializedName("code2")
    private String verifyCodeManager;
    @SerializedName("registrationid")
    private String cLogintype;
    @SerializedName("imei")
    private String imei;

    public String getcLogintype() {
        return cLogintype;
    }

    public void setcLogintype(String cLogintype) {
        this.cLogintype = cLogintype;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getVerifyCodeManager() {
        return verifyCodeManager;
    }

    public void setVerifyCodeManager(String verifyCodeManager) {
        this.verifyCodeManager = verifyCodeManager;
    }
}
