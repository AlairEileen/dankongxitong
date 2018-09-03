package yktong.com.godofdog.bean.market_beans;

import com.google.gson.annotations.SerializedName;

import space.eileen.tools.XString;

/**
 * Created by Eileen on 2017/8/1.
 */

public class MarketRecord {
    @SerializedName("cTaskid")
    private int id;
    @SerializedName("cTasktype")
    private int type;
    @SerializedName("cStarttime")
    private long time;
    @SerializedName("cTaskstage")
    private int status;
    private int cFileid;
    private int cInteractive;
    private int cIntervaltime;
    private int cLibraryid;
    private String cName;
    private String cCheckmage;
    private String cWechatqun;
    private int cNum;
    private int cSex;
    private int cSubmitter;
    private int cTuserid;
    private long cSubmittime;
    private String cCxcname;

    public MarketRecord() {
    }

    public MarketRecord(int type, long time, int status) {
        this.type = type;
        this.time = time;
        this.status = status;
    }

    public String getcCxcname() {
        return cCxcname;
    }

    public void setcCxcname(String cCxcname) {
        this.cCxcname = cCxcname;
    }

    public String getcWechatqun() {
        return cWechatqun;
    }

    public void setcWechatqun(String cWechatqun) {
        this.cWechatqun = cWechatqun;
    }

    public String getcCheckmage() {
        return cCheckmage;
    }

    public void setcCheckmage(String cCheckmage) {
        this.cCheckmage = cCheckmage;
    }

    public int getcFileid() {
        return cFileid;
    }

    public void setcFileid(int cFileid) {
        this.cFileid = cFileid;
    }

    public int getcInteractive() {
        return cInteractive;
    }

    public void setcInteractive(int cInteractive) {
        this.cInteractive = cInteractive;
    }

    public int getcIntervaltime() {
        return cIntervaltime;
    }

    public void setcIntervaltime(int cIntervaltime) {
        this.cIntervaltime = cIntervaltime;
    }

    public int getcLibraryid() {
        return cLibraryid;
    }

    public void setcLibraryid(int cLibraryid) {
        this.cLibraryid = cLibraryid;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public int getcNum() {
        return cNum;
    }

    public void setcNum(int cNum) {
        this.cNum = cNum;
    }

    public int getcSex() {
        return cSex;
    }

    public void setcSex(int cSex) {
        this.cSex = cSex;
    }

    public String getcSexText() {
        switch (getcSex()) {
            case 1:
                return "男";
            case 2:
                return "女";
            case 3:
                return "不限";
            default:
                return "不限";

        }
    }

    public int getcSubmitter() {
        return cSubmitter;
    }

    public void setcSubmitter(int cSubmitter) {
        this.cSubmitter = cSubmitter;
    }

    public int getcTuserid() {
        return cTuserid;
    }

    public void setcTuserid(int cTuserid) {
        this.cTuserid = cTuserid;
    }

    public long getcSubmittime() {
        return cSubmittime;
    }

    public void setcSubmittime(long cSubmittime) {
        this.cSubmittime = cSubmittime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTimeText() {


        return XString.toMiniForLong(time);
    }

    public String getStatusText() {
        switch (status) {
            case 0:
                return "未完成";
            case 1:
                return "完成";
            default:
                return "";
        }
    }

    public String getTypeText() {
        switch (type) {
            case 1:
                return "精准拓客";
            case 2:
                return "社群拓客";
            case 3:
                return "定时营销";
            case 4:
                return "定时互动";
            case 5:
                return "朋友圈";
            case 6:
                return "小程序";
            default:
                return "";
        }
    }

    public String getcSubmittimeText() {
        return XString.toMiniForLong(getcSubmittime());
    }
}
