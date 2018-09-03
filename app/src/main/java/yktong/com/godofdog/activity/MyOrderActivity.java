package yktong.com.godofdog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler2;
import yktong.com.godofdog.R;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.MyOrderBean;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.tool.view.CommonAdapter;
import yktong.com.godofdog.tool.view.CommonViewHolder;
import yktong.com.godofdog.util.time.DateUtils;
import yktong.com.godofdog.value.UrlValue;

/**
 * Created by vampire on 2017/7/6.
 */

public class MyOrderActivity extends BaseActivity implements UrlValue {
    public final static String USER_ID = "user_id";
    public final static String USER_NAME = "USER_NAME";
    String userName = null;
    CommonAdapter<MyOrderBean.OrdersBean> adapter;
    int userId = -1;
    private ArrayList<MyOrderBean.OrdersBean> been = new ArrayList<>();
    private boolean loadMore;
    private boolean refresh;
    private TextView noDateTv;
    private int pageIndex = 1;
    private int pageSize = 10;
    private PtrClassicFrameLayout mPtrFrame;
    private LinearLayout ll_no_order;
    private ListView orderLv;

    protected int setLayout() {
        return R.layout.activity_my_order;
    }

    @Override
    protected void initView() {
        orderLv = bindView(R.id.my_order_lv);
        TextView tv_title = bindView(R.id.tv_title);
        ll_no_order = bindView(R.id.ll_no_order);
        try {
            userId = getIntent().getIntExtra(USER_ID, -1);
            this.userName = getIntent().getStringExtra(USER_NAME);
        } catch (NullPointerException e) {

        }
        if (userId == -1) {
            userId = MyApp.userId;
        } else {
            tv_title.setText(userName + "的订单");
        }


        noDateTv = bindView(R.id.list_view_with_empty_view_fragment_empty_view);
        mPtrFrame = bindView(R.id.list_view_with_empty_view_fragment_ptr_frame);

        // show empty view
        mPtrFrame.setVisibility(View.INVISIBLE);
        noDateTv.setVisibility(View.VISIBLE);

        noDateTv.setOnClickListener(v -> {
            mPtrFrame.setVisibility(View.VISIBLE);
            mPtrFrame.autoRefresh();
        });


        orderLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = orderLv.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        Log.d("ListView", "##### 滚动到顶部 #####");
                        loadMore = false;
                        refresh = true;
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = orderLv.getChildAt(orderLv.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == orderLv.getHeight()) {
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
                if (null == been || been.isEmpty()) {
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
                if (null == been || been.isEmpty()) {
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

        adapter = new CommonAdapter<MyOrderBean.OrdersBean>(been, mContext, R.layout.order_item) {
            @Override
            public void setData(MyOrderBean.OrdersBean ordersBean, int position, CommonViewHolder viewHolder) {
//                Log.d("MyOrderActivity", ordersBean.toString());
//                viewHolder.setText(R.id.order_info_product_tv, ordersBean.getCProduct());
//                viewHolder.setText(R.id.order_info_name_tv, ordersBean.getCCustomername());
//                viewHolder.setText(R.id.order_info_money_tv, ordersBean.getCAmount() + "元");

                Log.d("MyOrderActivity", ordersBean.toString());
                viewHolder.setText(R.id.order_info_product_tv, ordersBean.getCProduct());
                viewHolder.setText(R.id.order_info_name_tv, ordersBean.getCCustomername());
                viewHolder.setText(R.id.order_info_money_tv, ordersBean.getCAmount() + "元");
                String times = DateUtils.times(ordersBean.getCOrdertime());
                viewHolder.setText(R.id.order_info_time_tv, times);
//                                viewHolder.getView(R.id.item_info_rl).setOnClickListener(v -> {
//                                    Intent intent = new Intent(mContext, OrderInfoActivity.class);
//                                    Bundle bundle = new Bundle();
//                                    bundle.putSerializable("argment", ordersBean);
////                                    intent.putExtra(USER_NAME,userName);
//                                    startActivity(intent, bundle);
//                                });
            }
        };
        orderLv.setAdapter(adapter);


        orderLv.setOnItemClickListener((parent, view, position, id) -> {
            Log.d("MyOrderActivity", "click");
            Intent intent = new Intent(mContext, OrderInfoActivity.class);
            intent.putExtra(USER_NAME, userName);
            Bundle bundle = new Bundle();
            bundle.putSerializable("bean", been.get(position));
            intent.putExtra("argument", bundle);
            startActivity(intent);
        });


    }

    public void updateDate() {
        ll_no_order.setVisibility(View.GONE);
        NetTool.getInstance().startRequest("get", UrlValue.GET_ORDER + userId +
                UrlValue.REQUEST_MATTER_LIST_PARAM_PAGE_INDEX +
                pageIndex, MyOrderBean.class, new OnHttpCallBack<MyOrderBean>() {
            @Override
            public void onSuccess(MyOrderBean response) {
                int code = Integer.parseInt(response.getCode());
                switch (code) {
                    case SUCCESS:
                        List<MyOrderBean.OrdersBean> resBeen = (ArrayList<MyOrderBean.OrdersBean>) response.getOrders();
//                        CommonAdapter<MyOrderBean.OrdersBean> adapter = new CommonAdapter<MyOrderBean.OrdersBean>(response.getOrders(), mContext, R.layout.order_item) {
//                            @Override
//                            public void setData(MyOrderBean.OrdersBean ordersBean, int position, CommonViewHolder viewHolder) {
//                                Log.d("MyOrderActivity", ordersBean.toString());
//                                viewHolder.setText(R.id.order_info_product_tv, ordersBean.getCProduct());
//                                viewHolder.setText(R.id.order_info_name_tv, ordersBean.getCCustomername());
//                                viewHolder.setText(R.id.order_info_money_tv, ordersBean.getCAmount() + "元");
//                                String times = DateUtils.times(ordersBean.getCOrdertime());
//                                viewHolder.setText(R.id.order_info_time_tv, times);
//                            }
//                        };
//                        ArrayList<MyOrderBean.OrdersBean> resultList = (ArrayList<MyOrderBean.OrdersBean>) response.getOrders();
                        if (resBeen != null) {
                            if (pageIndex == 1) {
                                runOnUiThread(() -> {
                                    if (resBeen.size() == 0) {
                                        ll_no_order.setVisibility(View.VISIBLE);
                                    } else {
                                        ll_no_order.setVisibility(View.GONE);
                                    }
                                });
                                adapter.setList(resBeen);
//                                adapter.notifyDataSetChanged();
                                mPtrFrame.setVisibility(View.VISIBLE);
                                mPtrFrame.autoRefresh();
                            } else {
                                adapter.addList(resBeen);
                                been.addAll(resBeen);
                            }
                        }
                        mPtrFrame.refreshComplete();
                        break;
                    case NO_DATE:
                        Log.d("MyOrderActivity", "没有数据");
                        if (pageIndex == 1) {
                            runOnUiThread(() -> {
                                ll_no_order.setVisibility(View.VISIBLE);
                            });
                        }
                        mPtrFrame.refreshComplete();
                        break;
                    case PARAM_MISS:
                        Log.d("MyOrderActivity", "参数错误或参数不完整");
                        break;
                    case NO_PERMISSION:
                        Log.d("MyOrderActivity", "没有权限");
                        break;
                    case ERROR:
                        Log.d("MyOrderActivity", "系统异常");
                        break;
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e("MyOrderActivity", e.getMessage());
            }
        });
    }

    @Override
    protected void initData() {
        updateDate();
    }
}
