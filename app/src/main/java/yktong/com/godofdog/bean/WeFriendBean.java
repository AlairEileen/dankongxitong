package yktong.com.godofdog.bean;

import java.util.List;

/**
 * Created by vampire on 2017/7/19.
 */

public class WeFriendBean {


    /**
     * cWechatFriend : [{"cWffriendname":"Bottom","cWfid":244,"cWfuserid":13},{"cWffriendname":"陈焕","cWfid":245,"cWfuserid":13},{"cWffriendname":"crazy","cWfid":246,"cWfuserid":13},{"cWffriendname":"丶壞人張","cWfid":247,"cWfuserid":13},{"cWffriendname":"Er NING* ","cWfid":248,"cWfuserid":13},{"cWffriendname":"fans-18932996282 磊","cWfid":249,"cWfuserid":13},{"cWffriendname":"fans-18932996285 一代天骄","cWfid":250,"cWfuserid":13},{"cWffriendname":"fans-18932997673","cWfid":251,"cWfuserid":13},{"cWffriendname":"fans-18932997675","cWfid":252,"cWfuserid":13},{"cWffriendname":"fans-18932997680","cWfid":253,"cWfuserid":13},{"cWffriendname":"fans-18932997682","cWfid":254,"cWfuserid":13},{"cWffriendname":"fans-18932997683","cWfid":255,"cWfuserid":13},{"cWffriendname":"fans-18932999 fans-18932999127 fans-18932999128 LL","cWfid":256,"cWfuserid":13},{"cWffriendname":"郭洪涛","cWfid":257,"cWfuserid":13},{"cWffriendname":"李璐璐","cWfid":258,"cWfuserid":13},{"cWffriendname":"蒙奇D路费","cWfid":259,"cWfuserid":13},{"cWffriendname":"null","cWfid":260,"cWfuserid":13},{"cWffriendname":"Ssan","cWfid":261,"cWfuserid":13},{"cWffriendname":"涛涛de洪水","cWfid":262,"cWfuserid":13},{"cWffriendname":"Top","cWfid":263,"cWfuserid":13},{"cWffriendname":"王子睿","cWfid":264,"cWfuserid":13},{"cWffriendname":"微信团队","cWfid":265,"cWfuserid":13},{"cWffriendname":"我是聪","cWfid":266,"cWfuserid":13},{"cWffriendname":"小敏","cWfid":267,"cWfuserid":13},{"cWffriendname":"绚丽停机了","cWfid":268,"cWfuserid":13}]
     * code : 1
     */

    private String code;
    private List<CWechatFriendBean> cWechatFriend;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<CWechatFriendBean> getCWechatFriend() {
        return cWechatFriend;
    }

    public void setCWechatFriend(List<CWechatFriendBean> cWechatFriend) {
        this.cWechatFriend = cWechatFriend;
    }

    public static class CWechatFriendBean {
        /**
         * cWffriendname : Bottom
         * cWfid : 244
         * cWfuserid : 13
         */

        private String cWffriendname;
        private int cWfid;
        private int cWfuserid;

        public String getCWffriendname() {
            return cWffriendname;
        }

        public void setCWffriendname(String cWffriendname) {
            this.cWffriendname = cWffriendname;
        }

        public int getCWfid() {
            return cWfid;
        }

        public void setCWfid(int cWfid) {
            this.cWfid = cWfid;
        }

        public int getCWfuserid() {
            return cWfuserid;
        }

        public void setCWfuserid(int cWfuserid) {
            this.cWfuserid = cWfuserid;
        }
    }
}
