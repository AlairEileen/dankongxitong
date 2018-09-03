package yktong.com.godofdog.bean;

import java.util.List;

/**
 * Created by vampire on 2017/7/12.
 */

public class PerformanceDateBean {


    /**
     * orderManagerIndex : {"performance":[{"countfriend":300,"monthamount":14000,"todayamount":0,"todaychat":0,"username":"小赵"},{"countfriend":200,"monthamount":1000,"todayamount":0,"todaychat":0,"username":"111"},{"countfriend":88,"monthamount":999,"todayamount":999,"todaychat":75,"username":"的 的"},{"countfriend":77,"monthamount":777,"todayamount":777,"todaychat":0,"username":"的耳朵"},{"countfriend":0,"monthamount":0,"todayamount":0,"todaychat":0}],"sumPerformance":{"sumcountfriend":665,"summonthamount":16776,"sumtodayamount":1776,"sumtodaychat":75},"avgPerformance":{"avgcountfriend":133,"avgmonthamount":3355.2,"avgtodayamount":355.2,"avgtodaychat":15},"dept":[{"cDcompanyid":1,"cDeptid":1,"cDeptname":"销售1"},{"cDcompanyid":1,"cDeptid":2,"cDeptname":"销售2"}]}
     * code : 1
     */

    private OrderManagerIndexBean orderManagerIndex;
    private String code;

    public OrderManagerIndexBean getOrderManagerIndex() {
        return orderManagerIndex;
    }

    public void setOrderManagerIndex(OrderManagerIndexBean orderManagerIndex) {
        this.orderManagerIndex = orderManagerIndex;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class OrderManagerIndexBean {
        /**
         * performance : [{"countfriend":300,"monthamount":14000,"todayamount":0,"todaychat":0,"username":"小赵"},{"countfriend":200,"monthamount":1000,"todayamount":0,"todaychat":0,"username":"111"},{"countfriend":88,"monthamount":999,"todayamount":999,"todaychat":75,"username":"的 的"},{"countfriend":77,"monthamount":777,"todayamount":777,"todaychat":0,"username":"的耳朵"},{"countfriend":0,"monthamount":0,"todayamount":0,"todaychat":0}]
         * sumPerformance : {"sumcountfriend":665,"summonthamount":16776,"sumtodayamount":1776,"sumtodaychat":75}
         * avgPerformance : {"avgcountfriend":133,"avgmonthamount":3355.2,"avgtodayamount":355.2,"avgtodaychat":15}
         * dept : [{"cDcompanyid":1,"cDeptid":1,"cDeptname":"销售1"},{"cDcompanyid":1,"cDeptid":2,"cDeptname":"销售2"}]
         */

        private SumPerformanceBean sumPerformance;
        private AvgPerformanceBean avgPerformance;
        private List<PerformanceBean> performance;
        private List<DeptBean> dept;

        public SumPerformanceBean getSumPerformance() {
            return sumPerformance;
        }

        public void setSumPerformance(SumPerformanceBean sumPerformance) {
            this.sumPerformance = sumPerformance;
        }

        public AvgPerformanceBean getAvgPerformance() {
            return avgPerformance;
        }

        public void setAvgPerformance(AvgPerformanceBean avgPerformance) {
            this.avgPerformance = avgPerformance;
        }

        public List<PerformanceBean> getPerformance() {
            return performance;
        }

        public void setPerformance(List<PerformanceBean> performance) {
            this.performance = performance;
        }

        public List<DeptBean> getDept() {
            return dept;
        }

        public void setDept(List<DeptBean> dept) {
            this.dept = dept;
        }

        public static class SumPerformanceBean {
            /**
             * sumcountfriend : 665
             * summonthamount : 16776
             * sumtodayamount : 1776
             * sumtodaychat : 75
             */

            private int sumcountfriend;
            private int summonthamount;
            private int sumtodayamount;
            private int sumtodaychat;

            public int getSumcountfriend() {
                return sumcountfriend;
            }

            public void setSumcountfriend(int sumcountfriend) {
                this.sumcountfriend = sumcountfriend;
            }

            public int getSummonthamount() {
                return summonthamount;
            }

            public void setSummonthamount(int summonthamount) {
                this.summonthamount = summonthamount;
            }

            public int getSumtodayamount() {
                return sumtodayamount;
            }

            public void setSumtodayamount(int sumtodayamount) {
                this.sumtodayamount = sumtodayamount;
            }

            public int getSumtodaychat() {
                return sumtodaychat;
            }

            public void setSumtodaychat(int sumtodaychat) {
                this.sumtodaychat = sumtodaychat;
            }
        }

        public static class AvgPerformanceBean {
            /**
             * avgcountfriend : 133
             * avgmonthamount : 3355.2
             * avgtodayamount : 355.2
             * avgtodaychat : 15
             */

            private int avgcountfriend;
            private double avgmonthamount;
            private double avgtodayamount;
            private int avgtodaychat;

            public int getAvgcountfriend() {
                return avgcountfriend;
            }

            public void setAvgcountfriend(int avgcountfriend) {
                this.avgcountfriend = avgcountfriend;
            }

            public double getAvgmonthamount() {
                return avgmonthamount;
            }

            public void setAvgmonthamount(double avgmonthamount) {
                this.avgmonthamount = avgmonthamount;
            }

            public double getAvgtodayamount() {
                return avgtodayamount;
            }

            public void setAvgtodayamount(double avgtodayamount) {
                this.avgtodayamount = avgtodayamount;
            }

            public int getAvgtodaychat() {
                return avgtodaychat;
            }

            public void setAvgtodaychat(int avgtodaychat) {
                this.avgtodaychat = avgtodaychat;
            }
        }

        public static class PerformanceBean {
            @Override
            public String toString() {
                return "PerformanceBean{" +
                        "countfriend=" + countfriend +
                        ", monthamount=" + monthamount +
                        ", todayamount=" + todayamount +
                        ", todaychat=" + todaychat +
                        ", username='" + username + '\'' +
                        '}';
            }

            /**
             * countfriend : 300
             * monthamount : 14000
             * todayamount : 0
             * todaychat : 0
             * username : 小赵
             */

            private int countfriend;
            private int monthamount;
            private int todayamount;
            private int todaychat;

            public int getUserid() {
                return userid;
            }

            public void setUserid(int userid) {
                this.userid = userid;
            }

            private int userid;
            private String username;

            public int getCountfriend() {
                return countfriend;
            }

            public void setCountfriend(int countfriend) {
                this.countfriend = countfriend;
            }

            public int getMonthamount() {
                return monthamount;
            }

            public void setMonthamount(int monthamount) {
                this.monthamount = monthamount;
            }

            public int getTodayamount() {
                return todayamount;
            }

            public void setTodayamount(int todayamount) {
                this.todayamount = todayamount;
            }

            public int getTodaychat() {
                return todaychat;
            }

            public void setTodaychat(int todaychat) {
                this.todaychat = todaychat;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }
        }

        public static class DeptBean {
            @Override
            public String toString() {
                return "DeptBean{" +
                        "cDcompanyid=" + cDcompanyid +
                        ", cDeptid=" + cDeptid +
                        ", cDeptname='" + cDeptname + '\'' +
                        '}';
            }

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
