package yktong.com.godofdog.bean.market_beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import yktong.com.godofdog.bean.FriendBean;
import yktong.com.godofdog.bean.matter_beans.MatterContentBean;

/**
 * Created by Eileen on 2017/8/2.
 */

public class MarketRecordDetail {
    @SerializedName("cUserTask")
    private MarketRecord marketRecord;
    @SerializedName("cFile")
    private TxtDataFileBean txtDataFileBean;
    @SerializedName("libraryImage")
    private MatterContentBean matterContentBean;
    @SerializedName("cWechatFriend")
    private List<FriendBean.CWechatFriend> cWechatFriendList;

    public List<FriendBean.CWechatFriend> getcWechatFriendList() {
        return cWechatFriendList;
    }

    public void setcWechatFriendList(List<FriendBean.CWechatFriend> cWechatFriendList) {
        this.cWechatFriendList = cWechatFriendList;
    }

    public TxtDataFileBean getTxtDataFileBean() {
        return txtDataFileBean;
    }

    public void setTxtDataFileBean(TxtDataFileBean txtDataFileBean) {
        this.txtDataFileBean = txtDataFileBean;
    }

    public MatterContentBean getMatterContentBean() {
        return matterContentBean;
    }

    public void setMatterContentBean(MatterContentBean matterContentBean) {
        this.matterContentBean = matterContentBean;
    }

    public MarketRecord getMarketRecord() {
        return marketRecord;
    }

    public void setMarketRecord(MarketRecord marketRecord) {
        this.marketRecord = marketRecord;
    }
}
