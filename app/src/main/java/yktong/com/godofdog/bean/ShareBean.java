package yktong.com.godofdog.bean;

import java.util.List;

/**
 * Created by vampire on 2017/8/1.
 */

public class ShareBean {


    /**
     * library_Image : {"clibrary":{"cContent":"算咯红红红","cLcompanyid":3,"cLibraryid":13,"cLibrarystage":1,"cLibrarystortid":1,"cLuserid":25,"cName":"梁家涛","cTime":1501127414000},"clibraryImageUrl":[{"imageUrlone":"http://47.94.42.159:8080/library/3/20170727115014644.jpg","imageUrlthree":"http://47.94.42.159:8080/library/3/20170727115014344.jpg","imageUrltwo":"http://47.94.42.159:8080/library/3/20170727115014259.jpg"}]}
     * code : 1
     */

    private LibraryImageBean library_Image;
    private String code;

    public LibraryImageBean getLibrary_Image() {
        return library_Image;
    }

    public void setLibrary_Image(LibraryImageBean library_Image) {
        this.library_Image = library_Image;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public static class LibraryImageBean {
        /**
         * clibrary : {"cContent":"算咯红红红","cLcompanyid":3,"cLibraryid":13,"cLibrarystage":1,"cLibrarystortid":1,"cLuserid":25,"cName":"梁家涛","cTime":1501127414000}
         * clibraryImageUrl : [{"imageUrlone":"http://47.94.42.159:8080/library/3/20170727115014644.jpg","imageUrlthree":"http://47.94.42.159:8080/library/3/20170727115014344.jpg","imageUrltwo":"http://47.94.42.159:8080/library/3/20170727115014259.jpg"}]
         */

        private ClibraryBean clibrary;
        private List<ClibraryImageUrlBean> clibraryImageUrl;

        public ClibraryBean getClibrary() {
            return clibrary;
        }

        public void setClibrary(ClibraryBean clibrary) {
            this.clibrary = clibrary;
        }

        public List<ClibraryImageUrlBean> getClibraryImageUrl() {
            return clibraryImageUrl;
        }

        public void setClibraryImageUrl(List<ClibraryImageUrlBean> clibraryImageUrl) {
            this.clibraryImageUrl = clibraryImageUrl;
        }

        public static class ClibraryBean {
            /**
             * cContent : 算咯红红红
             * cLcompanyid : 3
             * cLibraryid : 13
             * cLibrarystage : 1
             * cLibrarystortid : 1
             * cLuserid : 25
             * cName : 梁家涛
             * cTime : 1501127414000
             */

            private String cContent;
            private String cHeadline;
            private String cInterlinkage;
            private int cLcompanyid;
            private int cLibraryid;
            private int cLibrarystage;
            private int cLibrarystortid;
            private int cLuserid;
            private String cName;
            private long cTime;

            public String getcHeadline() {
                return cHeadline;
            }

            public void setcHeadline(String cHeadline) {
                this.cHeadline = cHeadline;
            }

            public String getcInterlinkage() {
                return cInterlinkage;
            }

            public void setcInterlinkage(String cInterlinkage) {
                this.cInterlinkage = cInterlinkage;
            }

            public String getCContent() {
                return cContent;
            }

            public void setCContent(String cContent) {
                this.cContent = cContent;
            }

            public int getCLcompanyid() {
                return cLcompanyid;
            }

            public void setCLcompanyid(int cLcompanyid) {
                this.cLcompanyid = cLcompanyid;
            }

            public int getCLibraryid() {
                return cLibraryid;
            }

            public void setCLibraryid(int cLibraryid) {
                this.cLibraryid = cLibraryid;
            }

            public int getCLibrarystage() {
                return cLibrarystage;
            }

            public void setCLibrarystage(int cLibrarystage) {
                this.cLibrarystage = cLibrarystage;
            }

            public int getCLibrarystortid() {
                return cLibrarystortid;
            }

            public void setCLibrarystortid(int cLibrarystortid) {
                this.cLibrarystortid = cLibrarystortid;
            }

            public int getCLuserid() {
                return cLuserid;
            }

            public void setCLuserid(int cLuserid) {
                this.cLuserid = cLuserid;
            }

            public String getCName() {
                return cName;
            }

            public void setCName(String cName) {
                this.cName = cName;
            }

            public long getCTime() {
                return cTime;
            }

            public void setCTime(long cTime) {
                this.cTime = cTime;
            }
        }

        public static class ClibraryImageUrlBean {
            /**
             * imageUrlone : http://47.94.42.159:8080/library/3/20170727115014644.jpg
             * imageUrlthree : http://47.94.42.159:8080/library/3/20170727115014344.jpg
             * imageUrltwo : http://47.94.42.159:8080/library/3/20170727115014259.jpg
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
