package yktong.com.godofdog.bean;

/**
 * Created by Eileen on 2017/7/17.
 */

public class UserAvatarBean extends ResponseBaseBean {
    private UserInfoBean cUserImageUrl;

    public UserInfoBean getcUserImageUrl() {
        return cUserImageUrl;
    }

    public void setcUserImageUrl(UserInfoBean cUserImageUrl) {
        this.cUserImageUrl = cUserImageUrl;
    }
}
