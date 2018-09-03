package yktong.com.godofdog.bean;

import java.util.ArrayList;

/**
 * Created by vampire on 2017/7/19.
 */

public class FriendBean {


    private ArrayList<CWechatFriend>  cWechatFriend;

    public static class CWechatFriend {

        private String cWffriendname;

        public CWechatFriend(String cWffriendname) {
            this.cWffriendname = cWffriendname;
        }

        public String getNickName() {
            return cWffriendname;
        }

        public void setNickName(String cWffriendname) {
            this.cWffriendname = cWffriendname;
        }
    }

    public FriendBean(ArrayList<CWechatFriend> cWechatFriend) {
        this.cWechatFriend = cWechatFriend;
    }
}
