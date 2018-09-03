package yktong.com.godofdog.bean.xpbean;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

import java.util.List;

/**
 * Created by vampire on 2017/8/25.
 */

public class WxHistoryBean {


    /**
     * cWechatChats : [{"cChatUserid":13,"chatid":2,"content":"过生日","conversationtime":"9223372036854775807","digest":"过生日","digestuser":0,"flag":"72057594037927935","hastrunc":1,"issend":1,"lastseq":"null","msgcount":40,"msgtype":1,"unreadcount":0,"username":"aishiyu123"},{"cChatUserid":13,"chatid":3,"content":"过生日","conversationtime":"1503627811395","digest":"过生日","digestuser":0,"flag":"1503627811395","hastrunc":1,"issend":1,"lastseq":"null","msgcount":0,"msgtype":1,"unreadcount":0,"username":"aishiyu123"},{"cChatUserid":13,"chatid":4,"content":"哦哦哦","conversationtime":"1503627907000","digest":"哦哦哦","digestuser":0,"flag":"1503627907000","hastrunc":1,"issend":0,"lastseq":"654984499","msgcount":41,"msgtype":1,"unreadcount":1,"username":"aishiyu123"},{"cChatUserid":13,"chatid":5,"content":"˚\u2027º·(˚ ˃̣̣̥᷄⌓˂̣̣̥᷅ )\u2027º·˚好热呀 [得意]","conversationtime":"1503627932000","digest":"˚\u2027º·(˚ ˃̣̣̥᷄⌓˂̣̣̥᷅ )\u2027º·˚好热呀 [得意]","digestuser":0,"flag":"1503627932000","hastrunc":1,"issend":0,"lastseq":"654984501","msgcount":42,"msgtype":1,"unreadcount":2,"username":"aishiyu123"}]
     * code : 1
     */

    private String code;
    private List<CWechatChatsBean> cWechatChats;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<CWechatChatsBean> getCWechatChats() {
        return cWechatChats;
    }

    public void setCWechatChats(List<CWechatChatsBean> cWechatChats) {
        this.cWechatChats = cWechatChats;
    }

    public static class CWechatChatsBean {
        @Override
        public String toString() {
            return "CWechatChatsBean{" +
                    "cChatUserid=" + cChatUserid +
                    ", chatid=" + chatid +
                    ", content='" + content + '\'' +
                    ", conversationtime='" + conversationtime + '\'' +
                    ", digest='" + digest + '\'' +
                    ", digestuser=" + digestuser +
                    ", flag='" + flag + '\'' +
                    ", hastrunc=" + hastrunc +
                    ", issend=" + issend +
                    ", lastseq='" + lastseq + '\'' +
                    ", msgcount=" + msgcount +
                    ", msgtype=" + msgtype +
                    ", unreadcount=" + unreadcount +
                    ", username='" + username + '\'' +
                    ", voicename='" + voicename + '\'' +
                    '}';
        }

        /**
         * cChatUserid : 13
         * chatid : 2
         * content : 过生日
         * conversationtime : 9223372036854775807
         * digest : 过生日
         * digestuser : 0
         * flag : 72057594037927935
         * hastrunc : 1
         * issend : 1
         * lastseq : null
         * msgcount : 40
         * msgtype : 1
         * unreadcount : 0
         * username : aishiyu123
         */
        @PrimaryKey(AssignType.AUTO_INCREMENT)
        private int id;
        private int cChatUserid;
        private int chatid;
        private String content;
        private String conversationtime;
        private String digest;
        private int digestuser;
        private String flag;
        private int hastrunc;
        private int issend;
        private String lastseq;
        private int msgcount;
        private int msgtype;
        private int unreadcount;
        @Column("username")
        private String username;
        @Column("voicename")
        private String voicename;
        private String cwcuImage;
        private String cUiname;

        public String getcUiname() {
            return cUiname;
        }

        public void setcUiname(String cUiname) {
            this.cUiname = cUiname;
        }

        public String getCwcuImage() {
            return cwcuImage;
        }

        public void setCwcuImage(String cwcuImage) {
            this.cwcuImage = cwcuImage;
        }

        public int getCChatUserid() {
            return cChatUserid;
        }

        public void setCChatUserid(int cChatUserid) {
            this.cChatUserid = cChatUserid;
        }

        public int getChatid() {
            return chatid;
        }

        public void setChatid(int chatid) {
            this.chatid = chatid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getConversationtime() {
            return conversationtime;
        }

        public void setConversationtime(String conversationtime) {
            this.conversationtime = conversationtime;
        }

        public String getVoicename() {
            return voicename;
        }

        public void setVoicename(String voicename) {
            this.voicename = voicename;
        }

        public String getDigest() {
            return digest;
        }

        public void setDigest(String digest) {
            this.digest = digest;
        }

        public int getDigestuser() {
            return digestuser;
        }

        public void setDigestuser(int digestuser) {
            this.digestuser = digestuser;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public int getHastrunc() {
            return hastrunc;
        }

        public void setHastrunc(int hastrunc) {
            this.hastrunc = hastrunc;
        }

        public int getIssend() {
            return issend;
        }

        public void setIssend(int issend) {
            this.issend = issend;
        }

        public String getLastseq() {
            return lastseq;
        }

        public void setLastseq(String lastseq) {
            this.lastseq = lastseq;
        }

        public int getMsgcount() {
            return msgcount;
        }

        public void setMsgcount(int msgcount) {
            this.msgcount = msgcount;
        }

        public int getMsgtype() {
            return msgtype;
        }

        public void setMsgtype(int msgtype) {
            this.msgtype = msgtype;
        }

        public int getUnreadcount() {
            return unreadcount;
        }

        public void setUnreadcount(int unreadcount) {
            this.unreadcount = unreadcount;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
