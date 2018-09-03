package yktong.com.godofdog.bean;

import com.google.gson.annotations.SerializedName;

import static yktong.com.godofdog.bean.CompanyRequestBean.CompanyNameResponseStatus.NO_COMPANY;

/**
 * Created by Eileen on 2017/8/17.
 */

public class CompanyRequestBean extends ResponseBaseBean {
    @SerializedName("companyname")
    private String companyName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void doResponse(CompanyNameResponseStatus companyNameResponseStatus) {
        super.doResponse(companyNameResponseStatus);
        switch (getCode()) {
            case NO_COMPANY:
                companyNameResponseStatus.noCompany(companyNameResponseStatus.NO_COMPANY_TEXT);
                break;
        }

    }
    public interface CompanyNameResponseStatus extends ResponseBaseBean.ResponseStatus {
        int NO_COMPANY = 2;
        String NO_COMPANY_TEXT = "公司不存在";

        void noCompany(String msg);
    }
}
