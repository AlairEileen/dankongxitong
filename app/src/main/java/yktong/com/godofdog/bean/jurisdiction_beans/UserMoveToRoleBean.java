package yktong.com.godofdog.bean.jurisdiction_beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Eileen on 2017/9/8.
 */

public class UserMoveToRoleBean {

    @SerializedName("curUserid")
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
