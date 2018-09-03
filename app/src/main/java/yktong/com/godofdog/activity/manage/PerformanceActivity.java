package yktong.com.godofdog.activity.manage;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;

import java.util.ArrayList;
import java.util.List;

import space.eileen.free_util.ProgressDialog;
import yktong.com.godofdog.R;
import yktong.com.godofdog.activity.MyOrderActivity;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.PerformanceDateBean;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.tool.view.CommonAdapter;
import yktong.com.godofdog.tool.view.CommonViewHolder;
import yktong.com.godofdog.util.SortList;
import yktong.com.godofdog.value.UrlValue;

/**
 * Created by vampire on 2017/7/5.
 */

public class PerformanceActivity extends BaseActivity {

    private final String DEFAULT = "无";
    private final String INCREASE = "正序";
    private final String REDUCE = "倒序";
    private PerformanceDateBean.OrderManagerIndexBean orderManagerIndex;
    private WindowManager.LayoutParams params;
    private TextView performanceSumTv;
    private TextView performanceDayTv;
    private TextView performanceFansTv;
    private TextView performanceChatTv;
    private TextView performanceAvgSumTv;
    private TextView performanceAvgDay;
    private TextView performanceAvgFans;
    private TextView performanceAvgChatTv;
    private ListView salilerInfoLv;
    private int deptId = -1;
    private LinearLayout[] layouts;
    private ImageView[] imageViews;
    private CommonAdapter<PerformanceDateBean.OrderManagerIndexBean.PerformanceBean> adapter;
    private OptionsPickerView pvOptions;
    private TextView deptNameTV;
    private ProgressDialog progressDialogLoading;

    @Override
    protected int setLayout() {
        return R.layout.activity_perfrormance;
    }

    @Override
    protected void initView() {
        RelativeLayout chooseDeptRl = bindView(R.id.choose_dept_rl);
        deptNameTV = bindView(R.id.dept_name_tv);
        chooseDeptRl.setOnClickListener(v -> {
            if (orderManagerIndex==null){
                Toast.makeText(this,"没有权限",Toast.LENGTH_SHORT).show();
                return;}
//            if (orderManagerIndex.getDept()==null||orderManagerIndex.getDept().size()==0)return;
//            PikerPopup popup = new PikerPopup(mContext, position -> {
//                if (orderManagerIndex.getDept()==null||orderManagerIndex.getDept().size()==0)return;
//                deptNameTV.setText(orderManagerIndex.getDept().get(position).getCDeptname().toString());
//                requestData(orderManagerIndex.getDept().get(position).getCDeptid() + "");
//
//            }, orderManagerIndex.getDept(),
//                    new CommonAdapter<PerformanceDateBean.OrderManagerIndexBean.DeptBean>(orderManagerIndex.getDept(), mContext, R.layout.piker_item) {
//                        @Override
//                        public void setData(PerformanceDateBean.OrderManagerIndexBean.DeptBean deptBean, int position, CommonViewHolder viewHolder) {
//                            Log.d("PerformanceActivity", deptBean.toString());
//                            Log.d("PerformanceActivity", "deptBean.getCDeptname()==null:" + (deptBean.getCDeptname() == null));
//                            viewHolder.setText(R.id.picker_item_tv, deptBean.getCDeptname());
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
            if (pvOptions!=null)  pvOptions.show();
        });

        performanceSumTv = bindView(R.id.performance_sum_performance);
        performanceDayTv = bindView(R.id.performance_sum_day_add);
        performanceFansTv = bindView(R.id.performance_sum_fans);
        performanceChatTv = bindView(R.id.performance_sum_chat);

        performanceAvgSumTv = bindView(R.id.performance_avg_performance);
        performanceAvgDay = bindView(R.id.performance_avg_friend);
        performanceAvgFans = bindView(R.id.performance_avg_fans);
        performanceAvgChatTv = bindView(R.id.performance_avg_chat);

        salilerInfoLv = bindView(R.id.fans_manage_lv);

        LinearLayout sortByPerformanceLl = bindView(R.id.fans_total_fans_ll);
        sortByPerformanceLl.setContentDescription(INCREASE);
        LinearLayout sortByDayPerformanceLl = bindView(R.id.sort_by_day_performance_ll);
        sortByDayPerformanceLl.setContentDescription(DEFAULT);
        LinearLayout sortByTotalFansLl = bindView(R.id.sort_by_fans_ll);
        sortByTotalFansLl.setContentDescription(DEFAULT);
        LinearLayout sortByDayChatLl = bindView(R.id.sort_by_chat_day);
        sortByDayChatLl.setContentDescription(DEFAULT);

        ImageView performanceSumIv = bindView(R.id.performance_sum_iv);
        ImageView performanceDayIv = bindView(R.id.performance_day_iv);
        ImageView fansIv = bindView(R.id.sort_fans_iv);
        ImageView chatIv = bindView(R.id.sort_chat_iv);

        TextView tv_add = bindView(R.id.tv_add);
        tv_add.setOnClickListener(v -> startActivity(new Intent(this, PayManageActivity.class)));

        layouts = new LinearLayout[]{sortByPerformanceLl, sortByDayPerformanceLl, sortByTotalFansLl, sortByDayChatLl};
        imageViews = new ImageView[]{performanceSumIv, performanceDayIv, fansIv, chatIv};
        for (int i = 0; i < layouts.length; i++) {
            int finalI = i;
            layouts[i].setOnClickListener(v -> {
                Log.d("FansManageActivity", "click");
                sortList(orderManagerIndex.getPerformance(), layouts, finalI);
            });
        }

    }

