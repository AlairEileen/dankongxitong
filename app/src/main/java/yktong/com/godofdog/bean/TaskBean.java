package yktong.com.godofdog.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vampire on 2017/7/21.
 */

public class TaskBean {

    /**
     * ctask : {"cStarttime":"2017-7-20 15:56:44","cSex":1,"cNum":4,"cTasktype":1,"cIntervaltime":60}
     * cuser : [{"cTuserid":8},{"cTuserid":9}]
     * cWechatFriend : [{"\u201ccWfid\u201d":1},{"\u201ccWfid\u201d":2}]
     */

    private CtaskBean ctask;
    private List<CuserBean> cuser;
    private List<CWechatFriendBean> cWechatFriend;

    public CtaskBean getCtask() {
        return ctask;
    }

    public void setCtask(CtaskBean ctask) {
        this.ctask = ctask;
    }

    public List<CuserBean> getCuser() {
        return cuser;
    }

    public void setCuser(List<CuserBean> cuser) {
        this.cuser = cuser;
    }

    public List<CWechatFriendBean> getCWechatFriend() {
        return cWechatFriend;
    }

    public void setCWechatFriend(List<CWechatFriendBean> cWechatFriend) {
        this.cWechatFriend = cWechatFriend;
    }

    public static class CtaskBean {
        /**
         * cStarttime : 2017-7-20 15:56:44
         * cSex : 1
         * cNum : 4
         * cTasktype : 1
         * cIntervaltime : 60
         */

        private String cStarttime;
        private int cSex;
        private int cNum;
        private int cTasktype;
        private int cIntervaltime;
        @SerializedName("cWechatqun")
        private String cWchatqun;
        private String cCheckmage;
        private int cInteractive;
        private int cFileid;
        private int cLibraryid;
        private String cCxcname;

        public String getcCxcname() {
            return cCxcname;
        }

        public void setcCxcname(String cCxcname) {
            this.cCxcname = cCxcname;
        }

        public int getcFileid() {
            return cFileid;
        }

        public void setcFileid(int cFileid) {
            this.cFileid = cFileid;
        }

        public int getcLibraryid() {
            return cLibraryid;
        }

        public void setcLibraryid(int cLibraryid) {
            this.cLibraryid = cLibraryid;
        }

        public int getcInteractive() {
            return cInteractive;
        }

        public void setcInteractive(int cInteractive) {
            this.cInteractive = cInteractive;
        }

        public String getcCheckmage() {
            return cCheckmage;
        }

        public void setcCheckmage(String cCheckmage) {
            this.cCheckmage = cCheckmage;
        }

        public void setcIntervaltime(int cIntervaltime) {
            this.cIntervaltime = cIntervaltime;
        }

        public String getcWchatqun() {

            return cWchatqun;
        }

        public void setcWchatqun(String cWchatqun) {
            this.cWchatqun = cWchatqun;
        }

        public String getCStarttime() {
            return cStarttime;
        }

        public void setCStarttime(String cStarttime) {
            this.cStarttime = cStarttime;
        }

        public int getCSex() {
            return cSex;
        }

        public void setCSex(int cSex) {
            this.cSex = cSex;
        }

        public int getCNum() {
            return cNum;
        }

        public void setCNum(int cNum) {
            this.cNum = cNum;
        }

        public int getCTasktype() {
            return cTasktype;
        }

        public void setCTasktype(int cTasktype) {
            this.cTasktype = cTasktype;
        }

    }


    public static class CuserBean {
        /**
         * cTuserid : 8
         */

        private int cId;

        public int getCId() {
            return cId;
        }

        public void setCId(int cId) {
            this.cId = cId;
        }

    }

    public static class CWechatFriendBean {
        @SerializedName("cWfid")
        private int cWfid; // FIXME check this code

        public int getcWfid() {
            return cWfid;
        }

        public void setcWfid(int cWfid) {
            this.cWfid = cWfid;
        }
    }
}
