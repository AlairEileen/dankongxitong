package yktong.com.godofdog.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import space.eileen.tools.XString;
import yktong.com.godofdog.R;
import yktong.com.godofdog.activity.MyOrderActivity;
import yktong.com.godofdog.activity.chat.ChatHistoryActivity;
import yktong.com.godofdog.activity.chat.ChatInfoActivity;
import yktong.com.godofdog.activity.chat.UserChatActivity;
import yktong.com.godofdog.activity.market.CircleFriendsActivity;
import yktong.com.godofdog.activity.market.InteractionRegularActivity;
import yktong.com.godofdog.activity.market.MarketingRegularActivity;
import yktong.com.godofdog.activity.market.TokerAccurateActivity;
import yktong.com.godofdog.activity.market.TokerCommunityActivity;
import yktong.com.godofdog.base.BaseFragment;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.MainFragmentInfoBean;
import yktong.com.godofdog.bean.MainFragmentLabelBean;
import yktong.com.godofdog.bean.wechat.UserChatBean;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.tool.share.ShareByApi;
import yktong.com.godofdog.tool.view.CommonAdapter;
import yktong.com.godofdog.tool.view.CommonViewHolder;
import yktong.com.godofdog.util.LocalDisplay;
import yktong.com.godofdog.value.SpValue;
import yktong.com.godofdog.value.UrlValue;
import yktong.com.godofdog.view.refresh.RefreshView;

/**
 * Created by vampire on 2017/6/13.
 */

public class MainFragment extends BaseFragment implements SpValue {

    private GridView laeblGv;
    private PtrClassicFrameLayout mPtrFrame;

    @Override
    protected int setLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView() {
        TextView totalFansTv = bindView(R.id.main_my_fans_num_tv);
        totalFansTv.setOnClickListener(v -> {

        });
        TextView mouthFansTv = bindView(R.id.main_mouth_fans_num_tv);
        TextView mouthPayTv = bindView(R.id.main_mouth_pay_num_tv);
        TextView dayFansTv = bindView(R.id.main_day_fans_num_tv);
        TextView daySnsTv = bindView(R.id.main_day_sns_num_tv);
        TextView dayChatTv = bindView(R.id.main_day_pay_num_tv);

        mPtrFrame = bindView(R.id.refresh_frame_layout);
        // header
        final RefreshView header = new RefreshView(getContext());
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, LocalDisplay.dp2px(15), 0, LocalDisplay.dp2px(10));
        header.setUp(mPtrFrame);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);

        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return true;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                setDate(totalFansTv, mouthFansTv, mouthPayTv, dayFansTv, daySnsTv, dayChatTv);
            }
        });
        setDate(totalFansTv, mouthFansTv, mouthPayTv, dayFansTv, daySnsTv, dayChatTv);

        laeblGv = bindView(R.id.main_label_list);
        int[] pics = new int[]{R.mipmap.sy_icon_jztk, R.mipmap.sy_icon_sqtk, R.mipmap.sy_icon_pyq, R.mipmap.sy_icon_dhyx, R.mipmap.sy_icon_dshd, R.mipmap.sy_icon_wddd};
        String[] titlte = new String[]{"精准拓客", "社群拓客", "朋友圈+", "定时营销", "定时互动", "我的订单"};
        ArrayList<MainFragmentLabelBean> been = new ArrayList<>();
        for (int i = 0; i < pics.length; i++) {
            been.add(new MainFragmentLabelBean(pics[i], titlte[i]));
        }
        CommonAdapter<MainFragmentLabelBean> adapter = new CommonAdapter<MainFragmentLabelBean>(been, mContext, R.layout.gv_main) {
            @Override
            public void setData(MainFragmentLabelBean mainFragmentLabelBean, int position, CommonViewHolder viewHolder) {
                viewHolder.setImage(R.id.gv_label_icon, mainFragmentLabelBean.getId());
                viewHolder.setText(R.id.gv_label_title, mainFragmentLabelBean.getTitle());
            }
        };
        laeblGv.setAdapter(adapter);

    }

    private void setDate(final TextView totalFansTv, final TextView mouthFansTv, final TextView mouthPayTv, final TextView dayFansTv, final TextView daySnsTv, final TextView dayChatTv) {
        NetTool.getInstance().startRequest("get", UrlValue.MAIN_INFO + MyApp.userId, MainFragmentInfoBean.class, new OnHttpCallBack<MainFragmentInfoBean>() {
            @Override
            public void onSuccess(MainFragmentInfoBean response) {
                if (response.getCode().equals("1")) {
                    MainFragmentInfoBean.IndexDataBean indexData = response.getIndexData();
                    totalFansTv.setText(XString.convertToMoney(indexData.getCountfriend()));
                    mouthFansTv.setText(XString.convertToMoney(indexData.getMonthfriend()));
                    mouthPayTv.setText(XString.convertToMoney(indexData.getMonthamount()));
                    dayFansTv.setText(XString.convertToMoney(indexData.getTodayfriend()));
                    daySnsTv.setText(XString.convertToMoney(indexData.getTodayquan()));
                    dayChatTv.setText(XString.convertToMoney(indexData.getTodaychat()));
                    mPtrFrame.refreshComplete();
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    @Override
    protected void initData() {
        laeblGv.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    // TODO: 2017/6/29  精准拓客
                    Intent jzIntent = new Intent(getActivity(), TokerAccurateActivity.class);
                    startActivity(jzIntent);
                    break;
                case 1:
                    // TODO: 2017/6/29  社群拓客
                    Intent intent = new Intent(getActivity(), TokerCommunityActivity.class);
                    startActivity(intent);
                    break;
                case 2:
                    // TODO: 2017/6/29  朋友圈+
                    Intent intent1 = new Intent(getActivity(), CircleFriendsActivity.class);
                    startActivity(intent1);
                    break;
                case 3:
                    // TODO: 2017/6/29 定时营销
                    Intent marketing = new Intent(mContext, MarketingRegularActivity.class);
                    startActivity(marketing);
                    break;
                case 4:
                    // TODO: 2017/6/29  定时互动
                    Intent interaction = new Intent(mContext, InteractionRegularActivity.class);
                    startActivity(interaction);
                    break;
                case 5:
                    // TODO: 2017/6/29 我的订单
                    Intent myorder = new Intent(mContext, MyOrderActivity.class);
                    startActivity(myorder);
                    break;
            }
        });
    }
}
