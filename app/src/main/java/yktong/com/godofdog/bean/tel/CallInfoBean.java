package yktong.com.godofdog.bean.tel;

import java.util.List;

/**
 * Created by vampire on 2017/8/21.
 */

public class CallInfoBean {


    /**
     * oneCommunicateData : {"communicateList":[{"userCommunicateData":[{"communicateduration":1,"communicatetime":1505286019000,"communicatetype":2,"recordname":"http://192.168.1.6:8080/phone/1/20170913150048663.amr"},{"communicateduration":0,"communicatetime":1505286212000,"communicatetype":2,"recordname":"http://192.168.1.6:8080/phone/1/2017091315010786.amr"},{"communicateduration":2,"communicatetime":1505286224000,"communicatetype":2,"recordname":"http://192.168.1.6:8080/phone/1/20170913150126724.amr"},{"communicateduration":17,"communicatetime":1505289604000,"communicatetype":2,"recordname":""},{"communicateduration":8,"communicatetime":1505289820000,"communicatetype":2,"recordname":""},{"communicateduration":3,"communicatetime":1505290127000,"communicatetype":2,"recordname":"http://192.168.1.6:8080/phone/1/20170913160612506.amr"},{"communicateduration":3,"communicatetime":1505290127000,"communicatetype":2,"recordname":""},{"communicateduration":3,"communicatetime":1505290127000,"communicatetype":2,"recordname":""},{"communicateduration":3,"communicatetime":1505290127000,"communicatetype":2,"recordname":""},{"communicateduration":3,"communicatetime":1505290127000,"communicatetype":2,"recordname":""}]}],"countOneCommunicate":{"communicateduration":43,"communicatename":"18210399566","communicatenumber":"18210399566"}}
     * code : 1
     */

    private OneCommunicateDataBean oneCommunicateData;
    private String code;

    public OneCommunicateDataBean getOneCommunicateData() {
        return oneCommunicateData;
    }

    public void setOneCommunicateData(OneCommunicateDataBean oneCommunicateData) {
        this.oneCommunicateData = oneCommunicateData;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class OneCommunicateDataBean {
        /**
         * communicateList : [{"userCommunicateData":[{"communicateduration":1,"communicatetime":1505286019000,"communicatetype":2,"recordname":"http://192.168.1.6:8080/phone/1/20170913150048663.amr"},{"communicateduration":0,"communicatetime":1505286212000,"communicatetype":2,"recordname":"http://192.168.1.6:8080/phone/1/2017091315010786.amr"},{"communicateduration":2,"communicatetime":1505286224000,"communicatetype":2,"recordname":"http://192.168.1.6:8080/phone/1/20170913150126724.amr"},{"communicateduration":17,"communicatetime":1505289604000,"communicatetype":2,"recordname":""},{"communicateduration":8,"communicatetime":1505289820000,"communicatetype":2,"recordname":""},{"communicateduration":3,"communicatetime":1505290127000,"communicatetype":2,"recordname":"http://192.168.1.6:8080/phone/1/20170913160612506.amr"},{"communicateduration":3,"communicatetime":1505290127000,"communicatetype":2,"recordname":""},{"communicateduration":3,"communicatetime":1505290127000,"communicatetype":2,"recordname":""},{"communicateduration":3,"communicatetime":1505290127000,"communicatetype":2,"recordname":""},{"communicateduration":3,"communicatetime":1505290127000,"communicatetype":2,"recordname":""}]}]
         * countOneCommunicate : {"communicateduration":43,"communicatename":"18210399566","communicatenumber":"18210399566"}
         */

        private CountOneCommunicateBean countOneCommunicate;
        private List<CommunicateListBean> communicateList;

        public CountOneCommunicateBean getCountOneCommunicate() {
            return countOneCommunicate;
        }

        public void setCountOneCommunicate(CountOneCommunicateBean countOneCommunicate) {
            this.countOneCommunicate = countOneCommunicate;
        }

        public List<CommunicateListBean> getCommunicateList() {
            return communicateList;
        }

        public void setCommunicateList(List<CommunicateListBean> communicateList) {
            this.communicateList = communicateList;
        }

        public static class CountOneCommunicateBean {
            /**
             * communicateduration : 43
             * communicatename : 18210399566
             * communicatenumber : 18210399566
             */

            private int communicateduration;
            private String communicatename;
            private String communicatenumber;

            public int getCommunicateduration() {
                return communicateduration;
            }

            public void setCommunicateduration(int communicateduration) {
                this.communicateduration = communicateduration;
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
        }

        public static class CommunicateListBean {
            private List<UserCommunicateDataBean> userCommunicateData;

            public List<UserCommunicateDataBean> getUserCommunicateData() {
                return userCommunicateData;
            }

            public void setUserCommunicateData(List<UserCommunicateDataBean> userCommunicateData) {
                this.userCommunicateData = userCommunicateData;
            }

            public static class UserCommunicateDataBean {
                /**
                 * communicateduration : 1
                 * communicatetime : 1505286019000
                 * communicatetype : 2
                 * recordname : http://192.168.1.6:8080/phone/1/20170913150048663.amr
                 */

                private int communicateduration;
                private long communicatetime;
                private int communicatetype;
                private String recordname;

                public int getCommunicateduration() {
                    return communicateduration;
                }

                public void setCommunicateduration(int communicateduration) {
                    this.communicateduration = communicateduration;
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

                public String getRecordname() {
                    return recordname;
                }

                public void setRecordname(String recordname) {
                    this.recordname = recordname;
                }
            }
        }
    }
}
