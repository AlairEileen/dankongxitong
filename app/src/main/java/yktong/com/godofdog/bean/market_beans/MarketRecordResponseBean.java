package yktong.com.godofdog.bean.market_beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import yktong.com.godofdog.bean.ResponseBaseBean;

/**
 * Created by Eileen on 2017/8/1.
 */

public class MarketRecordResponseBean extends ResponseBaseBean {
    @SerializedName("usertask")
    private List<MarketRecord> marketRecordList;


    public List<MarketRecord> getMarketRecordList() {
        return marketRecordList;
    }

    public void setMarketRecordList(List<MarketRecord> marketRecordList) {
        this.marketRecordList = marketRecordList;
    }
}
