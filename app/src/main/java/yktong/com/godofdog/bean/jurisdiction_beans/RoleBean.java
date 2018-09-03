package yktong.com.godofdog.bean.jurisdiction_beans;

import com.google.gson.annotations.SerializedName;

import yktong.com.godofdog.bean.BaseViewBean;

/**
 * Created by Eileen on 2017/9/6.
 */

public class RoleBean extends BaseViewBean {
    @SerializedName("cRoleid")
    private Integer id;
    @SerializedName("cRolename")
    private String name;
    @SerializedName("cRolecompanyid")
    private Integer companyId;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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
}
