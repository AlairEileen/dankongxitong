package yktong.com.godofdog.bean.tel;

import java.util.List;

/**
 * Created by vampire on 2017/8/18.
 */

public class TelUserCallLogBean {

    /**
     * communicateType : [{"communicatetype":2}]
     * communicateData : [{"addtime":1503034306000,"communicateduration":0,"communicateid":111,"communicatename":"13624159613","communicatenumber":"13624159613","communicatetime":1503072000000,"communicatetype":2,"communicateuserid":13,"phonesymbol":"18071adc0338d0484c1"},{"addtime":1503034306000,"communicateduration":0,"communicateid":112,"communicatename":"13624159613","communicatenumber":"13624159613","communicatetime":1503071973000,"communicatetype":2,"communicateuserid":13,"phonesymbol":"18071adc0338d0484c1"},{"addtime":1503034306000,"communicateduration":0,"communicateid":113,"communicatename":"13624159613","communicatenumber":"13624159613","communicatetime":1503071946000,"communicatetype":2,"communicateuserid":13,"phonesymbol":"18071adc0338d0484c1"},{"addtime":1503034306000,"communicateduration":0,"communicateid":114,"communicatename":"13624159613","communicatenumber":"13624159613","communicatetime":1503025610000,"communicatetype":2,"communicateuserid":13,"phonesymbol":"18071adc0338d0484c1"},{"addtime":1503034306000,"communicateduration":0,"communicateid":115,"communicatename":"动销-13400548889","communicatenumber":"13400548889","communicatetime":1503025098000,"communicatetype":2,"communicateuserid":13,"phonesymbol":"18071adc0338d0484c1"},{"addtime":1503034306000,"communicateduration":0,"communicateid":116,"communicatename":"18210399566","communicatenumber":"18210399566","communicatetime":1502781674000,"communicatetype":2,"communicateuserid":13,"phonesymbol":"18071adc0338d0484c1"},{"addtime":1503034306000,"communicateduration":0,"communicateid":117,"communicatename":"动销-13400550458","communicatenumber":"13400550458","communicatetime":1502780351000,"communicatetype":2,"communicateuserid":13,"phonesymbol":"18071adc0338d0484c1"},{"addtime":1503034306000,"communicateduration":0,"communicateid":118,"communicatename":"13624159613","communicatenumber":"13624159613","communicatetime":1496394814000,"communicatetype":2,"communicateuserid":13,"phonesymbol":"18071adc0338d0484c1"}]
     * code : 1
     */

    private String code;
    private List<CommunicateTypeBean> communicateType;
    private List<CommunicateDataBean> communicateData;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<CommunicateTypeBean> getCommunicateType() {
        return communicateType;
    }

    public void setCommunicateType(List<CommunicateTypeBean> communicateType) {
        this.communicateType = communicateType;
    }

    public List<CommunicateDataBean> getCommunicateData() {
        return communicateData;
    }

    public void setCommunicateData(List<CommunicateDataBean> communicateData) {
        this.communicateData = communicateData;
    }

    public static class CommunicateTypeBean {
        /**
         * communicatetype : 2
         */

        private int communicatetype;

        public int getCommunicatetype() {
            return communicatetype;
        }

        public void setCommunicatetype(int communicatetype) {
            this.communicatetype = communicatetype;
        }
    }

    public static class CommunicateDataBean {
        /**
         * addtime : 1503034306000
         * communicateduration : 0
         * communicateid : 111
         * communicatename : 13624159613
         * communicatenumber : 13624159613
         * communicatetime : 1503072000000
         * communicatetype : 2
         * communicateuserid : 13
         * phonesymbol : 18071adc0338d0484c1
         */

        private long addtime;
        private int communicateduration;
        private int communicateid;
        private String communicatename;
        private String communicatenumber;
        private long communicatetime;
        private int communicatetype;
        private int communicateuserid;
        private String phonesymbol;

        public long getAddtime() {
            return addtime;
        }

        public void setAddtime(long addtime) {
            this.addtime = addtime;
        }

        public int getCommunicateduration() {
            return communicateduration;
        }

        public void setCommunicateduration(int communicateduration) {
            this.communicateduration = communicateduration;
        }

        public int getCommunicateid() {
            return communicateid;
        }

        public void setCommunicateid(int communicateid) {
            this.communicateid = communicateid;
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

        public long getCommunicatetime() {
            return communicatetime;
        }

        public void setCommunicatetime(long communicatetime) {
            this.communicatetime = communicatetime;
        }

        public int getCommunicatetype() {
            return communicatetype;
        }

        public void setCommunicatetype(int communicatetype) {
            this.communicatetype = communicatetype;
        }

        public int getCommunicateuserid() {
            return communicateuserid;
        }

        public void setCommunicateuserid(int communicateuserid) {
            this.communicateuserid = communicateuserid;
        }

        public String getPhonesymbol() {
            return phonesymbol;
        }

        public void setPhonesymbol(String phonesymbol) {
            this.phonesymbol = phonesymbol;
        }
    }
}
