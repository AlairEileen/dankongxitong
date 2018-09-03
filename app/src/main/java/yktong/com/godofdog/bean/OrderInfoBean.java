package yktong.com.godofdog.bean;

import java.util.List;

/**
 * Created by vampire on 2017/7/7.
 */

public class OrderInfoBean {


    /**
     * order : {"cOrder":{"cCustomername":"万达","cOrderid":45,"cOrdertime":1499998740000,"cOuserid":8},"cOrderImageUrls":[{"imageUrlone":"http://192.168.1.6:8080/orderpic/品蝶/20170714101900273.png","imageUrlthree":"http://192.168.1.6:8080/orderpic/品蝶/20170714101900993.png","imageUrltwo":"http://192.168.1.6:8080/orderpic/品蝶/20170714101900408.png"},{"imageUrlone":"http://192.168.1.6:8080/orderpic/品蝶/20170714101902404.png","imageUrlthree":"http://192.168.1.6:8080/orderpic/品蝶/20170714101902715.png","imageUrltwo":"http://192.168.1.6:8080/orderpic/品蝶/20170714101902100.png"}]}
     * code : 1
     */

    private OrderBean order;
    private String code;

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class OrderBean {
        /**
         * cOrder : {"cCustomername":"万达","cOrderid":45,"cOrdertime":1499998740000,"cOuserid":8}
         * cOrderImageUrls : [{"imageUrlone":"http://192.168.1.6:8080/orderpic/品蝶/20170714101900273.png","imageUrlthree":"http://192.168.1.6:8080/orderpic/品蝶/20170714101900993.png","imageUrltwo":"http://192.168.1.6:8080/orderpic/品蝶/20170714101900408.png"},{"imageUrlone":"http://192.168.1.6:8080/orderpic/品蝶/20170714101902404.png","imageUrlthree":"http://192.168.1.6:8080/orderpic/品蝶/20170714101902715.png","imageUrltwo":"http://192.168.1.6:8080/orderpic/品蝶/20170714101902100.png"}]
         */

        private COrderBean cOrder;
        private List<COrderImageUrlsBean> cOrderImageUrls;

        public COrderBean getCOrder() {
            return cOrder;
        }

        public void setCOrder(COrderBean cOrder) {
            this.cOrder = cOrder;
        }

        public List<COrderImageUrlsBean> getCOrderImageUrls() {
            return cOrderImageUrls;
        }

        public void setCOrderImageUrls(List<COrderImageUrlsBean> cOrderImageUrls) {
            this.cOrderImageUrls = cOrderImageUrls;
        }

        public static class COrderBean {
            /**
             * cCustomername : 万达
             * cOrderid : 45
             * cOrdertime : 1499998740000
             * cOuserid : 8
             */

            private String cCustomername;
            private int cOrderid;
            private long cOrdertime;
            private int cOuserid;

            public String getCCustomername() {
                return cCustomername;
            }

            public void setCCustomername(String cCustomername) {
                this.cCustomername = cCustomername;
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

            public int getCOuserid() {
                return cOuserid;
            }

            public void setCOuserid(int cOuserid) {
                this.cOuserid = cOuserid;
            }
        }

        public static class COrderImageUrlsBean {
            /**
             * imageUrlone : http://192.168.1.6:8080/orderpic/品蝶/20170714101900273.png
             * imageUrlthree : http://192.168.1.6:8080/orderpic/品蝶/20170714101900993.png
             * imageUrltwo : http://192.168.1.6:8080/orderpic/品蝶/20170714101900408.png
             */

            private String imageUrlone;
            private String imageUrlthree;
            private String imageUrltwo;

            public String getImageUrlone() {
                return imageUrlone;
            }

            public void setImageUrlone(String imageUrlone) {
                this.imageUrlone = imageUrlone;
            }

            public String getImageUrlthree() {
                return imageUrlthree;
            }

            public void setImageUrlthree(String imageUrlthree) {
                this.imageUrlthree = imageUrlthree;
            }

            public String getImageUrltwo() {
                return imageUrltwo;
            }

            public void setImageUrltwo(String imageUrltwo) {
                this.imageUrltwo = imageUrltwo;
            }
        }
    }
}
