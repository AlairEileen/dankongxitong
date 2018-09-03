package yktong.com.godofdog.bean;

import java.util.List;

/**
 * Created by vampire on 2017/7/12.
 */

public class QueryGroupBean {


    /**
     * wechatqun : [{"cWqid":146,"cWqname":"贾兴为、淡雅♥时光、丰红娜@盈客通","cWquserid":1},{"cWqid":147,"cWqname":"贾兴为、淡雅♥时光、丰红娜@盈客通","cWquserid":1},{"cWqid":148,"cWqname":"贾兴为、李艳ly、No.1","cWquserid":1},{"cWqid":149,"cWqname":"贾兴为、李艳ly、No.1","cWquserid":1},{"cWqid":150,"cWqname":"Οo邡飛懜葙оΟ、贾兴为、PRIQMKN","cWquserid":1},{"cWqid":151,"cWqname":"Οo邡飛懜葙оΟ、贾兴为、PRIQMKN","cWquserid":1},{"cWqid":152,"cWqname":"贾兴为、FMBDZFJZA、IQSKRZ","cWquserid":1},{"cWqid":153,"cWqname":"贾兴为、FMBDZFJZA、IQSKRZ","cWquserid":1},{"cWqid":154,"cWqname":"animal、A盈客通陈楠、贾兴为","cWquserid":1},{"cWqid":155,"cWqname":"animal、A盈客通陈楠、贾兴为","cWquserid":1},{"cWqid":156,"cWqname":"A盈客通陈楠、贾兴为、淡雅♥时光","cWquserid":1},{"cWqid":157,"cWqname":"A盈客通陈楠、贾兴为、淡雅♥时光","cWquserid":1},{"cWqid":158,"cWqname":"ghhhhhhhhhh他特特特特LOL看看LO\u2026","cWquserid":1},{"cWqid":159,"cWqname":"ghhhhhhhhhh他特特特特LOL看看LO\u2026","cWquserid":1},{"cWqid":160,"cWqname":"贾兴为、丰红娜@盈客通、FMBDZFJ\u2026","cWquserid":1},{"cWqid":161,"cWqname":"贾兴为、丰红娜@盈客通、FMBDZFJ\u2026","cWquserid":1},{"cWqid":162,"cWqname":"hereis","cWquserid":1},{"cWqid":163,"cWqname":"hereis","cWquserid":1}]
     * code : 1
     */

    private String code;
    private List<WechatqunBean> wechatqun;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<WechatqunBean> getWechatqun() {
        return wechatqun;
    }

    public void setWechatqun(List<WechatqunBean> wechatqun) {
        this.wechatqun = wechatqun;
    }

    public static class WechatqunBean {
        /**
         * cWqid : 146
         * cWqname : 贾兴为、淡雅♥时光、丰红娜@盈客通
         * cWquserid : 1
         */

        private int cWqid;
        private String cWqname;
        private int cWquserid;

        public int getCWqid() {
            return cWqid;
        }

        public void setCWqid(int cWqid) {
            this.cWqid = cWqid;
        }

        public String getCWqname() {
            return cWqname;
        }

        public void setCWqname(String cWqname) {
            this.cWqname = cWqname;
        }

        public int getCWquserid() {
            return cWquserid;
        }

        public void setCWquserid(int cWquserid) {
            this.cWquserid = cWquserid;
        }
    }
}
