package yktong.com.godofdog.bean;

import java.util.List;

/**
 * Created by Eileen on 2017/7/19.
 */

public class MatterResponseBean extends ResponseBaseBean {

    private List<MatterBean> matterBeanList;

    public List<MatterBean> getMatterBeanList() {
        return matterBeanList;
    }

    public void setMatterBeanList(List<MatterBean> matterBeanList) {
        this.matterBeanList = matterBeanList;
    }
}
