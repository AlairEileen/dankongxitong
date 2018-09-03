package yktong.com.godofdog.bean.tel;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by vampire on 2017/8/15.
 */

public class CommunicateData {
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;
    private String communicatename;//姓名
    private String communicatenumber; // 电话号码
    private String communicatetime; // 日期
    private Integer communicatetype; // 来电:1，拨出:2,未接:3
    private Integer communicateduration;
    private Long callDate;

    public Long getCallDate() {
        return callDate;
    }

    public void setCallDate(Long callDate) {
        this.callDate = callDate;
    }

    public CommunicateData() {
    }

    public long getCommunicateduration() {
        return communicateduration;
    }

    public void setCommunicateduration(Integer communicateduration) {
        this.communicateduration = communicateduration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommunicatename() {
        return communicatename;
    }

    public void setCommunicatename(String communicatename) {
        this.communicatename = communicatename;
    }

    public String getCommunicatenumber() {
        return communicatenumber;
    }

    public void setCommunicatenumber(String communicatenumber) {
        this.communicatenumber = communicatenumber;
    }

    public String getCommunicatetime() {
        return communicatetime;
    }

    public void setCommunicatetime(String communicatetime) {
        this.communicatetime = communicatetime;
    }

    public Integer getCommunicatetype() {
        return communicatetype;
    }

    public void setCommunicatetype(Integer communicatetype) {
        this.communicatetype = communicatetype;
    }

}
