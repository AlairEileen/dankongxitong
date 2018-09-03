package yktong.com.godofdog.bean;

import java.util.List;

/**
 * Created by vampire on 2017/7/20.
 */

public class UserListBean {


    /**
     * code : 1
     * user : [{"cId":10,"cName":"韩学志"},{"cId":14,"cName":"15236019112"},{"cId":15,"cName":"18210399566"}]
     */

    private String code;
    private List<UserBean> user;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<UserBean> getUser() {
        return user;
    }

    public void setUser(List<UserBean> user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * cId : 10
         * cName : 韩学志
         */

        private int cId;
        private String cName;

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
    }
}
