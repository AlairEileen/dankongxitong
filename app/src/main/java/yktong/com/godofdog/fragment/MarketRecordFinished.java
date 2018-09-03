package yktong.com.godofdog.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler2;
import space.eileen.free_util.ProgressDialog;
import yktong.com.godofdog.R;
import yktong.com.godofdog.activity.market.MarketRecordActivity;
import yktong.com.godofdog.activity.market.MarketRecordDetailsActivity;
import yktong.com.godofdog.base.BaseFragment;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.market_beans.MarketRecord;
import yktong.com.godofdog.bean.market_beans.MarketRecordResponseBean;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.tool.view.CommonAdapter;
import yktong.com.godofdog.tool.view.CommonViewHolder;
import yktong.com.godofdog.value.UrlValue;

/**
 * Created by Eileen on 2017/7/31.
 */

public class MarketRecordFinished extends BaseFragment {

    CommonAdapter<MarketRecord> commonAdapter;
    MarketRecordResponseBean marketRecordResponseBean = new MarketRecordResponseBean();
    List<MarketRecord> marketRecordList=new ArrayList<>();

    private boolean loadMore;
    private boolean refresh;
    private TextView noDateTv;
    private int pageIndex = 1;
    private int pageSize = 10;
    private PtrClassicFrameLayout mPtrFrame;
    private int stage = 2;//已完成列表
    private ProgressDialog progressDialogLoading;

    @Override
    protected int setLayout() {
        return R.layout.fragment_market_record_finished;
    }

    @Override
    protected void initView() {
        ListView lv_market = bindView(R.id.lv_market);
        lv_market.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getActivity(), MarketRecordDetailsActivity.class);
            intent.putExtra(MarketRecordDetailsActivity.MARKET_RECORD_ID, marketRecordList.get(position).getId());
            startActivity(intent);
        });

        commonAdapter = new CommonAdapter<MarketRecord>(marketRecordList, getActivity(), R.layout.item_market_record_finished) {
            @Override
            public void setData(MarketRecord marketRecord, int position, CommonViewHolder viewHolder) {
                TextView tv_rwlx = viewHolder.getView(R.id.tv_rwlx);
                TextView tv_zxsj = viewHolder.getView(R.id.tv_zxsj);
                TextView tv_zxzt = viewHolder.getView(R.id.tv_zxzt);
                tv_rwlx.setText(marketRecord.getTypeText() + "");
                tv_zxsj.setText(marketRecord.getTimeText() + "");
                tv_zxzt.setText(marketRecord.getStatusText() + "");
            }
        };
        lv_market.setAdapter(commonAdapter);

        noDateTv = bindView(R.id.list_view_with_empty_view_fragment_empty_view);
        mPtrFrame = bindView(R.id.list_view_with_empty_view_fragment_ptr_frame);

        // show empty view
        mPtrFrame.setVisibility(View.INVISIBLE);
        noDateTv.setVisibility(View.VISIBLE);

        noDateTv.setOnClickListener(v -> {
            mPtrFrame.setVisibility(View.VISIBLE);
            mPtrFrame.autoRefresh();
        });


        lv_market.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = lv_market.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        Log.d("ListView", "##### 滚动到顶部 #####");
                        loadMore = false;
                        refresh = true;
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = lv_market.getChildAt(lv_market.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == lv_market.getHeight()) {
                        refresh = false;
                        loadMore = true;

                    }
                } else {
                    refresh = false;
                    loadMore = false;
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //do nothing
            }

        });

        mPtrFrame.setPtrHandler(new PtrHandler2() {
            @Override
            public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
                if (null == marketRecordList || marketRecordList.isEmpty()) {
                    return PtrDefaultHandler2.checkContentCanBePulledUp(frame, content, footer);
                } else {
                    return loadMore;
                }

            }

            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPtrFrame.postDelayed(() -> {
                    pageIndex++;
                    updateDate();
                }, 1500);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                if (null == marketRecordList || marketRecordList.isEmpty()) {
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                } else {
                    return refresh;
                }

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageIndex = 1;
                updateDate();
            }
        });
        // the following are default settings
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        mPtrFrame.setPullToRefresh(true);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);


    }

    private void updateDate() {
        if (progressDialogLoading == null)
            progressDialogLoading = ((MarketRecordActivity)getActivity()).getProgressDialogLoading();
        progressDialogLoading.show();
        NetTool.getInstance().startRequest("get", UrlValue.FIND_TASK_BY_STAGE + MyApp.userId +
                UrlValue.FIND_TASK_BY_STAGE_PARAM_STAGE + stage +
                UrlValue.REQUEST_MATTER_LIST_PARAM_PAGE_INDEX +
                pageIndex + UrlValue.REQUEST_MATTER_LIST_PARAM_PAGE_SIZE +
                pageSize, MarketRecordResponseBean.class, new OnHttpCallBack<MarketRecordResponseBean>() {
            @Override
            public void onSuccess(MarketRecordResponseBean response) {
                if (progressDialogLoading!=null)progressDialogLoading.dismiss();
                doSuccess(response);
            }

            @Override
            public void onError(Throwable e) {

            }
        });

    }

    private void doSuccess(MarketRecordResponseBean response) {
        response.doResponse(() -> {
            List<MarketRecord> resultList = response.getMarketRecordList();
            if (resultList != null) {
                if (pageIndex == 1) {
                    marketRecordList = resultList;
                    commonAdapter.setList(marketRecordList);
                    mPtrFrame.setVisibility(View.VISIBLE);
                    mPtrFrame.autoRefresh();
//                    matterLv.setAdapter(adapter);
                } else {
                    commonAdapter.addList(resultList);
                    marketRecordList.addAll(resultList);
                }
            }
            mPtrFrame.refreshComplete();
        });
    }

    @Override
    protected void initData() {
        updateDate();
    }
}
