package yktong.com.godofdog.bean;

/**
 * Created by vampire on 2017/7/5.
 */

public class DateListBean {
    private String fileName;
    private String upDateTime;
    private String fileState;
    private String dateNum;
    private boolean state;

    public DateListBean(String fileName, String upDateTime, String fileState, String dateNum) {
        this.fileName = fileName;
        this.upDateTime = upDateTime;
        this.fileState = fileState;
        this.dateNum = dateNum;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUpDateTime() {
        return upDateTime;
    }

    public void setUpDateTime(String upDateTime) {
        this.upDateTime = upDateTime;
    }

    public String getFileState() {
        return fileState;
    }

    public void setFileState(String fileState) {
        this.fileState = fileState;
    }

    public String getDateNum() {
        return dateNum;
    }

    public void setDateNum(String dateNum) {
        this.dateNum = dateNum;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
