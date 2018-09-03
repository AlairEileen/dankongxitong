package yktong.com.godofdog.bean.wechat;

import java.util.List;

/**
 * Created by vampire on 2017/9/12.
 */

public class UserChatBean {


    /**
     * deptchat : {"cdept":[{"cDcompanyid":1,"cDeptid":1,"cDeptname":"销售1"},{"cDcompanyid":1,"cDeptid":2,"cDeptname":"销售2"},{"cDcompanyid":1,"cDeptid":3,"cDeptname":"销售3"},{"cDcompanyid":1,"cDeptid":4,"cDeptname":"非常"},{"cDcompanyid":1,"cDeptid":5,"cDeptname":"胡和大"},{"cDcompanyid":1,"cDeptid":6,"cDeptname":"烦烦烦"},{"cDcompanyid":1,"cDeptid":7,"cDeptname":"v过分刚刚"},{"cDcompanyid":1,"cDeptid":8,"cDeptname":"唱歌不能吧呵呵"},{"cDcompanyid":1,"cDeptid":9,"cDeptname":"根本宝贝vvvv不宝"},{"cDcompanyid":1,"cDeptid":10,"cDeptname":"vv宝贝vvv过分"},{"cDcompanyid":1,"cDeptid":11,"cDeptname":"出差"},{"cDcompanyid":1,"cDeptid":12,"cDeptname":"好吧"},{"cDcompanyid":1,"cDeptid":13,"cDeptname":"哈哈"},{"cDcompanyid":1,"cDeptid":14,"cDeptname":"到底"},{"cDcompanyid":1,"cDeptid":15,"cDeptname":"一样"},{"cDcompanyid":1,"cDeptid":16,"cDeptname":"不好"},{"cDcompanyid":1,"cDeptid":17,"cDeptname":"么么么"},{"cDcompanyid":1,"cDeptid":18,"cDeptname":"那你想你"},{"cDcompanyid":1,"cDeptid":19,"cDeptname":"那你"},{"cDcompanyid":1,"cDeptid":20,"cDeptname":"慢慢看"},{"cDcompanyid":1,"cDeptid":21,"cDeptname":"没看看"},{"cDcompanyid":1,"cDeptid":22,"cDeptname":"姐姐"},{"cDcompanyid":1,"cDeptid":23,"cDeptname":"那你想"},{"cDcompanyid":1,"cDeptid":24,"cDeptname":"那你想"},{"cDcompanyid":1,"cDeptid":25,"cDeptname":"就在"},{"cDcompanyid":1,"cDeptid":26,"cDeptname":"绿"},{"cDcompanyid":1,"cDeptid":27,"cDeptname":"哦咖啡"},{"cDcompanyid":1,"cDeptid":28,"cDeptname":"看看"},{"cDcompanyid":1,"cDeptid":29,"cDeptname":"八宝粥"},{"cDcompanyid":1,"cDeptid":30,"cDeptname":"干活"},{"cDcompanyid":1,"cDeptid":31,"cDeptname":"方法"},{"cDcompanyid":1,"cDeptid":32,"cDeptname":"嘎嘎嘎"},{"cDcompanyid":1,"cDeptid":33,"cDeptname":"贵"},{"cDcompanyid":1,"cDeptid":34,"cDeptname":"%"},{"cDcompanyid":1,"cDeptid":35,"cDeptname":"⭕⭕⭕"},{"cDcompanyid":1,"cDeptid":36,"cDeptname":"闺女"},{"cDcompanyid":1,"cDeptid":37,"cDeptname":"v不会"},{"cDcompanyid":1,"cDeptid":38,"cDeptname":"嘎嘎嘎"},{"cDcompanyid":1,"cDeptid":39,"cDeptname":"🍆🍉🍅💣🔫"}],"userCWechatChat":[{"cId":10,"cName":"🌂🌂🌂🌂🌂","cUiname":"http://192.168.1.6:8080/userpic/1/20170905163419999.jpg","countNum":0},{"cId":13,"cName":"艾诗宇","cUiname":"http://192.168.1.6:8080/userpic/1/20170914110854142.jpg","countNum":2},{"cId":31,"cName":"🐰🐰🐰🐰","cUiname":"http://192.168.1.6:8080/userpic/1/2017091217205489.jpg","countNum":0},{"cId":9,"cName":"茶π","cUiname":"http://192.168.1.6:8080/userpic/1/20170914134417142.jpg","countNum":0},{"cId":8,"cName":"13698568836","cUiname":"http://192.168.1.6:8080/userpic/user.png","countNum":0},{"cId":11,"cName":"13255856558","cUiname":"http://192.168.1.6:8080/userpic/user.png","countNum":0},{"cId":12,"cName":"13245546454","cUiname":"http://192.168.1.6:8080/userpic/user.png","countNum":0},{"cId":14,"cName":"18810360761","cUiname":"http://192.168.1.6:8080/userpic/user.png","countNum":0},{"cId":15,"cName":"15836896589","cUiname":"http://192.168.1.6:8080/userpic/user.png","countNum":0},{"cId":19,"cName":"李四","cUiname":"http://192.168.1.6:8080/userpic/user.png","countNum":0},{"cId":21,"cName":"qweasd","cUiname":"http://192.168.1.6:8080/userpic/user.png","countNum":0},{"cId":22,"cName":"15236099999","cUiname":"http://192.168.1.6:8080/userpic/user.png","countNum":0},{"cId":23,"cName":"15333333333","cUiname":"http://192.168.1.6:8080/userpic/user.png","countNum":0},{"cId":27,"cName":"13611112222","cUiname":"http://192.168.1.6:8080/userpic/user.png","countNum":0},{"cId":28,"cName":"15333333333","cUiname":"http://192.168.1.6:8080/userpic/user.png","countNum":0},{"cId":29,"cName":"15344444444","cUiname":"http://192.168.1.6:8080/userpic/user.png","countNum":0},{"cId":30,"cName":"管理员","cUiname":"http://192.168.1.6:8080/userpic/user.png","countNum":0},{"cId":32,"cName":"🚺🚺🚺🚺","cUiname":"http://192.168.1.6:8080/userpic/user.png","countNum":0}]}
     * code : 1
     */

