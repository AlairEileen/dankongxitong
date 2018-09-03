package yktong.com.godofdog.bean.versons;

import space.eileen.tools.XString;

/**
 * Created by Eileen on 2017/8/8.
 */

public class AppUpdateInfoBean {

    private String cUpdatecontent;
    private String cDownloadurl;
    private long cUpdatetime;
    private String cVername;
    private int cVercode;
    private int cVid;

    public int getcVid() {
        return cVid;
    }

    public void setcVid(int cVid) {
        this.cVid = cVid;
    }

    public String getcUpdatecontent() {
        return cUpdatecontent;
    }

    public void setcUpdatecontent(String cUpdatecontent) {
        this.cUpdatecontent = cUpdatecontent;
    }

    public String getcDownloadurl() {
        return cDownloadurl;
    }

    public void setcDownloadurl(String cDownloadurl) {
        this.cDownloadurl = cDownloadurl;
    }

    public long getcUpdatetime() {
        return cUpdatetime;
    }
    public String getcUpdatetimeText() {
        return XString.toDateForLong(getcUpdatetime());
    }
    public void setcUpdatetime(long cUpdatetime) {
        this.cUpdatetime = cUpdatetime;
    }

    public String getcVername() {
        return cVername;
    }

    public void setcVername(String cVername) {
        this.cVername = cVername;
    }

    public int getcVercode() {
        return cVercode;
    }

    public void setcVercode(int cVercode) {
        this.cVercode = cVercode;
    }
}
