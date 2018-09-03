package yktong.com.godofdog.activity.manage;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;

import java.util.ArrayList;
import java.util.List;

import space.eileen.free_util.ProgressDialog;
import yktong.com.godofdog.R;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.FansManageBean;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.tool.view.CommonAdapter;
import yktong.com.godofdog.tool.view.CommonViewHolder;
import yktong.com.godofdog.util.SortList;
import yktong.com.godofdog.value.UrlValue;

/**
 * Created by vampire on 2017/7/5.
 */

public class FansManageActivity extends BaseActivity {

    private final String DEFAULT = "无";
    private final String INCREASE = "正序";
    private final String REDUCE = "倒序";
    private ArrayList<FansManageBean.CountFansBean.SelectFansDataBean> been;
    private ListView listView;
    private CommonAdapter<FansManageBean.CountFansBean.SelectFansDataBean> adapter;
    private ImageView totalSortIv;
    private ImageView daySortIv;
    private ImageView snsSortIv;
    private ImageView chatSortIv;
    private ImageView[] imageViews;
    private FansManageBean.CountFansBean.SelectFansDataBean[] list;
    private LinearLayout[] layouts;
    private FansManageBean.CountFansBean.DeptBean selectedDept;
    private List<FansManageBean.CountFansBean.DeptBean> deptBeanList;
    private WindowManager.LayoutParams params;
    private TextView allFanstv;
    private TextView fansAddtv;
    private TextView snstv;
    private TextView chattv;
    private TextView avgFanstv;
    private TextView avgFriendtv;
    private TextView avgSnsTv;
    private TextView avgChatTv;
    private TextView tv_bmxz;
    private OptionsPickerView pvOptions;
    private ProgressDialog progressDialogLoading;

    @Override
    protected int setLayout() {
        return R.layout.activity_fans_manage;
    }

