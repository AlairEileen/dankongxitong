package yktong.com.godofdog.bean.matter_beans;

import yktong.com.godofdog.bean.ResponseBaseBean;

/**
 * Created by Eileen on 2017/7/19.
 */

public class MatterPageResponseBean extends ResponseBaseBean {
    public MatterPageBean getLibrarys() {
        return Librarys;
    }

    public void setLibrarys(MatterPageBean librarys) {
        Librarys = librarys;
    }

    private MatterPageBean Librarys;
}
