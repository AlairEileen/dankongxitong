package yktong.com.godofdog.bean;

/**
 * Created by vampire on 2017/6/30.
 */

public class SnsDailyBean {
    private String nickName ;
    private String content;
    private String time;
    private String describe;

    /**
     *
     * @param nickName
     * @param content
     * @param time
     */
    public SnsDailyBean(String nickName, String content, String time) {
        this.nickName = nickName;
        this.content = content;
        this.time = time;
    }

    public SnsDailyBean() {
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Override
    public String toString() {
        return "SnsDailyBean{" +
                "nickName='" + nickName + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", describe='" + describe + '\'' +
                '}';
    }
}
