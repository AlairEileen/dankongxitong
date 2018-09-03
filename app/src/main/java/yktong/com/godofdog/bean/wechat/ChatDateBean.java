package yktong.com.godofdog.bean.wechat;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vampire on 2017/9/1.
 */

public class ChatDateBean {

    /**
     * cWechatChats : [{"chatid":162,"content":"wxid_ub7fd6lwr6mv22:1808:0","conversationtime":"1504669978908","cwcuImage":"","digest":"[语音]","msgtype":34,"nickname":"伴我久久","username":"wxid_ys7b27bjha3822","voicename":"http://192.168.1.6:8080/voice/1/20170906115017217.amr"},{"chatid":167,"content":"wxid_ub7fd6lwr6mv22:3425:0","conversationtime":"1504746839649","cwcuImage":"","digest":"[语音]","msgtype":34,"nickname":"珩","username":"aishiyu123","voicename":"http://192.168.1.6:8080/voice/1/2017090709111777.amr"},{"chatid":191,"content":"wxid_7va1ypphkuio11:7420:0","conversationtime":"1504753674847","cwcuImage":"","digest":"[语音]","msgtype":34,"username":"wxid_7va1ypphkuio11","voicename":"http://192.168.1.6:8080/voice/1/20170907110512268.mp3"},{"chatid":202,"content":"哈喽，我是大雄的同事，我叫王润刚，大雄家里有事情，我接他的工作，需要软件联系呀[奸笑][奸笑][奸笑]","conversationtime":"1504833355000","cwcuImage":"","digest":"哈喽，我是大雄的同事，我叫王润刚，大雄家里有事情，我接他的工作，需要软件联系呀[奸笑][奸笑][奸笑]","msgtype":1,"username":"wxid_40spbjuvynea22"},{"chatid":205,"content":"wxid_4fp76q9n4hp322:2899:0","conversationtime":"1504834411000","cwcuImage":"","digest":"[语音]","msgtype":34,"nickname":"伴我久久","username":"wxid_4fp76q9n4hp322","voicename":"http://192.168.1.6:8080/voice/1/20170908093047931.mp3"},{"chatid":206,"content":"wxid_fuonu19s746g22:2167:0","conversationtime":"1504834465000","cwcuImage":"","digest":"[语音]","msgtype":34,"nickname":"animal","username":"wxid_fuonu19s746g22","voicename":"http://192.168.1.6:8080/voice/1/20170908093141968.mp3"},{"chatid":208,"content":"wxid_bdysdgeto3sl22:3507:0","conversationtime":"1504850047906","cwcuImage":"","digest":"[语音]","msgtype":34,"nickname":"animal","username":"wxid_bdysdgeto3sl22","voicename":"http://192.168.1.6:8080/voice/1/20170908135122541.mp3"}]
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
        /**
         * chatid : 162
         * content : wxid_ub7fd6lwr6mv22:1808:0
         * conversationtime : 1504669978908
         * cwcuImage :
         * digest : [语音]
         * msgtype : 34
         * nickname : 伴我久久
         * username : wxid_ys7b27bjha3822
         * voicename : http://192.168.1.6:8080/voice/1/20170906115017217.amr
         */

        private int chatid;
        private String content;
        @SerializedName("convertime")
        private String conversationtime;
        private String cwcuImage;
        private String digest;
        private int msgtype;
        private String nickname;
        private String username;
        private String voicename;
        private int cChatUserid;

        public int getcChatUserid() {
            return cChatUserid;
        }

        public void setcChatUserid(int cChatUserid) {
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

        public String getCwcuImage() {
            return cwcuImage;
        }

        public void setCwcuImage(String cwcuImage) {
            this.cwcuImage = cwcuImage;
        }

        public String getDigest() {
            return digest;
        }

        public void setDigest(String digest) {
            this.digest = digest;
        }

        public int getMsgtype() {
            return msgtype;
        }

        public void setMsgtype(int msgtype) {
            this.msgtype = msgtype;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getVoicename() {
            return voicename;
        }

        public void setVoicename(String voicename) {
            this.voicename = voicename;
        }
    }
}
