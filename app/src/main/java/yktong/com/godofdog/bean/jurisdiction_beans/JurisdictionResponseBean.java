package yktong.com.godofdog.bean.jurisdiction_beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import yktong.com.godofdog.bean.ResponseBaseBean;

/**
 * Created by Eileen on 2017/9/7.
 */

public class JurisdictionResponseBean extends ResponseBaseBean {

    @SerializedName("resource")
    private List<JurisdictionBean> jurisdictionBeanList;

    public List<JurisdictionBean> getJurisdictionBeanList() {
        return jurisdictionBeanList;
    }

    public void setJurisdictionBeanList(List<JurisdictionBean> jurisdictionBeanList) {
        this.jurisdictionBeanList = jurisdictionBeanList;
    }
    public void doResponse(JurisdictionResponseStatus jurisdictionResponseStatus) {
        super.doResponse(jurisdictionResponseStatus);
        if (getCode() == JurisdictionResponseStatus.not_admin) {
            jurisdictionResponseStatus.notAdmin(JurisdictionResponseStatus.not_admin_text);
        }
    }

    public static interface JurisdictionResponseStatus extends ResponseStatus {
        int not_admin = 4;
        String not_admin_text = "没有权限";

        void notAdmin(String msg);
    }
}