    @Override
    protected void initView() {
        listView = bindView(R.id.fans_manage_lv);
        allFanstv = bindView(R.id.fans_sum_fans);
        fansAddtv = bindView(R.id.fans_sum_friend_add);
        snstv = bindView(R.id.fans_sum_sns);
        chattv = bindView(R.id.fans_sum_chat);
        avgFanstv = bindView(R.id.fans_avg_fans);
        avgFriendtv = bindView(R.id.fans_avg_friend);
        avgSnsTv = bindView(R.id.fans_avg_sns);
        avgChatTv = bindView(R.id.fans_avg_chat);
        tv_bmxz = bindView(R.id.tv_bmxz);
        LinearLayout ll_bmxz = bindView(R.id.ll_bmxz);

        if (progressDialogLoading == null)
            progressDialogLoading = new ProgressDialog(this, R.mipmap.loading);
        progressDialogLoading.show();
        NetTool.getInstance().startRequest("get",
                UrlValue.FANS_MANGER + MyApp.companyDd +
                        UrlValue.FANS_MANAGER_PARAM_USER_ID + MyApp.userId,
                FansManageBean.class, new OnHttpCallBack<FansManageBean>() {
                    @Override
                    public void onSuccess(FansManageBean response) {
                        if (progressDialogLoading!=null)progressDialogLoading.dismiss();

                        if (response.getCode().equals("4")) {
                            Toast.makeText(FansManageActivity.this, "没有权限", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!response.getCode().equals("1")) return;
                        FansManageBean.CountFansBean.SumFansDataBean sumFansData = response.getCountFans().getSumFansData();
                        if (sumFansData == null) return;

                        allFanstv.setText(sumFansData.getSumcountfriend() + "");
                        fansAddtv.setText(sumFansData.getSumtodayfriend() + "");
                        snstv.setText(sumFansData.getSumtodayquan() + "");
                        chattv.setText(sumFansData.getSumtodaychat() + "");
                        FansManageBean.CountFansBean.AvgFansDataBean avgFansData = response.getCountFans().getAvgFansData();
                        int avgcountfriend = (int) avgFansData.getAvgcountfriend();
                        avgFanstv.setText(avgcountfriend + "");
                        int avgtodayfriend = (int) avgFansData.getAvgtodayfriend();
                        avgFriendtv.setText(avgtodayfriend + "");
                        int avgtodayquan = (int) avgFansData.getAvgtodayquan();
                        avgSnsTv.setText(avgtodayquan + "");
                        int avgtodaychat = (int) avgFansData.getAvgtodaychat();
                        avgChatTv.setText(avgtodaychat + "");
                        been = (ArrayList<FansManageBean.CountFansBean.SelectFansDataBean>) response.getCountFans().getSelectFansData();
                        deptBeanList = response.getCountFans().getDept();
                        resetDeptView();
                        adapter.setList(been);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

        LinearLayout fansTotalLayout = bindView(R.id.fans_total_fans_ll);
        fansTotalLayout.setContentDescription(INCREASE);
        LinearLayout fansDayLayout = bindView(R.id.fans_day_fans);
        fansDayLayout.setContentDescription(DEFAULT);
        LinearLayout snsDayLayout = bindView(R.id.fans_sns_day);
        snsDayLayout.setContentDescription(DEFAULT);
        LinearLayout chatDayLayout = bindView(R.id.fans_chat_day);
        chatDayLayout.setContentDescription(DEFAULT);
        layouts = new LinearLayout[]{fansTotalLayout, fansDayLayout, snsDayLayout, chatDayLayout};

        totalSortIv = bindView(R.id.fans_sort_fans);
        daySortIv = bindView(R.id.fans_sort_day);
        snsSortIv = bindView(R.id.fans_sort_sns);
        chatSortIv = bindView(R.id.fans_sort_chat);
        imageViews = new ImageView[]{totalSortIv, daySortIv, snsSortIv, chatSortIv};

        for (int i = 0; i < layouts.length; i++) {
            int finalI = i;
            layouts[i].setOnClickListener(v -> {
                Log.d("FansManageActivity", "click");
                sortList(been, layouts, finalI);
            });
        }
        ll_bmxz.setOnClickListener(v -> {
            if (pvOptions!=null)  pvOptions.show();

//
//            PikerPopup popup = new PikerPopup(mContext, position -> {
////                if (selectedDept==null||selectedDept.size()==0)return;
//                selectedDept = deptBeanList.get(position);
//                tv_bmxz.setText(selectedDept.getCDeptname());
//                updateFans(selectedDept);
//
//            }, deptBeanList, new CommonAdapter<FansManageBean.CountFansBean.DeptBean>(deptBeanList, mContext, R.layout.piker_item) {
//                @Override
//                public void setData(FansManageBean.CountFansBean.DeptBean deptBean, int position, CommonViewHolder viewHolder) {
//                    viewHolder.setText(R.id.picker_item_tv, deptBean.getCDeptname());
//                }
//            });
//            popup.showAtLocation(findViewById(R.id.tv_bmxz), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
    }

    private void updateFans(FansManageBean.CountFansBean.DeptBean selectedDept) {
        if (progressDialogLoading == null)
            progressDialogLoading = new ProgressDialog(this, R.mipmap.loading);
        progressDialogLoading.show();
        NetTool.getInstance().startRequest("get", UrlValue.FANS_MANGER + MyApp.companyDd +
                        UrlValue.FANS_MANAGER_PARAM_USER_ID + MyApp.userId +
                        (selectedDept==null?"": UrlValue.FANS_MANAGER_PARAM_DEPT + selectedDept.getCDeptid()),
                FansManageBean.class, new OnHttpCallBack<FansManageBean>() {
                    @Override
                    public void onSuccess(FansManageBean response) {
                        if (progressDialogLoading!=null)progressDialogLoading.dismiss();
                        if (response.getCode().equals("4")) {
                            Toast.makeText(FansManageActivity.this, "没有权限", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!response.getCode().equals("1")) return;
                        FansManageBean.CountFansBean.SumFansDataBean sumFansData = response.getCountFans().getSumFansData();

                        runOnUiThread(() -> {
                            String allFans = 0 + "";
                            String fansAdd = 0 + "";
                            String sns = 0 + "";
                            String chat = 0 + "";
                            String avgFans = 0 + "";
                            String avgFriend = 0 + "";
                            String avgSns = 0 + "";
                            String avgChat = 0 + "";
                            FansManageBean.CountFansBean.AvgFansDataBean avgFansData = response.getCountFans().getAvgFansData();
                            if (sumFansData != null) {
                                allFans = sumFansData.getSumcountfriend() + "";
                                fansAdd = sumFansData.getSumtodayfriend() + "";
                                sns = sumFansData.getSumtodayquan() + "";
                                chat = sumFansData.getSumtodaychat() + "";

                                avgFans = avgFansData.getAvgcountfriend() + "";
                                avgFriend = avgFansData.getAvgtodayfriend() + "";
                                avgSns = avgFansData.getAvgtodayquan() + "";
                                avgChat = avgFansData.getAvgtodaychat() + "";
                            }
                            allFanstv.setText(allFans);
                            fansAddtv.setText(fansAdd);
                            snstv.setText(sns);
                            chattv.setText(chat);
                            avgFanstv.setText(avgFans);
                            avgFriendtv.setText(avgFriend);
                            avgSnsTv.setText(avgSns);
                            avgChatTv.setText(avgChat);
                            been = (ArrayList<FansManageBean.CountFansBean.SelectFansDataBean>) response.getCountFans().getSelectFansData();
                            deptBeanList = response.getCountFans().getDept();
                            resetDeptView();
                            adapter.setList(been);
                        });
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void resetDeptView() {

        List<String> options=new ArrayList<>();
        String all="全部";
        options.add(all);
        for (FansManageBean.CountFansBean.DeptBean deptBean:deptBeanList){
            options.add(deptBean.getCDeptname());
        }

        pvOptions = new OptionsPickerView.Builder(this, (options1, option2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            String tx =options1==0?all:deptBeanList.get(options1-1).getCDeptname();
            selectedDept =options1==0?null: deptBeanList.get(options1-1);
            tv_bmxz.setText(tx);
            updateFans(selectedDept);
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

    private void sortList(ArrayList<FansManageBean.CountFansBean.SelectFansDataBean> been, LinearLayout[] layouts, int finalI) {
        String tag = (String) layouts[finalI].getContentDescription();
        SortList<FansManageBean.CountFansBean.SelectFansDataBean> list = new SortList<>();
        switch (finalI) {
            case 0:
                if (tag.equals(INCREASE)) {                            //递增
                    list.Sort(been, "getCountfriend", "desc");
                } else {
                    list.Sort(been, "getCountfriend", null);
                }
                break;
            case 1:
                if (tag.equals(INCREASE)) {                            //递增
                    list.Sort(been, "getTodayfriend", "desc");
                } else {
                    list.Sort(been, "getTodayfriend", null);
                }
                break;
            case 2:
                if (tag.equals(INCREASE)) {                            //递增
                    list.Sort(been, "getTodayquan", "desc");
                } else {
                    list.Sort(been, "getTodayquan", null);
                }
                break;
            case 3:
                if (tag.equals(INCREASE)) {                            //递增
                    list.Sort(been, "getTodaychat", "desc");
                } else {
                    list.Sort(been, "getTodaychat", null);
                }
                break;
        }
        responseUtil(finalI, tag);
        adapter.setList(been);
    }

    private void responseUtil(int finalI, String tag) {
        for (int i = 0; i < imageViews.length; i++) {
            if (i == finalI) {
                if (tag.equals(INCREASE)) {
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

    @Override
    protected void initData() {
        been = new ArrayList<>();
        adapter = new CommonAdapter<FansManageBean.CountFansBean.SelectFansDataBean>(been, mContext, R.layout.fans_item) {
            @Override
            public void setData(FansManageBean.CountFansBean.SelectFansDataBean selectFansDataBean, int position, CommonViewHolder viewHolder) {
                viewHolder.getConvertView().setBackgroundColor(getResources().getColor((position % 2 == 0 ? R.color.content_split_gray : R.color.theme_white)));

                viewHolder.getView(R.id.iv_hg).setVisibility(position == 0 ? View.VISIBLE : View.GONE);
                TextView textView = viewHolder.getView(R.id.item_salesman_name_tv);
                textView.setText(selectFansDataBean.getUsername());
                textView.setTextColor(getResources().getColor(selectFansDataBean.getcId() == MyApp.userId ? R.color.text_green_light : R.color.text_black));

//                viewHolder.getView(R.id.iv_hg).setVisibility(selectFansDataBean.getcId() == MyApp.userId ? View.VISIBLE : View.GONE);
//                viewHolder.setText(R.id.item_salesman_name_tv, selectFansDataBean.getUsername());
                viewHolder.setText(R.id.item_fans_all_fans, selectFansDataBean.getCountfriend() + "");
                viewHolder.setText(R.id.item_fans_day_fans, selectFansDataBean.getTodayfriend() + "");
                viewHolder.setText(R.id.item_fans_sns_fans, selectFansDataBean.getTodayquan() + "");
                viewHolder.setText(R.id.item_fans_chat_fans, selectFansDataBean.getTodaychat() + "");
            }
        };
        listView.setAdapter(adapter);
    }
}
