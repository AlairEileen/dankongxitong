package yktong.com.godofdog.bean;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by vampire on 2017/6/23.
 */

public class GroupBean {
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;
    private String groupName;

    public GroupBean(String groupName) {
        this.groupName = groupName;
    }

    public GroupBean() {
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
