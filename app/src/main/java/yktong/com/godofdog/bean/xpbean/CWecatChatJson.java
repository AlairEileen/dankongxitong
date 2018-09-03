package yktong.com.godofdog.bean.xpbean;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by vampire on 2017/8/24.
 */

public class CWecatChatJson {
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;
    private String lastSeq;
    private String digest;
    private String digestUser;
    private int status;
    private String username;
    private int msgType;
    private String conversationTime;
    private int msgCount;
    private int hasTrunc;
    private int unReadCount;
    private String content;
    private int isSend;
    private String flag;
    private String nickname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public CWecatChatJson() {
    }

    public String getLastSeq() {
        return lastSeq;
    }

    public void setLastSeq(String lastSeq) {
        this.lastSeq = lastSeq;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getDigestUser() {
        return digestUser;
    }

    public void setDigestUser(String digestUser) {
        this.digestUser = digestUser;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getConversationTime() {
        return conversationTime;
    }

    public void setConversationTime(String conversationTime) {
        this.conversationTime = conversationTime;
    }

    public int getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(int msgCount) {
        this.msgCount = msgCount;
    }

    public int getHasTrunc() {
        return hasTrunc;
    }

    public void setHasTrunc(int hasTrunc) {
        this.hasTrunc = hasTrunc;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsSend() {
        return isSend;
    }

    public void setIsSend(int isSend) {
        this.isSend = isSend;
    }
}
