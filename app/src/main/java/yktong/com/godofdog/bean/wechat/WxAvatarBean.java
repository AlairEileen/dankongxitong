package yktong.com.godofdog.bean.wechat;

/**
 * Created by vampire on 2017/9/11.
 */

public class WxAvatarBean {
    private String username;
    private String filePath;

    public WxAvatarBean(String username, String filePath) {
        this.username = username;
        this.filePath = filePath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
