package yktong.com.godofdog.bean.map_beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Eileen on 2017/8/24.
 */

public class UserLocationBean {
    @SerializedName("cLocususerid")
    private int userId;
    @SerializedName("cLatitude")
    private double latitude;
    @SerializedName("cLongitude")
    private double longitude;
    @SerializedName("cLocustime")
    private long dateTime;

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
