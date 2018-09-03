package yktong.com.godofdog.bean.jurisdiction_beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Eileen on 2017/9/6.
 */

public class JurisdictionBean {
    @SerializedName("cResourceid")
    private Integer id;
    @SerializedName("cResourcename")
    private String name;
    @SerializedName("selected_status")
    private Integer selectedStatus;

    public Integer getSelectedStatus() {
        return selectedStatus;
    }

    public void setSelectedStatus(Integer selectedStatus) {
        this.selectedStatus = selectedStatus;
    }

    public boolean getSelectedStatusBool() {
        return selectedStatus == 1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
