package yktong.com.godofdog.bean.tel;

import java.util.List;

/**
 * Created by vampire on 2017/8/18.
 */

public class TelTotalBean {

    /**
     * userDeptCommunicateData : {"cDept":[{"cDcompanyid":1,"cDeptid":1,"cDeptname":"销售1"},{"cDcompanyid":1,"cDeptid":2,"cDeptname":"销售2"}],"userCommunicateData":[{"cId":8,"cName":"13698568836","countNum":0,"sumCommunicateduration":0},{"cId":15,"cName":"15836896589","countNum":0,"sumCommunicateduration":0},{"cId":9,"cName":"茶π","countNum":0,"sumCommunicateduration":0},{"cId":19,"cName":"张三","countNum":0,"sumCommunicateduration":0},{"cId":10,"cName":"15678336814","countNum":0,"sumCommunicateduration":0},{"cId":21,"cName":"qweasd","countNum":0,"sumCommunicateduration":0},{"cId":11,"cName":"13255856558","countNum":0,"sumCommunicateduration":0},{"cId":12,"cName":"13245546454","countNum":0,"sumCommunicateduration":0},{"cId":14,"cName":"18810360761","countNum":0,"sumCommunicateduration":0}]}
     * code : 1
     */

    private UserDeptCommunicateDataBean userDeptCommunicateData;
    private String code;

    public UserDeptCommunicateDataBean getUserDeptCommunicateData() {
        return userDeptCommunicateData;
    }

    public void setUserDeptCommunicateData(UserDeptCommunicateDataBean userDeptCommunicateData) {
        this.userDeptCommunicateData = userDeptCommunicateData;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class UserDeptCommunicateDataBean {
        private List<CDeptBean> cDept;
        private List<UserCommunicateDataBean> userCommunicateData;

        public List<CDeptBean> getCDept() {
            return cDept;
        }

        public void setCDept(List<CDeptBean> cDept) {
            this.cDept = cDept;
        }

        public List<UserCommunicateDataBean> getUserCommunicateData() {
            return userCommunicateData;
        }

        public void setUserCommunicateData(List<UserCommunicateDataBean> userCommunicateData) {
            this.userCommunicateData = userCommunicateData;
        }

        public static class CDeptBean {
            /**
             * cDcompanyid : 1
             * cDeptid : 1
             * cDeptname : 销售1
             */

            private int cDcompanyid;
            private int cDeptid;
            private String cDeptname;

            public int getCDcompanyid() {
                return cDcompanyid;
            }

            public void setCDcompanyid(int cDcompanyid) {
                this.cDcompanyid = cDcompanyid;
            }

            public int getCDeptid() {
                return cDeptid;
            }

            public void setCDeptid(int cDeptid) {
                this.cDeptid = cDeptid;
            }

            public String getCDeptname() {
                return cDeptname;
            }

            public void setCDeptname(String cDeptname) {
                this.cDeptname = cDeptname;
            }
        }

        public static class UserCommunicateDataBean {
            /**
             * cId : 8
             * cName : 13698568836
             * countNum : 0
             * sumCommunicateduration : 0
             */

            private int cId;
            private String cName;
            private int countNum;
            private int sumCommunicateduration;

            public int getCId() {
                return cId;
            }

            public void setCId(int cId) {
                this.cId = cId;
            }

            public String getCName() {
                return cName;
            }

            public void setCName(String cName) {
                this.cName = cName;
            }

            public int getCountNum() {
                return countNum;
            }

            public void setCountNum(int countNum) {
                this.countNum = countNum;
            }

            public int getSumCommunicateduration() {
                return sumCommunicateduration;
            }

            public void setSumCommunicateduration(int sumCommunicateduration) {
                this.sumCommunicateduration = sumCommunicateduration;
            }
        }
    }
}
