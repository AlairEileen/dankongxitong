package yktong.com.godofdog.bean;

/**
 * Created by Eileen on 2017/9/6.
 */

public abstract class BaseViewBean {
    protected Integer id;
    protected String name;

    public abstract Integer getId();

    public abstract void setId(Integer id);

    public abstract String getName();

    public abstract void setName(String name);

}
