package yktong.com.godofdog.activity;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
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
import yktong.com.godofdog.R;
import yktong.com.godofdog.adapter.MatterListAdapter;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.MatterBean;
import yktong.com.godofdog.bean.MatterListRequestBean;
import yktong.com.godofdog.bean.matter_beans.MatterPageResponseBean;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.value.UrlValue;


public class MatterSearchActivity extends BaseActivity {

    public final static String SEARCH_PARAM = "SEARCH_PARAM";
    public final static int RESULT_MANAGE_MATTER = 2020;
    public int code;
    private String search_param;
    private EditText et_search;

    private ListView lv_matter;
    private MatterListAdapter matterListAdapter;
    private List<MatterBean> matterBeanList = new ArrayList<>();

    private int page = 1;
    private PtrClassicFrameLayout mPtrFrame;
    private boolean refresh;
    private boolean loadMore;
    private TextView noDateTv;


    @Override
    protected int setLayout() {
        return R.layout.activity_matter_search;
    }

    @Override
    protected void initView() {
        et_search = bindView(R.id.et_search);
        et_search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String param = et_search.getText().toString();
                if (param.trim().length() == 0) return false;
                search_param = param;
                page = 1;
                updateDate();
                return true;
            }
            return false;
        });

        lv_matter = bindView(R.id.lv_matter);

        matterListAdapter = new MatterListAdapter(this, matterBeanList);
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

    private void updateDate() {

        MatterListRequestBean matterListRequestBean = new MatterListRequestBean();
        matterListRequestBean.setCompanyId(MyApp.companyDd);
        matterListRequestBean.setPageIndex(page);
        matterListRequestBean.setSearchParams(search_param);
        String json = new Gson().toJson(matterListRequestBean);
        Log.d("####searchMatterList:", json);
        NetTool.getInstance().postRequest(UrlValue.REQUEST_MATTER_LIST+page, json, MatterPageResponseBean.class, new OnHttpCallBack<MatterPageResponseBean>() {
            @Override
            public void onSuccess(MatterPageResponseBean response) {
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
            } else if (page == 1) {
                matterBeanList.clear();
                matterListAdapter.setData(matterBeanList);
                mPtrFrame.autoRefresh();
            } else {
                page--;
            }
            mPtrFrame.refreshComplete();
        });
    }

    @Override
    protected void initData() {
        try {
            code = getIntent().getIntExtra("code", -1);
            search_param = getIntent().getStringExtra(SEARCH_PARAM);
            et_search.setText(search_param);
        } catch (NullPointerException e) {

        }
        updateDate();
    }

}