    private DeptchatBean deptchat;
    private String code;

    public DeptchatBean getDeptchat() {
        return deptchat;
    }

    public void setDeptchat(DeptchatBean deptchat) {
        this.deptchat = deptchat;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class DeptchatBean {
        private List<CdeptBean> cdept;
        private List<UserCWechatChatBean> userCWechatChat;

        public List<CdeptBean> getCdept() {
            return cdept;
        }

        public void setCdept(List<CdeptBean> cdept) {
            this.cdept = cdept;
        }

        public List<UserCWechatChatBean> getUserCWechatChat() {
            return userCWechatChat;
        }

        public void setUserCWechatChat(List<UserCWechatChatBean> userCWechatChat) {
            this.userCWechatChat = userCWechatChat;
        }

        public static class CdeptBean {
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

        public static class UserCWechatChatBean {
            /**
             * cId : 10
             * cName : 🌂🌂🌂🌂🌂
             * cUiname : http://192.168.1.6:8080/userpic/1/20170905163419999.jpg
             * countNum : 0
             */

            private int cId;
            private String cName;
            private String cUiname;
            private int countNum;

            public int getCId() {
                return cId;
            }

            public void setCId(int cId) {
                this.cId = cId;
            }

            public String getCName() {
                return cName;
            }

            public void setCName(String cName) {
                this.cName = cName;
            }

            public String getCUiname() {
                return cUiname;
            }

            public void setCUiname(String cUiname) {
                this.cUiname = cUiname;
            }

            public int getCountNum() {
                return countNum;
            }

            public void setCountNum(int countNum) {
                this.countNum = countNum;
            }
        }
    }
}
