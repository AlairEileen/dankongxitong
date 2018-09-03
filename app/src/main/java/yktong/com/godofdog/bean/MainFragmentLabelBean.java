package yktong.com.godofdog.bean;

/**
 * Created by vampire on 2017/6/22.
 */

public class MainFragmentLabelBean {
    private int id;
    private String title;

    public MainFragmentLabelBean(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
