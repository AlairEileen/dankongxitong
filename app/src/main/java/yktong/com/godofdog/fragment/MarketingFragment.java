package yktong.com.godofdog.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import space.eileen.tools.XString;
import yktong.com.godofdog.R;
import yktong.com.godofdog.activity.market.CircleFriendsActivity;
import yktong.com.godofdog.activity.market.InteractionRegularActivity;
import yktong.com.godofdog.activity.market.MarketingRegularActivity;
import yktong.com.godofdog.activity.market.SmallRoutineActivity;
import yktong.com.godofdog.activity.market.TokerAccurateActivity;
import yktong.com.godofdog.activity.market.TokerCommunityActivity;
import yktong.com.godofdog.base.BaseFragment;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.MarketingBean;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.util.LocalDisplay;
import yktong.com.godofdog.value.UrlValue;
import yktong.com.godofdog.view.refresh.RefreshView;

/**
 * Created by vampire on 2017/6/13.
 */

// 营销
public class MarketingFragment extends BaseFragment implements UrlValue {
    private LinearLayout ll_jztk;
    private LinearLayout ll_sqtk;
    private LinearLayout ll_pyq;
    private LinearLayout ll_dshd;
    private LinearLayout ll_dsyx;
    private PtrClassicFrameLayout mPtrFrame;
    private ScrollView scrollView;
    private LinearLayout ll_xcx;

    @Override
    protected int setLayout() {
        return R.layout.fragment_marketing;
    }

    @Override
    protected void initView() {
        ll_jztk = bindView(R.id.ll_jztk);
        ll_sqtk = bindView(R.id.ll_sqtk);
        ll_dshd = bindView(R.id.ll_dshd);
        ll_pyq = bindView(R.id.ll_pyq);
        ll_dsyx = bindView(R.id.ll_dsyx);
        ll_xcx = bindView(R.id.ll_xcx);
        scrollView = bindView(R.id.scroll_market);

        ll_sqtk.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TokerCommunityActivity.class);
            startActivity(intent);
        });
        ll_jztk.setOnClickListener(v -> {
            Intent jzIntent = new Intent(getActivity(), TokerAccurateActivity.class);
            startActivity(jzIntent);
        });
        ll_pyq.setOnClickListener(v1 -> {
            Intent intent1 = new Intent(getActivity(), CircleFriendsActivity.class);
            startActivity(intent1);
        });
        ll_dshd.setOnClickListener(v1 -> {
            Intent intent1 = new Intent(getActivity(), InteractionRegularActivity.class);
            startActivity(intent1);
        });
        ll_dsyx.setOnClickListener(v1 -> {
            Intent intent1 = new Intent(getActivity(), MarketingRegularActivity.class);
            startActivity(intent1);
        });
        ll_xcx.setOnClickListener(v -> startActivity(new Intent(getActivity(),SmallRoutineActivity.class)));

        TextView dayDateTv = bindView(R.id.marketing_day_date_num_tv);
        TextView dayFansTv = bindView(R.id.marketing_day_fans_num_tv);
        TextView daySnsTv = bindView(R.id.marketing_day_sns_num_tv);
        TextView dayInteractionTv = bindView(R.id.marketing_day_interaction_num_tv);
        TextView marketingTv = bindView(R.id.marketing_day_marketing_num_tv);

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
                return scrollView.getScrollY() == 0;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                setDate(dayDateTv, dayFansTv, daySnsTv, dayInteractionTv, marketingTv);
            }
        });
        setDate(dayDateTv, dayFansTv, daySnsTv, dayInteractionTv, marketingTv);
    }

    private void setDate(final TextView dayDateTv, final TextView dayFansTv, final TextView daySnsTv, final TextView dayInteractionTv, final TextView marketingTv) {
        NetTool.getInstance().startRequest("get", PAY_INFO + MyApp.userId, MarketingBean.class, new OnHttpCallBack<MarketingBean>() {
            @Override
            public void onSuccess(MarketingBean response) {
                if (response.getCode().equals("1")) {
                    int countfriend = response.getCountCWechatData().getCountfriend();
                    int countinteract = response.getCountCWechatData().getCountinteract();
                    int countmarket = response.getCountCWechatData().getCountmarket();
                    int countquan = response.getCountCWechatData().getCountquan();
                    int sum = response.getCountCWechatData().getSum();
                    dayDateTv.setText(XString.convertToMoney(sum ));
                    daySnsTv.setText(XString.convertToMoney(countquan ));
                    marketingTv.setText(XString.convertToMoney(countmarket ));
                    dayFansTv.setText(XString.convertToMoney(countfriend ));
                    dayInteractionTv.setText(XString.convertToMoney(countinteract ));
                    mPtrFrame.refreshComplete();
                }

//                WorkManger.getInstence().doTask(SpValue.STATE_CHECK_GROUP);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void initData() {
    }
}
