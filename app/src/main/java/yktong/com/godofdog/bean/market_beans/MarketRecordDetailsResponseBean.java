package yktong.com.godofdog.bean.market_beans;

import com.google.gson.annotations.SerializedName;

import yktong.com.godofdog.bean.ResponseBaseBean;

/**
 * Created by Eileen on 2017/8/1.
 */

public class MarketRecordDetailsResponseBean extends ResponseBaseBean {
    public MarketRecordDetail getMarketRecordDetail() {
        return marketRecordDetail;
    }

    public void setMarketRecordDetail(MarketRecordDetail marketRecordDetail) {
        this.marketRecordDetail = marketRecordDetail;
    }

    @SerializedName("task")
    private MarketRecordDetail marketRecordDetail;



}
