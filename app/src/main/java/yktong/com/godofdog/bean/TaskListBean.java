package yktong.com.godofdog.bean;

import java.util.List;

/**
 * Created by vampire on 2017/7/21.
 */

public class TaskListBean {


    /**
     * task : [{"cIntervaltime":60,"cNum":4,"cSex":1,"cStarttime":1500537404000,"cTaskid":5,"cTasktype":1,"cTuserid":15},{"cIntervaltime":60,"cNum":4,"cSex":1,"cStarttime":1500537404000,"cTaskid":7,"cTasktype":1,"cTuserid":15},{"cIntervaltime":60,"cNum":4,"cSex":1,"cStarttime":1500537404000,"cTaskid":9,"cTasktype":1,"cTuserid":15},{"cIntervaltime":50,"cNum":6,"cSex":0,"cStarttime":1500602215000,"cTaskid":11,"cTasktype":4,"cTuserid":15},{"cIntervaltime":60,"cNum":4,"cSex":1,"cStarttime":1500537404000,"cTaskid":15,"cTasktype":1,"cTuserid":15},{"cIntervaltime":50,"cNum":4,"cSex":0,"cStarttime":1501037940000,"cTaskid":16,"cTasktype":4,"cTuserid":15}]
     * code : 1
     */

    private String code;
    private List<TaskBean> task;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<TaskBean> getTask() {
        return task;
    }

    public void setTask(List<TaskBean> task) {
        this.task = task;
    }

    public static class TaskBean {
        @Override
        public String toString() {
            return "TaskBean{" +
                    "cIntervaltime=" + cIntervaltime +
                    ", cNum=" + cNum +
                    ", cSex=" + cSex +
                    ", cStarttime=" + cStarttime +
                    ", cTaskid=" + cTaskid +
                    ", cTasktype=" + cTasktype +
                    ", cTuserid=" + cTuserid +
                    ", cWechatqun='" + cWechatqun + '\'' +
                    ", cCheckmage='" + cCheckmage + '\'' +
                    ", cLibraryid=" + cLibraryid +
                    '}';
        }

        /**
         * cIntervaltime : 60
         * cNum : 4
         * cSex : 1
         * cStarttime : 1500537404000
         * cTaskid : 5
         * cTasktype : 1
         * cTuserid : 15
         */

        private int cIntervaltime;
        private int cNum;
        private int cSex;
        private long cStarttime;
        private int cTaskid;
        private int cTasktype;
        private int cTuserid;
        private String cWechatqun;
        private String cCheckmage;
        private int cLibraryid;
        private String cCxcname;

        public String getcCxcname() {
            return cCxcname;
        }

        public void setcCxcname(String cCxcname) {
            this.cCxcname = cCxcname;
        }

        public int getcLibraryid() {
            return cLibraryid;
        }

        public void setcLibraryid(int cLibraryid) {
            this.cLibraryid = cLibraryid;
        }

        public String getcCheckmage() {
            return cCheckmage;
        }

        public void setcCheckmage(String cCheckmage) {
            this.cCheckmage = cCheckmage;
        }

        public String getcWechatqun() {
            return cWechatqun;
        }

        public void setcWechatqun(String cWechatqun) {
            this.cWechatqun = cWechatqun;
        }

        public int getCIntervaltime() {
            return cIntervaltime;
        }

        public void setCIntervaltime(int cIntervaltime) {
            this.cIntervaltime = cIntervaltime;
        }

        public int getCNum() {
            return cNum;
        }

        public void setCNum(int cNum) {
            this.cNum = cNum;
        }

        public int getCSex() {
            return cSex;
        }

        public void setCSex(int cSex) {
            this.cSex = cSex;
        }

        public long getCStarttime() {
            return cStarttime;
        }

        public void setCStarttime(long cStarttime) {
            this.cStarttime = cStarttime;
        }

        public int getCTaskid() {
            return cTaskid;
        }

        public void setCTaskid(int cTaskid) {
            this.cTaskid = cTaskid;
        }

        public int getCTasktype() {
            return cTasktype;
        }

        public void setCTasktype(int cTasktype) {
            this.cTasktype = cTasktype;
        }

        public int getCTuserid() {
            return cTuserid;
        }

        public void setCTuserid(int cTuserid) {
            this.cTuserid = cTuserid;
        }
    }
}
