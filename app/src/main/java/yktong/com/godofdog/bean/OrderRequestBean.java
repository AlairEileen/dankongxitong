package yktong.com.godofdog.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Eileen on 2017/8/31.
 */

public class OrderRequestBean {
    @SerializedName("cOuserid")
    private Integer userId;
    @SerializedName("cCustomername")
    private String customerName;
    @SerializedName("cCompanyname")
    private String companyName;
    @SerializedName("cLinktel")
    private String linkTel;
    @SerializedName("cAddress")
    private String address;
    @SerializedName("cProduct")
    private String product;
    @SerializedName("cNum")
    private Integer num;
    @SerializedName("cAmount")
    private Double amount;
    @SerializedName("cWechat")
    private String weChat;
    @SerializedName("cPayment")
    private String payment;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLinkTel() {
        return linkTel;
    }

    public void setLinkTel(String linkTel) {
        this.linkTel = linkTel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getWeChat() {
        return weChat;
    }

    public void setWeChat(String weChat) {
        this.weChat = weChat;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
}
