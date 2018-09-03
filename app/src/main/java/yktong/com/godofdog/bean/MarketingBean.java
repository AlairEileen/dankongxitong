package yktong.com.godofdog.bean;

/**
 * Created by vampire on 2017/7/5.
 */

public class MarketingBean {


    /**
     * code : 1
     * countCWechatData : {"countfriend":10,"countinteract":5,"countmarket":10,"countquan":10,"sum":35}
     */

    private String code;
    private CountCWechatDataBean countCWechatData;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CountCWechatDataBean getCountCWechatData() {
        return countCWechatData;
    }

    public void setCountCWechatData(CountCWechatDataBean countCWechatData) {
        this.countCWechatData = countCWechatData;
    }

    public static class CountCWechatDataBean {
        /**
         * countfriend : 10
         * countinteract : 5
         * countmarket : 10
         * countquan : 10
         * sum : 35
         */

        private int countfriend;
        private int countinteract;
        private int countmarket;
        private int countquan;
        private int sum;

        public int getCountfriend() {
            return countfriend;
        }

        public void setCountfriend(int countfriend) {
            this.countfriend = countfriend;
        }

        public int getCountinteract() {
            return countinteract;
        }

        public void setCountinteract(int countinteract) {
            this.countinteract = countinteract;
        }

        public int getCountmarket() {
            return countmarket;
        }

        public void setCountmarket(int countmarket) {
            this.countmarket = countmarket;
        }

        public int getCountquan() {
            return countquan;
        }

        public void setCountquan(int countquan) {
            this.countquan = countquan;
        }

        public int getSum() {
            return sum;
        }

        public void setSum(int sum) {
            this.sum = sum;
        }

        @Override
        public String toString() {
            return "CountCWechatDataBean{" +
                    "countfriend=" + countfriend +
                    ", countinteract=" + countinteract +
                    ", countmarket=" + countmarket +
                    ", countquan=" + countquan +
                    ", sum=" + sum +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "MarketingBean{" +
                "code='" + code + '\'' +
                ", countCWechatData=" + countCWechatData.toString() +
                '}';
    }
}
