package yktong.com.godofdog.bean;

/**
 * Created by Eileen on 2017/7/17.
 */

public class PhoneNumberFileBean {
    private int cFcomparyid;
    private int cFileid;
    private int countPhone;
    private String cFilename;
    private String cFiletime;
    private String cFiletype;
    private String fileTypeName;
    private boolean selected;
    private boolean isGot;

    public boolean isSelected() {
      if (!isGot)initProperty();
        return selected;
    }

    private void initProperty() {
        int status = Integer.valueOf(cFiletype);
        switch (status) {
            case 0:
                setFileTypeName("未使用");
                setSelected(false);
                break;
            case 1:
                setFileTypeName("使用中");
                setSelected(true);
                break;
            case 2:
                setFileTypeName("已用完");
                setSelected(false);
                break;

        }
        isGot=true;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getFileTypeName() {
        if (!isGot)initProperty();
        return fileTypeName;
    }

    public void setFileTypeName(String fileTypeName) {
        this.fileTypeName = fileTypeName;
    }

    public int getcFcomparyid() {

        return cFcomparyid;
    }

    public void setcFcomparyid(int cFcomparyid) {
        this.cFcomparyid = cFcomparyid;
    }

    public int getcFileid() {
        return cFileid;
    }

    public void setcFileid(int cFileid) {
        this.cFileid = cFileid;
    }

    public int getCountPhone() {
        return countPhone;
    }

    public void setCountPhone(int countPhone) {
        this.countPhone = countPhone;
    }

    public String getcFilename() {
        return cFilename;
    }

    public void setcFilename(String cFilename) {
        this.cFilename = cFilename;
    }

    public String getcFiletime() {
        return cFiletime;
    }

    public void setcFiletime(String cFiletime) {
        this.cFiletime = cFiletime;
    }

    public String getcFiletype() {
        return cFiletype;
    }

    public void setcFiletype(String cFiletype) {
        this.cFiletype = cFiletype;

    }
}
