package yktong.com.godofdog.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.PtrHandler2;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import yktong.com.godofdog.R;
import yktong.com.godofdog.activity.WebActivity;
import yktong.com.godofdog.base.BaseFragment;
import yktong.com.godofdog.bean.MatterWechatBean;
import yktong.com.godofdog.service.task.InteractFans;
import yktong.com.godofdog.tool.BitmapUtil;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.tool.share.ShareByApi;
import yktong.com.godofdog.tool.thread.ThreadPool;
import yktong.com.godofdog.tool.view.CommonAdapter;
import yktong.com.godofdog.tool.view.CommonViewHolder;
import yktong.com.godofdog.util.LocalDisplay;
import yktong.com.godofdog.util.util.ImageFactory;
import yktong.com.godofdog.value.UrlValue;
import yktong.com.godofdog.view.refresh.RefreshView;

/**
 * Created by vampire on 2017/7/14.
 */

public class MatterWechatFragment extends BaseFragment {
    private int page = 1;
    private List<MatterWechatBean.ResultBean.ListBean> dateList;
    private CommonAdapter<MatterWechatBean.ResultBean.ListBean> adapter;
    private PtrClassicFrameLayout mPtrFrame;
    private boolean refresh;
    private boolean loadMore;
    private ListView matterLv;
    private TextView noDateTv;

    @Override
    protected int setLayout() {
        return R.layout.activity_matter_wechat;
    }

    @Override
    protected void initView() {
        matterLv = bindView(R.id.lv_matter);
        noDateTv = bindView(R.id.list_view_with_empty_view_fragment_empty_view);
        mPtrFrame = bindView(R.id.list_view_with_empty_view_fragment_ptr_frame);

        // show empty view
        mPtrFrame.setVisibility(View.INVISIBLE);
        noDateTv.setVisibility(View.VISIBLE);

        noDateTv.setOnClickListener(v -> {
            mPtrFrame.setVisibility(View.VISIBLE);
            mPtrFrame.autoRefresh();
        });

        matterLv.setAdapter(adapter);
        // header

        matterLv.setOnItemClickListener((parent, view, position, id) -> {
            MatterWechatBean.ResultBean.ListBean listBean = dateList.get(position);
            ThreadPool.thredP.execute(()->{
                Bitmap bitmap = BitmapUtil.returnBitMap(listBean.getFirstImg());
                ImageFactory imageFactory  =new ImageFactory();
                Bitmap ratio = imageFactory.ratio(bitmap, 60, 60);
                ShareByApi.getInstance().shareWeb(listBean.getUrl(),listBean.getTitle(),ratio,listBean.getTitle(), SendMessageToWX.Req.WXSceneSession);
            });
        });

        // header
        final RefreshView header = new RefreshView(getContext());
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, LocalDisplay.dp2px(15), 0, LocalDisplay.dp2px(10));
        header.setUp(mPtrFrame);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);

        dateList = new ArrayList<>();

        adapter = new CommonAdapter<MatterWechatBean.ResultBean.ListBean>(dateList, mContext, R.layout.item_matter_wechat) {
            @Override
            public void setData(MatterWechatBean.ResultBean.ListBean listBean, int position, CommonViewHolder viewHolder) {
                viewHolder.setImage(R.id.iv_icon_url, listBean.getFirstImg());
                viewHolder.setText(R.id.tv_title_url, listBean.getTitle());
                viewHolder.getView(R.id.tv_title_url).setOnClickListener(v -> {
                    Intent intent = new Intent(mContext, WebActivity.class);
                    intent.putExtra(WebActivity.WEB_TITLE, listBean.getTitle());
                    intent.putExtra(WebActivity.LOAD_URL, listBean.getUrl());
                    startActivity(intent);
                });
            }
        };

        matterLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = matterLv.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        loadMore = false;
                        refresh = true;
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = matterLv.getChildAt(matterLv.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == matterLv.getHeight()) {
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
                if (null == dateList || dateList.isEmpty()) {
                    return PtrDefaultHandler2.checkContentCanBePulledUp(frame, content, footer);
                } else {
                    return loadMore;
                }
            }

            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPtrFrame.postDelayed(() -> {
                    page++;
                    updateDate();
                }, 1500);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                if (null == dateList || dateList.isEmpty()) {
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                } else {
                    return refresh;
                }
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
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

    @Override
    protected void initData() {
        updateDate();
    }

    private void updateDate() {
        NetTool.getInstance().startRequest("get", UrlValue.WE_CHAT_DATE + page, MatterWechatBean.class, new OnHttpCallBack<MatterWechatBean>() {
            @Override
            public void onSuccess(MatterWechatBean response) {
                Log.d("MatterWechatFragment", response.getReason());
                List<MatterWechatBean.ResultBean.ListBean> resultList = response.getResult().getList();
                if (resultList != null) {
                    if (page == 1) {
                        dateList = resultList;
                        adapter.setList(dateList);
                        mPtrFrame.setVisibility(View.VISIBLE);
                        mPtrFrame.autoRefresh();
                        matterLv.setAdapter(adapter);
                    } else {
                        adapter.addList(resultList);
                        dateList.addAll(resultList);
                    }
                }
                mPtrFrame.refreshComplete();
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }
}
