package yktong.com.godofdog.bean.map_beans;

import com.google.gson.annotations.SerializedName;

import space.eileen.tools.XString;

/**
 * Created by Eileen on 2017/8/28.
 */

public class UsersLocationBean {
    @SerializedName("cId")
    private int id;
    @SerializedName("cName")
    private String name;
    @SerializedName("cCljourney")
    private double countJourney;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCountJourney() {
        return countJourney;
    }
    public double getCountJourneyKM() {
        return XString.convertToKM(countJourney);
    }

    public void setCountJourney(double countJourney) {
        this.countJourney = countJourney;
    }
}
