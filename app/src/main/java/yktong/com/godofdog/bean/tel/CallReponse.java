package yktong.com.godofdog.bean.tel;

import java.util.ArrayList;

/**
 * Created by vampire on 2017/8/17.
 */

public class CallReponse {
    private ArrayList<CommunicateData> cCommunicateData;

    public CallReponse(ArrayList<CommunicateData> cCommunicateData) {
        this.cCommunicateData = cCommunicateData;
    }

    public ArrayList<CommunicateData> getcCommunicateData() {
        return cCommunicateData;
    }

    public void setcCommunicateData(ArrayList<CommunicateData> cCommunicateData) {
        this.cCommunicateData = cCommunicateData;
    }
}
