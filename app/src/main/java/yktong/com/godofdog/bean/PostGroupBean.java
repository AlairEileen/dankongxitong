package yktong.com.godofdog.bean;

import java.util.ArrayList;

/**
 * Created by vampire on 2017/7/10.
 */

public class PostGroupBean {
    ArrayList<CWechatQun> list;

    public PostGroupBean(ArrayList<CWechatQun> list) {
        this.list = list;
    }

    public static class CWechatQun {
        private String cWqname;
        private Integer cWquserid;

        public CWechatQun(String cWqname, int cWquserid) {
            this.cWqname = cWqname;
            this.cWquserid = cWquserid;
        }

        public String getcWqname() {
            return cWqname;
        }

        public void setcWqname(String cWqname) {
            this.cWqname = cWqname;
        }

        public Integer getcWquserid() {
            return cWquserid;
        }

        public void setcWquserid(Integer cWquserid) {
            this.cWquserid = cWquserid;
        }
    }
}
