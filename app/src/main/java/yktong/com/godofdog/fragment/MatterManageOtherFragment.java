package yktong.com.godofdog.fragment;

import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler2;
import space.eileen.free_util.ProgressDialog;
import yktong.com.godofdog.R;
import yktong.com.godofdog.activity.ManageMatterActivity;
import yktong.com.godofdog.adapter.MatterListAdapter;
import yktong.com.godofdog.base.BaseFragment;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.MatterBean;
import yktong.com.godofdog.bean.MatterListRequestBean;
import yktong.com.godofdog.bean.matter_beans.MatterPageResponseBean;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.value.UrlValue;

import static yktong.com.godofdog.fragment.MatterManageOtherFragment.OrderIconManager.IconStatus.def;
import static yktong.com.godofdog.fragment.MatterManageOtherFragment.OrderIconManager.IconStatus.down;


public class MatterManageOtherFragment extends BaseFragment {
    private int typeId = 5;
    private int code = -1;
    private ListView lv_matter;
    private MatterListAdapter matterListAdapter;
    private List<MatterBean> matterBeanList = new ArrayList<>();

    private int page = 1;
    private PtrClassicFrameLayout mPtrFrame;
    private boolean refresh;
    private boolean loadMore;
    private TextView noDateTv;

    private OrderManagerUploadTime orderManagerUploadTime;
    private OrderManagerUseTimes orderManagerUseTimes;
    private ProgressDialog progressDialogLoading;

    @Override
    protected int setLayout() {
        return R.layout.fragment_matter_manage_product;
    }

    @Override
    protected void initView() {
        lv_matter = bindView(R.id.lv_matter);
        LinearLayout ll_use_times = bindView(R.id.ll_use_times);
        LinearLayout ll_upload_time = bindView(R.id.ll_upload_time);
        ImageView iv_ut = bindView(R.id.iv_ut);
        ImageView iv_ult = bindView(R.id.iv_ult);
        if (orderManagerUseTimes == null) {
            orderManagerUseTimes = new OrderManagerUseTimes(iv_ut, new OrderIconManager.OnOrderChanged() {
                @Override
                public void asc() {
                    code = 4;
                    page = 1;
                    updateDate();
                }

                @Override
                public void desc() {
                    code = 3;
                    page = 1;
                    updateDate();
                }
            });
        }
        if (orderManagerUploadTime == null) {
            orderManagerUploadTime = new OrderManagerUploadTime(iv_ult, new OrderIconManager.OnOrderChanged() {
                @Override
                public void asc() {
                    code = 2;
                    page = 1;
                    updateDate();
                }

                @Override
                public void desc() {
                    code = 1;
                    page = 1;
                    updateDate();

                }
            });
        }
        ll_use_times.setOnClickListener(v -> {

            orderManagerUseTimes.orderByUseTimes();
            orderManagerUploadTime.reset();
        });
        ll_upload_time.setOnClickListener(v -> {

            orderManagerUploadTime.orderByUploadTime();
            orderManagerUseTimes.reset();
        });

        matterListAdapter = new MatterListAdapter((ManageMatterActivity) getActivity(), matterBeanList);
        lv_matter.setAdapter(matterListAdapter);


        noDateTv = bindView(R.id.list_view_with_empty_view_fragment_empty_view);
        mPtrFrame = bindView(R.id.list_view_with_empty_view_fragment_ptr_frame);

        // show empty view
        mPtrFrame.setVisibility(View.INVISIBLE);
        noDateTv.setVisibility(View.VISIBLE);

        noDateTv.setOnClickListener(v -> {
            mPtrFrame.setVisibility(View.VISIBLE);
            mPtrFrame.autoRefresh();
        });


        lv_matter.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = lv_matter.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        Log.d("ListView", "##### 滚动到顶部 #####");
                        loadMore = false;
                        refresh = true;
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = lv_matter.getChildAt(lv_matter.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == lv_matter.getHeight()) {
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
                if (null == matterBeanList || matterBeanList.isEmpty()) {
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
                if (null == matterBeanList || matterBeanList.isEmpty()) {
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
        if (progressDialogLoading == null)
            progressDialogLoading = ((ManageMatterActivity)getActivity()).getProgressDialogLoading();
        progressDialogLoading.show();
        MatterListRequestBean matterListRequestBean = new MatterListRequestBean();
        matterListRequestBean.setLibrariesTortId(typeId);
        matterListRequestBean.setCompanyId(MyApp.companyDd);
        matterListRequestBean.setPageIndex(page);
        if (code != -1) matterListRequestBean.setOrderByCode(code);
        String json = new Gson().toJson(matterListRequestBean);
        Log.d("####caseMatterList:", json);
        NetTool.getInstance().postRequest(UrlValue.REQUEST_MATTER_LIST+page, json, MatterPageResponseBean.class, new OnHttpCallBack<MatterPageResponseBean>() {
            @Override
            public void onSuccess(MatterPageResponseBean response) {
                if (progressDialogLoading!=null)progressDialogLoading.dismiss();

//                Log.d("MatterWechatFragment", response.getReason());
                doSuccess(response);

            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void doSuccess(MatterPageResponseBean response) {
        response.doResponse(() -> {
            List<MatterBean> resultList = response.getLibrarys().toMatterBeanList();
            if (resultList != null) {
                if (page == 1) {
//                    matterBeanList.clear();
                    matterBeanList = resultList;
                    matterListAdapter.setData(resultList);
                    mPtrFrame.setVisibility(View.VISIBLE);
                    mPtrFrame.autoRefresh();
                } else {
                    matterListAdapter.addData(resultList);
                    matterBeanList.addAll(resultList);
                }
            }
            mPtrFrame.refreshComplete();
        });
    }


    public  static class OrderIconManager {
        protected ImageView imageView;
        protected IconStatus iconStatus = down;
        private OnOrderChanged onOrderChanged;

        private OrderIconManager(ImageView imageView, OnOrderChanged onOrderChanged) {
            this.imageView = imageView;
            this.onOrderChanged = onOrderChanged;
        }

        private int changeIcon() {
            switch (iconStatus) {
                case def:
                    iconStatus = down;
                    return R.mipmap.icon_px_def;
                case up:
                    iconStatus = down;
                    onOrderChanged.asc();
                    return R.mipmap.icon_px_up;
                case down:
                    iconStatus = IconStatus.up;
                    onOrderChanged.desc();
                    return R.mipmap.icon_px_down;
                default:
                    return R.mipmap.icon_px_def;
            }
        }

        protected void refreshIcon() {
            imageView.setImageResource(changeIcon());
        }

        void reset() {
            iconStatus = def;
            imageView.setImageResource(changeIcon());
        }


        enum IconStatus {
            def, up, down;
        }

        public   static interface OnOrderChanged {
            void asc();

            void desc();
        }
    }

    public     static class OrderManagerUploadTime extends OrderIconManager {


        OrderManagerUploadTime(ImageView imageView, OnOrderChanged onOrderChanged) {
            super(imageView, onOrderChanged);
        }

        void orderByUploadTime() {
            refreshIcon();
        }
    }

    public     static class OrderManagerUseTimes extends OrderIconManager {


        OrderManagerUseTimes(ImageView imageView, OnOrderChanged onOrderChanged) {
            super(imageView, onOrderChanged);
        }

        void orderByUseTimes() {
            refreshIcon();
        }

    }

}
