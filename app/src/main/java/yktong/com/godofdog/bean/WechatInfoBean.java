package yktong.com.godofdog.bean;

import java.util.Date;

/**
 * Created by vampire on 2017/7/13.
 */

public class WechatInfoBean {
    private int cWduserid;
    private int cWdtodayquan;
    private int cWdtodaymarket;
    private int cWdcountfriend;
    private int cWdtodayinteract;
    private Date cWdtime;

    public WechatInfoBean() {
    }

    @Override
    public String toString() {
        return "WechatInfoBean{" +
                "cWduserid=" + cWduserid +
                ", cWdtodayquan=" + cWdtodayquan +
                ", cWdtodaymarket=" + cWdtodaymarket +
                ", cWdcountfriend=" + cWdcountfriend +
                ", cWdtodayinteract=" + cWdtodayinteract +
                ", cWdtime=" + cWdtime +
                '}';
    }

    public int getcWduserid() {
        return cWduserid;
    }

    public void setcWduserid(int cWduserid) {
        this.cWduserid = cWduserid;
    }

    public int getcWdtodayquan() {
        return cWdtodayquan;
    }

    public void setcWdtodayquan(int cWdtodayquan) {
        this.cWdtodayquan = cWdtodayquan;
    }

    public int getcWdtodaymarket() {
        return cWdtodaymarket;
    }

    public void setcWdtodaymarket(int cWdtodaymarket) {
        this.cWdtodaymarket = cWdtodaymarket;
    }

    public int getcWdcountfriend() {
        return cWdcountfriend;
    }

    public void setcWdcountfriend(int cWdcountfriend) {
        this.cWdcountfriend = cWdcountfriend;
    }

    public int getcWdtodayinteract() {
        return cWdtodayinteract;
    }

    public void setcWdtodayinteract(int cWdtodayinteract) {
        this.cWdtodayinteract = cWdtodayinteract;
    }

    public Date getcWdtime() {
        return cWdtime;
    }

    public void setcWdtime(Date cWdtime) {
        this.cWdtime = cWdtime;
    }
}