    private void sortList(List<PerformanceDateBean.OrderManagerIndexBean.PerformanceBean> been,
                          LinearLayout[] layouts, int i) {
        String des = (String) layouts[i].getContentDescription();
        SortList<PerformanceDateBean.OrderManagerIndexBean.PerformanceBean> sortList = new SortList<>();
        switch (i) {
            case 0:
                if (des.equals(INCREASE)) {                            //递增
                    sortList.Sort(been, "getMonthamount", "desc");
                } else {
                    sortList.Sort(been, "getMonthamount", null);
                }
                break;
            case 1:
                if (des.equals(INCREASE)) {                            //递增
                    sortList.Sort(been, "getTodayamount", "desc");
                } else {
                    sortList.Sort(been, "getTodayamount", null);
                }
                break;
            case 2:
                if (des.equals(INCREASE)) {                            //递增
                    sortList.Sort(been, "getCountfriend", "desc");
                } else {
                    sortList.Sort(been, "getCountfriend", null);
                }
                break;
            case 3:
                if (des.equals(INCREASE)) {                            //递增
                    sortList.Sort(been, "getTodaychat", "desc");
                } else {
                    sortList.Sort(been, "getTodaychat", null);
                }
                break;
        }
        responseUtil(i, des);
        for (PerformanceDateBean.OrderManagerIndexBean.PerformanceBean performanceBean : been) {
            Log.d("PerformanceActivity", performanceBean.toString());
        }
        adapter.setList(been);


    }

