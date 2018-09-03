package yktong.com.godofdog.bean.matter_beans;

/**
 * Created by Eileen on 2017/7/19.
 */

public class MatterTextBean {
    private String cContent;
    private String cHeadline;
    private int cLibraryid;
    private int cLibrarystage;
    private int cLibrarystortid;
    private String cName;
    private int cUsetimes;
    private String cInterlinkage;
    private long cTime;

    public int getcUsetimes() {
        return cUsetimes;
    }

    public void setcUsetimes(int cUsetimes) {
        this.cUsetimes = cUsetimes;
    }

    public int getcLibrarystage() {
        return cLibrarystage;
    }

    public void setcLibrarystage(int cLibrarystage) {
        this.cLibrarystage = cLibrarystage;
    }

    public String getcInterlinkage() {
        return cInterlinkage;
    }

    public void setcInterlinkage(String cInterlinkage) {
        this.cInterlinkage = cInterlinkage;
    }

    public String getcHeadline() {
        return cHeadline;
    }

    public void setcHeadline(String cHeadline) {
        this.cHeadline = cHeadline;
    }

    public int getcLibraryid() {
        return cLibraryid;
    }

    public void setcLibraryid(int cLibraryid) {
        this.cLibraryid = cLibraryid;
    }

    public int getcLibrarystortid() {
        return cLibrarystortid;
    }

    public void setcLibrarystortid(int cLibrarystortid) {
        this.cLibrarystortid = cLibrarystortid;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public long getcTime() {
        return cTime;
    }

    public void setcTime(long cTime) {
        this.cTime = cTime;
    }

    public String getcContent() {

        return cContent;
    }

    public void setcContent(String cContent) {
        this.cContent = cContent;
    }
}
