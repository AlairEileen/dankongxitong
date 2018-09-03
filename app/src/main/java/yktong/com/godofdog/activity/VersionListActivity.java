package yktong.com.godofdog.activity;

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
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.bean.versons.AppUpdateInfoBean;
import yktong.com.godofdog.bean.versons.VersionListResponseBean;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OkHttpUtil;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.tool.view.CommonAdapter;
import yktong.com.godofdog.tool.view.CommonViewHolder;
import yktong.com.godofdog.value.UrlValue;

public class VersionListActivity extends BaseActivity {

    List<AppUpdateInfoBean> appUpdateInfoBeanList = new ArrayList<>();
    private ListView lv_version;
    private CommonAdapter<AppUpdateInfoBean> adapter;
    private TextView noDateTv;
    private PtrClassicFrameLayout mPtrFrame;
    private boolean loadMore, refresh;
    private int pageIndex = 1;
    private ProgressDialog progressDialogLoading;

    @Override
    protected int setLayout() {
        return R.layout.activity_version_list;
    }

    @Override
    protected void initView() {
        lv_version = bindView(R.id.lv_version);
        adapter = new CommonAdapter<AppUpdateInfoBean>(appUpdateInfoBeanList, this, R.layout.item_version) {
            @Override
            public void setData(AppUpdateInfoBean appUpdateInfoBean, int position, CommonViewHolder viewHolder) {
                ((TextView) viewHolder.getView(R.id.tv_title)).setText("动销一号" + appUpdateInfoBean.getcVername() + "更新");
                ((TextView) viewHolder.getView(R.id.tv_date)).setText(appUpdateInfoBean.getcUpdatetimeText());
            }
        };
        lv_version.setAdapter(adapter);


        lv_version.setOnItemClickListener((parent, view, position, id) -> {

            Intent intent = new Intent(this, VersionInfoActivity.class);
            intent.putExtra(VersionInfoActivity.VERSION_ID, appUpdateInfoBeanList.get(position).getcVid());
            startActivity(intent);
        });


        noDateTv = bindView(R.id.list_view_with_empty_view_fragment_empty_view);
        mPtrFrame = bindView(R.id.list_view_with_empty_view_fragment_ptr_frame);

        // show empty view
        mPtrFrame.setVisibility(View.INVISIBLE);
        noDateTv.setVisibility(View.VISIBLE);

        noDateTv.setOnClickListener(v -> {
            mPtrFrame.setVisibility(View.VISIBLE);
            mPtrFrame.autoRefresh();
        });


        lv_version.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = lv_version.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        Log.d("ListView", "##### 滚动到顶部 #####");
                        loadMore = false;
                        refresh = true;
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = lv_version.getChildAt(lv_version.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == lv_version.getHeight()) {
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
                if (null == appUpdateInfoBeanList || appUpdateInfoBeanList.isEmpty()) {
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
                if (null == appUpdateInfoBeanList || appUpdateInfoBeanList.isEmpty()) {
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
            progressDialogLoading = new ProgressDialog(this, R.mipmap.loading);
        progressDialogLoading.show();
        NetTool.getInstance().startRequest(OkHttpUtil.GET, UrlValue.REQUEST_VERSION_LIST, VersionListResponseBean.class, new OnHttpCallBack<VersionListResponseBean>() {
            @Override
            public void onSuccess(VersionListResponseBean response) {
                if (progressDialogLoading!=null)progressDialogLoading.dismiss();
                response.doResponse(() -> {
                    List<AppUpdateInfoBean> appUpdateInfoBeens = response.getAppUpdateInfoBeanList();
                    if (appUpdateInfoBeens != null) {
                        if (pageIndex == 1) {
                            appUpdateInfoBeanList = appUpdateInfoBeens;
                            adapter.setList(appUpdateInfoBeens);
                            mPtrFrame.setVisibility(View.VISIBLE);
                            mPtrFrame.autoRefresh();
                        } else {
                            adapter.addList(appUpdateInfoBeens);
                            appUpdateInfoBeanList.addAll(appUpdateInfoBeens);
                        }
                    }
                    mPtrFrame.refreshComplete();
                });
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    @Override
    protected void initData() {
        updateDate();
    }
}
