package yktong.com.godofdog.bean;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by vampire on 2017/6/16.
 */

public class UserBean {
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;
    private String cLoginname;
    private String cPassword;
    private String cAddress;
    private String cCompanyname;
    private String cDutyname;
    private int cVersion;
    private String cName;
    private int cCompanyid;
    private int cUdeptid;
    private int cUdutyid;
    private int cId;
    public UserBean() {

    }
    public UserBean(String cLoginname, String cPassword, String cName, int cCompanyid, int cUdeptid, int cId) {
        this.cLoginname = cLoginname;
        this.cPassword = cPassword;
        this.cName = cName;
        this.cCompanyid = cCompanyid;
        this.cUdeptid = cUdeptid;
        this.cId = cId;
    }

    public UserBean(int id, String cLoginname, String cPassword, String cName, int cCompanyid, int cUdeptid, int cId) {

        this.id = id;
        this.cLoginname = cLoginname;
        this.cPassword = cPassword;
        this.cName = cName;
        this.cCompanyid = cCompanyid;
        this.cUdeptid = cUdeptid;
        this.cId = cId;
    }

    public String getcDutyname() {
        return cDutyname;
    }

    public void setcDutyname(String cDutyname) {
        this.cDutyname = cDutyname;
    }

    public int getcUdutyid() {
        return cUdutyid;
    }

    public void setcUdutyid(int cUdutyid) {
        this.cUdutyid = cUdutyid;
    }

    public String getcAddress() {
        return cAddress;
    }

    public void setcAddress(String cAddress) {
        this.cAddress = cAddress;
    }

    public String getcCompanyname() {
        return cCompanyname;
    }

    public void setcCompanyname(String cCompanyname) {
        this.cCompanyname = cCompanyname;
    }

    public int getcVersion() {
        return cVersion;
    }

    public void setcVersion(int cVersion) {
        this.cVersion = cVersion;
    }

    public String getcName() {

        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getcLoginname() {
        return cLoginname;
    }

    public void setcLoginname(String cLoginname) {
        this.cLoginname = cLoginname;
    }

    public String getcPassword() {
        return cPassword;
    }

    public void setcPassword(String cPassword) {
        this.cPassword = cPassword;
    }

    public int getcCompanyid() {
        return cCompanyid;
    }

    public void setcCompanyid(int cCompanyid) {
        this.cCompanyid = cCompanyid;
    }

    public int getcUdeptid() {
        return cUdeptid;
    }

    public void setcUdeptid(int cUdeptid) {
        this.cUdeptid = cUdeptid;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

}
