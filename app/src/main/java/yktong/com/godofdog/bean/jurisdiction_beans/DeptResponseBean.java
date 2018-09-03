package yktong.com.godofdog.bean.jurisdiction_beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import yktong.com.godofdog.bean.ResponseBaseBean;
import yktong.com.godofdog.bean.map_beans.DeptBean;

/**
 * Created by Eileen on 2017/9/7.
 */


public class DeptResponseBean extends ResponseBaseBean {
    @SerializedName("dept")
    private List<DeptBean> deptBeanList;


    public List<DeptBean> getDeptBeanList() {
        return deptBeanList;
    }

    public void setDeptBeanList(List<DeptBean> deptBeanList) {
        this.deptBeanList = deptBeanList;
    }

    public void doResponse(DeptResponseStatus deptResponseStatus) {
        super.doResponse(deptResponseStatus);
        if (getCode() == DeptResponseStatus.not_admin) {
            deptResponseStatus.notAdmin(DeptResponseStatus.not_admin_text);
        }
    }

    public static interface DeptResponseStatus extends ResponseStatus {
        int not_admin = 4;
        String not_admin_text = "没有权限";

        void notAdmin(String msg);
    }
}
