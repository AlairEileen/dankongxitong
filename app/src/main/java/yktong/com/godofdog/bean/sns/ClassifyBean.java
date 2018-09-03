package yktong.com.godofdog.bean.sns;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vampire on 2017/7/14.
 */

public class ClassifyBean {


    /**
     * cLibrarySort : [{"cLsort":1,"cLsortname":"产品"},{"cLsort":2,"cLsortname":"案例"},{"cLsort":3,"cLsortname":"活动"},{"cLsort":4,"cLsortname":"笑话"},{"cLsort":5,"cLsortname":"其他"}]
     * code : 1
     */

    private String code;
    private List<CLibraryStortBean> cLibrarySort;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<CLibraryStortBean> getCLibraryStort() {
        return cLibrarySort;
    }

    public List<String> getOptions() {
        List<String> options = new ArrayList<>();
        for (CLibraryStortBean cLibraryStortBean : cLibrarySort) {
            options.add(cLibraryStortBean.cLsortname);
        }
        return options;
    }

    public void setCLibraryStort(List<CLibraryStortBean> cLibraryStort) {
        this.cLibrarySort = cLibraryStort;
    }

    public static class CLibraryStortBean {
        /**
         * cLsort : 1
         * cLsortname : 产品
         */

        private int cLsort;
        private String cLsortname;

        public CLibraryStortBean(int cLsort, String cLsortname) {
            this.cLsort = cLsort;
            this.cLsortname = cLsortname;
        }

        public int getCLsort() {
            return cLsort;
        }

        public void setCLsort(int cLsort) {
            this.cLsort = cLsort;
        }

        public String getCLsortname() {
            return cLsortname;
        }

        public void setCLsortname(String cLsortname) {
            this.cLsortname = cLsortname;
        }
    }
}
