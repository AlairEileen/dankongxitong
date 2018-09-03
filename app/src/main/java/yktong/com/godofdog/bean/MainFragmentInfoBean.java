package yktong.com.godofdog.bean;

/**
 * Created by vampire on 2017/7/11.
 */

public class MainFragmentInfoBean {

    /**
     * indexData : {"countfriend":26,"monthamount":1443,"monthfriend":2,"todaychat":33,"todayfriend":2,"todayquan":22}
     * code : 1
     */

    private IndexDataBean indexData;
    private String code;

    public IndexDataBean getIndexData() {
        return indexData;
    }

    public void setIndexData(IndexDataBean indexData) {
        this.indexData = indexData;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class IndexDataBean {
        /**
         * countfriend : 26
         * monthamount : 1443
         * monthfriend : 2
         * todaychat : 33
         * todayfriend : 2
         * todayquan : 22
         */

        private int countfriend;
        private int monthamount;
        private int monthfriend;
        private int todaychat;
        private int todayfriend;
        private int todayquan;

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

        public int getMonthfriend() {
            return monthfriend;
        }

        public void setMonthfriend(int monthfriend) {
            this.monthfriend = monthfriend;
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
    }
}
