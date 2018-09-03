package yktong.com.godofdog.bean.map_beans;

import com.google.gson.annotations.SerializedName;

import yktong.com.godofdog.bean.BaseViewBean;

/**
 * Created by Eileen on 2017/8/28.
 */

public class DeptBean extends BaseViewBean {
    @SerializedName("cDeptid")
    private Integer id;
    @SerializedName("cDcompanyid")
    private Integer companyId;
    @SerializedName("cDeptname")
    private String name;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
