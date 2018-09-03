package yktong.com.godofdog.bean;

import java.util.List;

/**
 * Created by vampire on 2017/8/1.
 */

public class FriendNickListBean {

    @Override
    public String toString() {
        return "FriendNickListBean{" +
                "code='" + code + '\'' +
                ", friendname=" + friendname +
                '}';
    }

    /**
     * friendname : ["sdsd"]
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
