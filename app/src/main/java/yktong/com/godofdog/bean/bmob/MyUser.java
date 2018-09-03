package yktong.com.godofdog.bean.bmob;

/**
 * Created by vampire on 2017/7/17.
 */

public class MyUser extends cn.bmob.v3.BmobUser {
    private String versionName;
    private int versionCode;
    private String filePath;

    public MyUser() {
    }

    public MyUser(String versionName) {
        this.versionName = versionName;
    }

    public MyUser(String versionName, int versionCode) {
        this.versionName = versionName;
        this.versionCode = versionCode;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public MyUser(String versionName, int versionCode, String filePath) {

        this.versionName = versionName;
        this.versionCode = versionCode;
        this.filePath = filePath;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }
}
