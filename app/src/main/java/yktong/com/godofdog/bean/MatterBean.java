package yktong.com.godofdog.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Eileen on 2017/7/5.
 */

public class MatterBean {

    private String id;
    private String title;
    private String matterType;
    private List<String> imgList;
    private List<String> imgLargeList;
    private List<String> imgMiddleList;
    private String matter_url_title;
    private String matter_url_icon;
    private String matter_date_time;
    private String matter_use_count;
    private String matter_another;
    private String matter_id;

    public String getMatter_url_link() {
        return matter_url_link;
    }

    public void setMatter_url_link(String matter_url_link) {
        this.matter_url_link = matter_url_link;
    }

    private String matter_url_link;

    public String getMatter_id() {
        return matter_id;
    }

    public void setMatter_id(String matter_id) {
        this.matter_id = matter_id;
    }

    public List<String> getImgLargeList() {
        return imgLargeList;
    }

    public void setImgLargeList(List<String> imgLargeList) {
        this.imgLargeList = imgLargeList;
    }

    public List<String> getImgMiddleList() {
        return imgMiddleList;
    }

    public void setImgMiddleList(List<String> imgMiddleList) {
        this.imgMiddleList = imgMiddleList;
    }


    /**
     * @param title
     * @param matterType
     * @param imgList
     * @param matter_date_time
     * @param matter_use_count
     * @param matter_another
     */
    public MatterBean(String title, String matterType, List<String> imgList, String matter_date_time, String matter_use_count, String matter_another) {
        this.title = title;
        this.matterType = matterType;
        this.imgList = imgList;
        this.matter_date_time = matter_date_time;
        this.matter_use_count = matter_use_count;
        this.matter_another = matter_another;
    }

    public MatterBean() {
    }

    public MatterBean(String id, String title, String matterType, List<String> imgList, String matter_date_time, String matter_use_count, String matter_another) {
        this.id = id;
        this.title = title;
        this.matterType = matterType;
        this.imgList = imgList;
        this.matter_date_time = matter_date_time;
        this.matter_use_count = matter_use_count;
        this.matter_another = matter_another;
    }

    public MatterBean(String id, String title, String matterType, String matter_url_title, String matter_url_icon, String matter_date_time, String matter_use_count, String matter_another) {
        this.id = id;
        this.title = title;
        this.matterType = matterType;
        this.matter_url_title = matter_url_title;
        this.matter_url_icon = matter_url_icon;
        this.matter_date_time = matter_date_time;
        this.matter_use_count = matter_use_count;
        this.matter_another = matter_another;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMatterType() {
        return matterType;
    }

    public void setMatterType(String matterType) {
        this.matterType = matterType;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    public String getMatter_url_title() {
        return matter_url_title;
    }

    public void setMatter_url_title(String matter_url_title) {
        this.matter_url_title = matter_url_title;
    }

    public String getMatter_url_icon() {
        return matter_url_icon;
    }

    public void setMatter_url_icon(String matter_url_icon) {
        this.matter_url_icon = matter_url_icon;
    }

    public String getMatter_date_time() {
        return matter_date_time;
    }

    public void setMatter_date_time(String matter_date_time) {
        this.matter_date_time = matter_date_time;
    }

    public String getMatter_use_count() {
        return matter_use_count;
    }

    public void setMatter_use_count(String matter_use_count) {
        this.matter_use_count = matter_use_count;
    }

    public String getMatter_another() {
        return matter_another;
    }

    public void setMatter_another(String matter_another) {
        this.matter_another = matter_another;
    }

    public String convertToMatterType(int typeId) {
        String type = null;
        switch (typeId) {
            case 1:
                type = "产品";
                break;
            case 2:
                type = "案例";
                break;
            case 3:
                type = "活动";
                break;
            case 4:
                type = "笑话";
                break;
            case 5:
                type = "其他";
                break;
        }
        return type;
    }

    public boolean isLink(int isLinkId) {
        return isLinkId == 2 ? true : false;
    }
}
