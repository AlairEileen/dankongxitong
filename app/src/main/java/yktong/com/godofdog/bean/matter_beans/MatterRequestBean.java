package yktong.com.godofdog.bean.matter_beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Eileen on 2017/8/31.
 */

public class MatterRequestBean {
    @SerializedName("cLuserid")
    private Integer userId;
    @SerializedName("cLcompanyid")
    private Integer companyId;
    @SerializedName("cContent")
    private String content;
    @SerializedName("cLibrarystortid")
    private Integer librariesTortId;
    @SerializedName("cLibrarystage")
    private Integer librariesStage;
    @SerializedName("cHeadline")
    private String title;
    @SerializedName("cInterlinkage")
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLibrariesTortId() {
        return librariesTortId;
    }

    public void setLibrariesTortId(Integer librariesTortId) {
        this.librariesTortId = librariesTortId;
    }

    public Integer getLibrariesStage() {
        return librariesStage;
    }

    public void setLibrariesStage(Integer librariesStage) {
        this.librariesStage = librariesStage;
    }

}
