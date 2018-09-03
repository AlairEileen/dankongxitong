package yktong.com.godofdog.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by vampire on 2017/7/7.
 */

public class MyOrderBean {


    /**
     * Orders : [{"cAddress":"上海","cAmount":3000,"cCompanyname":"江淮","cCustomername":"小红","cLinktel":"18654254785","cNum":2,"cOrderid":3,"cOrdertime":1499405347000,"cOrderurl":"awdad","cOuserid":1,"cPayment":"转账","cProduct":"云控","cWechat":"18654254785"},{"cAddress":"美国","cAmount":5000,"cCompanyname":"美国通用","cCustomername":"李雷","cLinktel":"18847584568","cNum":1,"cOrderid":10,"cOrdertime":1499405355000,"cOrderurl":"awd","cOuserid":1,"cPayment":"微信","cProduct":"群控","cWechat":"18847584568"},{"cAddress":"美国","cAmount":6000,"cCompanyname":"美国微软","cCustomername":"韩梅梅","cLinktel":"15245785425","cNum":1,"cOrderid":11,"cOrdertime":1499318956000,"cOrderurl":"awd","cOuserid":1,"cPayment":"微信","cProduct":"手控","cWechat":"15245785425"}]
     * code : 1
     */

    private String code;
    private List<OrdersBean> Orders;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<OrdersBean> getOrders() {
        return Orders;
    }

    public void setOrders(List<OrdersBean> Orders) {
        this.Orders = Orders;
    }

    public static class OrdersBean implements Serializable {
        /**
         * cAddress : 上海
         * cAmount : 3000
         * cCompanyname : 江淮
         * cCustomername : 小红
         * cLinktel : 18654254785
         * cNum : 2
         * cOrderid : 3
         * cOrdertime : 1499405347000
         * cOrderurl : awdad
         * cOuserid : 1
         * cPayment : 转账
         * cProduct : 云控
         * cWechat : 18654254785
         */

        private String cAddress;
        private int cAmount;
        private String cCompanyname;
        private String cCustomername;
        private String cLinktel;
        private int cNum;
        private int cOrderid;
        private long cOrdertime;
        private String cOrderurl;
        private int cOuserid;
        private String cPayment;
        private String cProduct;
        private String cWechat;

        public String getCAddress() {
            return cAddress;
        }

        public void setCAddress(String cAddress) {
            this.cAddress = cAddress;
        }

        public int getCAmount() {
            return cAmount;
        }

        public void setCAmount(int cAmount) {
            this.cAmount = cAmount;
        }

        public String getCCompanyname() {
            return cCompanyname;
        }

        public void setCCompanyname(String cCompanyname) {
            this.cCompanyname = cCompanyname;
        }

        public String getCCustomername() {
            return cCustomername;
        }

        public void setCCustomername(String cCustomername) {
            this.cCustomername = cCustomername;
        }

        public String getCLinktel() {
            return cLinktel;
        }

        public void setCLinktel(String cLinktel) {
            this.cLinktel = cLinktel;
        }

        public int getCNum() {
            return cNum;
        }

        public void setCNum(int cNum) {
            this.cNum = cNum;
        }

        public int getCOrderid() {
            return cOrderid;
        }

        public void setCOrderid(int cOrderid) {
            this.cOrderid = cOrderid;
        }

        public long getCOrdertime() {
            return cOrdertime;
        }

        public void setCOrdertime(long cOrdertime) {
            this.cOrdertime = cOrdertime;
        }

        public String getCOrderurl() {
            return cOrderurl;
        }

        public void setCOrderurl(String cOrderurl) {
            this.cOrderurl = cOrderurl;
        }

        public int getCOuserid() {
            return cOuserid;
        }

        public void setCOuserid(int cOuserid) {
            this.cOuserid = cOuserid;
        }

        public String getCPayment() {
            return cPayment;
        }

        public void setCPayment(String cPayment) {
            this.cPayment = cPayment;
        }

        public String getCProduct() {
            return cProduct;
        }

        public void setCProduct(String cProduct) {
            this.cProduct = cProduct;
        }

        public String getCWechat() {
            return cWechat;
        }

        public void setCWechat(String cWechat) {
            this.cWechat = cWechat;
        }

        @Override
        public String toString() {
            return "OrdersBean{" +
                    "cAddress='" + cAddress + '\'' +
                    ", cAmount=" + cAmount +
                    ", cCompanyname='" + cCompanyname + '\'' +
                    ", cCustomername='" + cCustomername + '\'' +
                    ", cLinktel='" + cLinktel + '\'' +
                    ", cNum=" + cNum +
                    ", cOrderid=" + cOrderid +
                    ", cOrdertime=" + cOrdertime +
                    ", cOrderurl='" + cOrderurl + '\'' +
                    ", cOuserid=" + cOuserid +
                    ", cPayment='" + cPayment + '\'' +
                    ", cProduct='" + cProduct + '\'' +
                    ", cWechat='" + cWechat + '\'' +
                    '}';
        }
    }
}
