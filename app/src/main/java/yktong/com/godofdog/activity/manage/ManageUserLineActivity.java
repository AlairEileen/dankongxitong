package yktong.com.godofdog.activity.manage;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;

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
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.map_beans.DeptBean;
import yktong.com.godofdog.bean.map_beans.UsersLocationBean;
import yktong.com.godofdog.bean.map_beans.UsersLocationResponseBean;
import yktong.com.godofdog.bean.map_beans.UsersLocationViewBean;
import yktong.com.godofdog.bean.market_beans.MarketRecordResponseBean;
import yktong.com.godofdog.map.amap.LineMapActivity;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.tool.view.CommonAdapter;
import yktong.com.godofdog.tool.view.CommonViewHolder;
import yktong.com.godofdog.value.UrlValue;


public class ManageUserLineActivity extends BaseActivity {


    CommonAdapter<UsersLocationBean> commonAdapter;
    MarketRecordResponseBean marketRecordResponseBean = new MarketRecordResponseBean();
    List<UsersLocationBean> userLocationBeanList = new ArrayList<>();

    private boolean loadMore;
    private boolean refresh;
    private TextView noDateTv;
    private int pageIndex = 1;
    private int pageSize = 10;
    private PtrClassicFrameLayout mPtrFrame;
    private List<DeptBean> deptBeanList = new ArrayList<>();
    private WindowManager.LayoutParams params;
    private int deptId = -1;
    private OptionsPickerView pvOptions;
    TextView dept_name_tv;
    private ProgressDialog progressDialogLoading;

    @Override
    protected int setLayout() {
        return R.layout.activity_manage_user_line;
    }

    @Override
    protected void initView() {

        RelativeLayout choose_dept_rl = bindView(R.id.choose_dept_rl);
        dept_name_tv = bindView(R.id.dept_name_tv);
        choose_dept_rl.setOnClickListener(v -> {
            if (pvOptions!=null)  pvOptions.show();

//            PikerPopup popup = new PikerPopup(mContext, position -> {
//                dept_name_tv.setText(deptBeanList.get(position).getName());
//                pageIndex = 1;
//                deptId = deptBeanList.get(position).getId();
//                updateDate();
//
//            }, deptBeanList,
//                    new CommonAdapter<DeptBean>(deptBeanList, mContext, R.layout.piker_item) {
//                        @Override
//                        public void setData(DeptBean deptBean, int position, CommonViewHolder viewHolder) {
//                            Log.d("PerformanceActivity", deptBean.toString());
//                            Log.d("PerformanceActivity", "deptBean.getCDeptname()==null:" + (deptBean.getName() == null));
//                            viewHolder.setText(R.id.picker_item_tv, deptBean.getName());
//                        }
//                    });
//            popup.showAtLocation(findViewById(R.id.choose_dept_rl), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//            params = getWindow().getAttributes();
//            //当弹出Popupwindow时，背景变半透明
//            params.alpha = 0.7f;
//            getWindow().setAttributes(params);
//            //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
//            popup.setOnDismissListener(() -> {
//                params = getWindow().getAttributes();
//                params.alpha = 1f;
//                getWindow().setAttributes(params);
//            });
        });


        ListView lv_market = bindView(R.id.lv_line_users);
        lv_market.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, LineMapActivity.class);
            intent.putExtra(LineMapActivity.USER_ID, userLocationBeanList.get(position).getId());
            startActivity(intent);
        });
        commonAdapter = new CommonAdapter<UsersLocationBean>(userLocationBeanList, this, R.layout.item_line_location_users) {

            @Override
            public void setData(UsersLocationBean userLocationBean, int position, CommonViewHolder viewHolder) {
                viewHolder.setText(R.id.tv_user_name, userLocationBean.getName());
                viewHolder.setText(R.id.tv_course, userLocationBean.getCountJourneyKM() + "");

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
                if (null == userLocationBeanList || userLocationBeanList.isEmpty()) {
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
                if (null == userLocationBeanList || userLocationBeanList.isEmpty()) {
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

    private void resetDeptView() {

        List<String> options = new ArrayList<>();
        String all = "全部";
        options.add(all);
        for (DeptBean deptBean : deptBeanList) {
            options.add(deptBean.getName());
        }

        pvOptions = new OptionsPickerView.Builder(this, (options1, option2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            String tx = options1 == 0 ? all : deptBeanList.get(options1 - 1).getName();
            deptId = options1 == 0 ? -1 : deptBeanList.get(options1 - 1).getId();
            dept_name_tv.setText(tx);
           updateDate();
        })
                .setTitleSize(8)
                .setSubCalSize(15)
                .setSubmitColor(getResources().getColor(R.color.text_md_gray))
                .setCancelColor(getResources().getColor(R.color.text_md_gray))
                .setTitleBgColor(getResources().getColor(R.color.theme_white))
                .setBgColor(getResources().getColor(R.color.content_split_gray))
                .setContentTextSize(18)
                .setTextColorCenter(getResources().getColor(R.color.text_black))
                .setTextColorCenter(getResources().getColor(R.color.text_md_gray))
                .build();
        pvOptions.setPicker(options);

    }

    private void updateDate() {
        if (progressDialogLoading == null)
            progressDialogLoading = new ProgressDialog(this, R.mipmap.loading);
        progressDialogLoading.show();
        NetTool.getInstance().startRequest("get", UrlValue.FIND_USERS_LINE + MyApp.companyDd +
                (deptId == -1 ? "" : UrlValue.FIND_USERS_LINE_DEPTS + deptId) +
                UrlValue.REQUEST_MATTER_LIST_PARAM_PAGE_INDEX +
                pageIndex + UrlValue.FANS_MANAGER_PARAM_USER_ID + MyApp.userId, UsersLocationResponseBean.class, new OnHttpCallBack<UsersLocationResponseBean>() {
            @Override
            public void onSuccess(UsersLocationResponseBean response) {
                if (progressDialogLoading!=null)progressDialogLoading.dismiss();
                doSuccess(response);
            }

            @Override
            public void onError(Throwable e) {

            }
        });

    }

    private void doSuccess(UsersLocationResponseBean response) {
        response.doResponse(() -> {
            UsersLocationViewBean usersLocationViewBean = response.getUsersLocationViewBean();
            if (usersLocationViewBean != null) {
                if (usersLocationViewBean.getUsersLocationBeanList() != null) {
                    if (pageIndex == 1) {
                        userLocationBeanList = usersLocationViewBean.getUsersLocationBeanList();
                        commonAdapter.setList(userLocationBeanList);
                        mPtrFrame.setVisibility(View.VISIBLE);
                        mPtrFrame.autoRefresh();
                    } else {
                        commonAdapter.addList(usersLocationViewBean.getUsersLocationBeanList());
                        userLocationBeanList.addAll(usersLocationViewBean.getUsersLocationBeanList());
                    }
                }
                mPtrFrame.refreshComplete();
                if (usersLocationViewBean.getDeptBeanList() != null) {
                    deptBeanList.clear();
                    deptBeanList.addAll(usersLocationViewBean.getDeptBeanList());
                    resetDeptView();
                }
            }
        });
    }

    @Override
    protected void initData() {
        updateDate();
    }


}
