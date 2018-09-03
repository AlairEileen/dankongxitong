package yktong.com.godofdog.bean;

import java.util.List;

/**
 * Created by vampire on 2017/7/31.
 */

public class ShareNameListBean {


    /**
     * friendname : ["时间的话付款"]
     * code : 1
     */

    private String code;
    private List<String> friendname;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getFriendname() {
        return friendname;
    }

    public void setFriendname(List<String> friendname) {
        this.friendname = friendname;
    }
}
