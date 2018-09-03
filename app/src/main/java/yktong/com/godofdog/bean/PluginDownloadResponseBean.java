package yktong.com.godofdog.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Eileen on 2017/9/19.
 */

public class PluginDownloadResponseBean extends ResponseBaseBean {
    @SerializedName("plugUrl")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
