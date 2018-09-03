package yktong.com.godofdog.bean;

/**
 * Created by Eileen on 2017/7/7.
 */

public class ManagementFragmentBean {
    /**
     * {
     * "COrderWeChatData": {
     * "companyid": 1,
     * "countfriend": 3,
     * "countlibrary": 0,
     * "monthamount": 4,
     * "todayamount": 3,
     * "todayfriend": 1
     * },
     * "code": "1"
     * }
     */

    private String code;
    private COrderWeChatData cOrderWeChatData;

    public COrderWeChatData getcOrderWeChatData() {
        return cOrderWeChatData;
    }

    public void setcOrderWeChatData(COrderWeChatData cOrderWeChatData) {
        this.cOrderWeChatData = cOrderWeChatData;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public class COrderWeChatData {
        private int companyid;
        private int countfriend;
        private int countlibrary;
        private int monthamount;
        private int todayamount;
        private int todayfriend;

        public int getCompanyid() {
            return companyid;
        }

        public void setCompanyid(int companyid) {
            this.companyid = companyid;
        }

        public int getCountfriend() {
            return countfriend;
        }

        public void setCountfriend(int countfriend) {
            this.countfriend = countfriend;
        }

        public int getCountlibrary() {
            return countlibrary;
        }

        public void setCountlibrary(int countlibrary) {
            this.countlibrary = countlibrary;
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

        public int getTodayfriend() {
            return todayfriend;
        }

        public void setTodayfriend(int todayfriend) {
            this.todayfriend = todayfriend;
        }
    }

}
