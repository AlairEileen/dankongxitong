package yktong.com.godofdog.bean;

/**
 * Created by vampire on 2017/6/30.
 */

public class ChatHistoryBean {
    String name;
    String lastName;

    public ChatHistoryBean(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "ChatHistoryBean{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
