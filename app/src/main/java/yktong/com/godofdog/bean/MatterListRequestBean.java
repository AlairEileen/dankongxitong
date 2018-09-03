package yktong.com.godofdog.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Eileen on 2017/8/31.
 */

public class MatterListRequestBean {
    @SerializedName("cLcompanyid")
    private Integer companyId;
    @SerializedName("code")
    private Integer orderByCode;
    @SerializedName("cLibrarystortid")
    private Integer librariesTortId;
    @SerializedName("pageIndex")
    private Integer pageIndex;
    @SerializedName("nameContentHeadline")
    private String searchParams;

    public Integer getOrderByCode() {
        return orderByCode;
    }

    public void setOrderByCode(Integer orderByCode) {
        this.orderByCode = orderByCode;
    }

    public String getSearchParams() {
        return searchParams;
    }

    public void setSearchParams(String searchParams) {
        this.searchParams = searchParams;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getLibrariesTortId() {
        return librariesTortId;
    }

    public void setLibrariesTortId(Integer librariesTortId) {
        this.librariesTortId = librariesTortId;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

}
