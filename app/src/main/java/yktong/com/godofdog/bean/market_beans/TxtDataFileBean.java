package yktong.com.godofdog.bean.market_beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Eileen on 2017/8/2.
 */

public class TxtDataFileBean {
    @SerializedName("cFilename")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
