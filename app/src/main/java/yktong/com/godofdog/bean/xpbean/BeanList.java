package yktong.com.godofdog.bean.xpbean;

import java.util.ArrayList;

/**
 * Created by vampire on 2017/8/25.
 */

public class BeanList {
    private ArrayList<CWecatChatJson> cWechatChats;

    public BeanList(ArrayList<CWecatChatJson> cWechatChats) {
        this.cWechatChats = cWechatChats;
    }

    public ArrayList<CWecatChatJson> getcWechatChats() {
        return cWechatChats;
    }

    public void setcWechatChats(ArrayList<CWecatChatJson> cWechatChats) {
        this.cWechatChats = cWechatChats;
    }

}
