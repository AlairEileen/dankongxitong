package yktong.com.godofdog.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import space.eileen.tools.XString;
import yktong.com.godofdog.R;
import yktong.com.godofdog.activity.ManageMatterActivity;
import yktong.com.godofdog.activity.chat.UserChatActivity;
import yktong.com.godofdog.activity.manage.tel.TelManagerActivity;
import yktong.com.godofdog.activity.manage.DateManageActivity;
import yktong.com.godofdog.activity.manage.FansManageActivity;
import yktong.com.godofdog.activity.manage.ManageUserLineActivity;
import yktong.com.godofdog.activity.manage.PayManageActivity;
import yktong.com.godofdog.activity.manage.PerformanceActivity;
import yktong.com.godofdog.activity.market.MarketRecordActivity;
import yktong.com.godofdog.base.BaseFragment;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.ManagementFragmentBean;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OkHttpUtil;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.util.LocalDisplay;
import yktong.com.godofdog.value.UrlValue;
import yktong.com.godofdog.view.refresh.RefreshView;

/**
 * Created by vampire on 2017/6/13.
 */

//管理页
public class ManagementFragment extends BaseFragment {
    TextView mouthFansNumTv;
    TextView mouthPayTv;
    TextView daySnsTv;
    TextView dayPayTv;
    TextView dayChatTv;
    private PtrClassicFrameLayout mPtrFrame;
    private ScrollView scrollView;

    @Override
    protected int setLayout() {
        return R.layout.fragment_management;
    }

    @Override
    protected void initView() {
        mouthFansNumTv = bindView(R.id.main_mouth_fans_num_tv);
        mouthPayTv = bindView(R.id.main_mouth_pay_num_tv);
        daySnsTv = bindView(R.id.manger_day_sns_num_tv);
        dayPayTv = bindView(R.id.manger_day_pay_num_tv);
        dayChatTv = bindView(R.id.manger_day_chat_tv);
        scrollView = bindView(R.id.manage_scroll);
        LinearLayout fansMangeLayout = bindView(R.id.ll_fans_manage); //粉丝管理
        LinearLayout telMangeLayout = bindView(R.id.ll_tel_manger); // 电话管理
        LinearLayout performanceLayout = bindView(R.id.ll_performance_manager); //业绩管理
        LinearLayout snsManagerLayout = bindView(R.id.ll_sns_manager); //素材管理
        LinearLayout dateManagerLayout = bindView(R.id.ll_date_manager);//数据管理
        LinearLayout payManagerLayout = bindView(R.id.ll_pay_manager); // 订单管理
        LinearLayout ll_market_record = bindView(R.id.ll_market_record); // 任务列表
        LinearLayout ll_lbs_manager = bindView(R.id.ll_lbs_manager); // 轨迹管理
        LinearLayout chatManagerLayout = bindView(R.id.ll_chat_manage);//聊天管理
        ll_lbs_manager.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ManageUserLineActivity.class));
        });
        // 订单添加
        payManagerLayout.setOnClickListener(v -> {
            Intent pay = new Intent(mContext, PayManageActivity.class);
            startActivity(pay);
        });

        // 电话管理
        telMangeLayout.setOnClickListener(v -> {
            Intent tel = new Intent(mContext, TelManagerActivity.class);
            startActivity(tel);
        });

        // 粉丝管理
        fansMangeLayout.setOnClickListener(v -> {
            Intent fans = new Intent(mContext, FansManageActivity.class);
            startActivity(fans);
        });

        //业绩管理
        performanceLayout.setOnClickListener(v -> {
            Intent performance = new Intent(mContext, PerformanceActivity.class);
            startActivity(performance);
        });

        //数据管理
        dateManagerLayout.setOnClickListener(v -> {
            Intent date = new Intent(mContext, DateManageActivity.class);
            startActivity(date);
        });


        snsManagerLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ManageMatterActivity.class);
            startActivity(intent);
        });

        chatManagerLayout.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, UserChatActivity.class);
            startActivity(intent);
        });
        //任务列表
        ll_market_record.setOnClickListener(v -> startActivity(new Intent(getActivity(), MarketRecordActivity.class)));
    }

    @Override
    protected void initData() {
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
                setDate();
            }
        });

        setDate();
    }

    private void setDate() {
        NetTool.getInstance().startRequest(OkHttpUtil.GET, UrlValue.REQUEST_MANAGEMENT_FRAGMENT + MyApp.companyDd, ManagementFragmentBean.class, new OnHttpCallBack<ManagementFragmentBean>() {
            @Override
            public void onSuccess(ManagementFragmentBean response) {
                mouthFansNumTv.setText(XString.convertToMoney(response.getcOrderWeChatData().getCountfriend()));
                mouthPayTv.setText(XString.convertToMoney(response.getcOrderWeChatData().getMonthamount()));
                daySnsTv.setText(XString.convertToMoney(response.getcOrderWeChatData().getTodayfriend()));
                dayPayTv.setText(XString.convertToMoney(response.getcOrderWeChatData().getTodayamount()));
                dayChatTv.setText(XString.convertToMoney(response.getcOrderWeChatData().getCountlibrary()));
                mPtrFrame.refreshComplete();
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }
}