    private void responseUtil(int j, String des) {
        for (int i = 0; i < imageViews.length; i++) {
            if (i == j) {
                if (des.equals(INCREASE)) {
                    layouts[i].setContentDescription(REDUCE);
                    imageViews[i].setImageResource(R.mipmap.icon_px_down);
                } else {
                    imageViews[i].setImageResource(R.mipmap.icon_px_up);
                    layouts[i].setContentDescription(INCREASE);
                }
            } else {
                imageViews[i].setImageResource(R.mipmap.icon_px_def);
                layouts[i].setContentDescription(DEFAULT);
            }
        }
    }
    private void resetDeptView() {
        if (orderManagerIndex.getDept()==null||orderManagerIndex.getDept().size()==0)return;

        List<String> options=new ArrayList<>();
        String all="全部";
        options.add(all);
        for (PerformanceDateBean.OrderManagerIndexBean.DeptBean deptBean:orderManagerIndex.getDept()){
            options.add(deptBean.getCDeptname());
        }

        pvOptions = new OptionsPickerView.Builder(this, (options1, option2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            String tx =options1==0?all:orderManagerIndex.getDept().get(options1-1).getCDeptname();
          String deptId=options1==0?null: orderManagerIndex.getDept().get(options1-1).getCDeptid()+"";
            deptNameTV.setText(tx);
            requestData(deptId);
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
    @Override
    protected void initData() {
        requestData(null);
    }

    private void requestData(String deptId) {
        String url = UrlValue.PERFORMANCE_MANGER + MyApp.companyDd;
        if (deptId != null)
            url = url + UrlValue.DEPT_ID + deptId;
        if (progressDialogLoading == null)
            progressDialogLoading = new ProgressDialog(this, R.mipmap.loading);
        progressDialogLoading.show();
        NetTool.getInstance().startRequest("get", url + UrlValue.FANS_MANAGER_PARAM_USER_ID + MyApp.userId, PerformanceDateBean.class, new OnHttpCallBack<PerformanceDateBean>() {
            @Override
            public void onSuccess(PerformanceDateBean response) {
                if (progressDialogLoading!=null)progressDialogLoading.dismiss();
                if (response.getCode().equals("1")) {
                    orderManagerIndex = response.getOrderManagerIndex();
                    resetDeptView();
                    PerformanceDateBean.OrderManagerIndexBean.SumPerformanceBean sumPerformance = orderManagerIndex.getSumPerformance();
                    PerformanceDateBean.OrderManagerIndexBean.AvgPerformanceBean avgPerformance = orderManagerIndex.getAvgPerformance();
                    performanceSumTv.setText(sumPerformance.getSummonthamount() + "");
                    performanceDayTv.setText(sumPerformance.getSumtodayamount() + "");
                    performanceFansTv.setText(sumPerformance.getSumcountfriend() + "");
                    performanceChatTv.setText(sumPerformance.getSumtodaychat() + "");
                    int avgmonthamount = (int) avgPerformance.getAvgmonthamount();
                    performanceAvgSumTv.setText(avgmonthamount + "");
                    int avgDay = (int) avgPerformance.getAvgtodayamount();
                    performanceAvgDay.setText(avgDay + "");
                    int avgcountfriend = avgPerformance.getAvgcountfriend();
                    performanceAvgFans.setText(avgcountfriend + "");
                    int avgtodaychat = avgPerformance.getAvgtodaychat();
                    performanceAvgChatTv.setText(avgtodaychat + "");
                    adapter = new CommonAdapter<PerformanceDateBean.OrderManagerIndexBean.PerformanceBean>(
                            orderManagerIndex.getPerformance(), mContext, R.layout.fans_item) {
                        @Override
                        public void setData(PerformanceDateBean.OrderManagerIndexBean.PerformanceBean performanceBean, int position, CommonViewHolder viewHolder) {
                            viewHolder.getConvertView().setBackgroundColor(getResources().getColor((position % 2 == 0 ? R.color.content_split_gray : R.color.theme_white)));

                            viewHolder.getView(R.id.iv_hg).setVisibility(position == 0 ? View.VISIBLE : View.GONE);
                            TextView textView = viewHolder.getView(R.id.item_salesman_name_tv);
                            textView.setText(performanceBean.getUsername());
                            textView.setTextColor(getResources().getColor(performanceBean.getUserid() == MyApp.userId ? R.color.text_green_light : R.color.text_black));
//                            viewHolder.setText(R.id.item_salesman_name_tv, performanceBean.getUsername());
                            viewHolder.setText(R.id.item_fans_all_fans, performanceBean.getMonthamount() + "");
                            viewHolder.setText(R.id.item_fans_day_fans, performanceBean.getTodayamount() + "");
                            viewHolder.setText(R.id.item_fans_sns_fans, performanceBean.getCountfriend() + "");
                            viewHolder.setText(R.id.item_fans_chat_fans, performanceBean.getTodaychat() + "");
                        }

                    };
                    salilerInfoLv.setAdapter(adapter);
                    salilerInfoLv.setOnItemClickListener((parent, view, position, id) -> {
                        int userid = response.getOrderManagerIndex().getPerformance().get(position).getUserid();
                        String userName = response.getOrderManagerIndex().getPerformance().get(position).getUsername();
                        Intent intent = new Intent(PerformanceActivity.this, MyOrderActivity.class);
                        intent.putExtra(MyOrderActivity.USER_ID, userid);
                        intent.putExtra(MyOrderActivity.USER_NAME, userName);
                        startActivity(intent);
                    });
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }
}
