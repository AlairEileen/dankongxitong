package yktong.com.godofdog.bean;

import java.util.List;

/**
 * Created by vampire on 2017/7/7.
 */

public class FansManageBean {


    /**
     * code : 1
     * countFans : {"avgFansData":{"avgcountfriend":655,"avgtodaychat":6,"avgtodayfriend":5,"avgtodayquan":14},"sumFansData":{"sumcountfriend":1311,"sumtodaychat":12,"sumtodayfriend":10,"sumtodayquan":29},"selectFansData":[{"countfriend":1111,"todaychat":2,"todayfriend":0,"todayquan":23,"username":"小赵"},{"countfriend":200,"todaychat":10,"todayfriend":10,"todayquan":6,"username":"111"}],"dept":[{"cDcompanyid":1,"cDeptid":1,"cDeptname":"销售1"},{"cDcompanyid":1,"cDeptid":2,"cDeptname":"销售2"}]}
     */

    private String code;
    private CountFansBean countFans;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CountFansBean getCountFans() {
        return countFans;
    }

    public void setCountFans(CountFansBean countFans) {
        this.countFans = countFans;
    }

    public static class CountFansBean {
        /**
         * avgFansData : {"avgcountfriend":655,"avgtodaychat":6,"avgtodayfriend":5,"avgtodayquan":14}
         * sumFansData : {"sumcountfriend":1311,"sumtodaychat":12,"sumtodayfriend":10,"sumtodayquan":29}
         * selectFansData : [{"countfriend":1111,"todaychat":2,"todayfriend":0,"todayquan":23,"username":"小赵"},{"countfriend":200,"todaychat":10,"todayfriend":10,"todayquan":6,"username":"111"}]
         * dept : [{"cDcompanyid":1,"cDeptid":1,"cDeptname":"销售1"},{"cDcompanyid":1,"cDeptid":2,"cDeptname":"销售2"}]
         */

        private AvgFansDataBean avgFansData;
        private SumFansDataBean sumFansData;
        private List<SelectFansDataBean> selectFansData;
        private List<DeptBean> dept;

        public AvgFansDataBean getAvgFansData() {
            return avgFansData;
        }

        public void setAvgFansData(AvgFansDataBean avgFansData) {
            this.avgFansData = avgFansData;
        }

        public SumFansDataBean getSumFansData() {
            return sumFansData;
        }

        public void setSumFansData(SumFansDataBean sumFansData) {
            this.sumFansData = sumFansData;
        }

        public List<SelectFansDataBean> getSelectFansData() {
            return selectFansData;
        }

        public void setSelectFansData(List<SelectFansDataBean> selectFansData) {
            this.selectFansData = selectFansData;
        }

        public List<DeptBean> getDept() {
            return dept;
        }

        public void setDept(List<DeptBean> dept) {
            this.dept = dept;
        }

        public static class AvgFansDataBean {
            /**
             * avgcountfriend : 655
             * avgtodaychat : 6
             * avgtodayfriend : 5
             * avgtodayquan : 14
             */

            private double avgcountfriend;
            private double avgtodaychat;
            private double avgtodayfriend;
            private double avgtodayquan;

            public double getAvgcountfriend() {
                return avgcountfriend;
            }

            public void setAvgcountfriend(int avgcountfriend) {
                this.avgcountfriend = avgcountfriend;
            }

            public double getAvgtodaychat() {
                return avgtodaychat;
            }

            public void setAvgtodaychat(int avgtodaychat) {
                this.avgtodaychat = avgtodaychat;
            }

            public double getAvgtodayfriend() {
                return avgtodayfriend;
            }

            public void setAvgtodayfriend(int avgtodayfriend) {
                this.avgtodayfriend = avgtodayfriend;
            }

            public double getAvgtodayquan() {
                return avgtodayquan;
            }

            public void setAvgtodayquan(int avgtodayquan) {
                this.avgtodayquan = avgtodayquan;
            }
        }

        public static class SumFansDataBean {
            /**
             * sumcountfriend : 1311
             * sumtodaychat : 12
             * sumtodayfriend : 10
             * sumtodayquan : 29
             */

            private int sumcountfriend;
            private int sumtodaychat;
            private int sumtodayfriend;
            private int sumtodayquan;

            public int getSumcountfriend() {
                return sumcountfriend;
            }

            public void setSumcountfriend(int sumcountfriend) {
                this.sumcountfriend = sumcountfriend;
            }

            public int getSumtodaychat() {
                return sumtodaychat;
            }

            public void setSumtodaychat(int sumtodaychat) {
                this.sumtodaychat = sumtodaychat;
            }

            public int getSumtodayfriend() {
                return sumtodayfriend;
            }

            public void setSumtodayfriend(int sumtodayfriend) {
                this.sumtodayfriend = sumtodayfriend;
            }

            public int getSumtodayquan() {
                return sumtodayquan;
            }

            public void setSumtodayquan(int sumtodayquan) {
                this.sumtodayquan = sumtodayquan;
            }
        }

        public static class SelectFansDataBean {
            /**
             * countfriend : 1111
             * todaychat : 2
             * todayfriend : 0
             * todayquan : 23
             * username : 小赵
             */

            private int countfriend;
            private int cId;
            private int todaychat;
            private int todayfriend;
            private int todayquan;
            private String username;

            @Override
            public String toString() {
                return "SelectFansDataBean{" +
                        "countfriend=" + countfriend +
                        ", todaychat=" + todaychat +
                        ", todayfriend=" + todayfriend +
                        ", todayquan=" + todayquan +
                        ", username='" + username + '\'' +
                        '}';
            }

            public int getcId() {
                return cId;
            }

            public void setcId(int cId) {
                this.cId = cId;
            }

            public int getCountfriend() {
                return countfriend;
            }

            public void setCountfriend(int countfriend) {
                this.countfriend = countfriend;
            }

            public int getTodaychat() {
                return todaychat;
            }

            public void setTodaychat(int todaychat) {
                this.todaychat = todaychat;
            }

            public int getTodayfriend() {
                return todayfriend;
            }

            public void setTodayfriend(int todayfriend) {
                this.todayfriend = todayfriend;
            }

            public int getTodayquan() {
                return todayquan;
            }

            public void setTodayquan(int todayquan) {
                this.todayquan = todayquan;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }
        }

        public static class DeptBean {
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
    }
}
