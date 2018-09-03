package yktong.com.godofdog.activity.manage.tel;

import android.content.Intent;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;

import java.util.ArrayList;
import java.util.List;

import space.eileen.free_util.ProgressDialog;
import yktong.com.godofdog.R;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.tel.TelTotalBean;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.tool.view.CommonAdapter;
import yktong.com.godofdog.tool.view.CommonViewHolder;
import yktong.com.godofdog.util.SortList;
import yktong.com.godofdog.util.time.DateUtils;
import yktong.com.godofdog.value.UrlValue;

/**
 * Created by vampire on 2017/8/17.
 */

public class TelManagerActivity extends BaseActivity {
    private final static String NORMAL = "normal";
    private final static String SEQUENTIAL = "sequential";
    private final static String INVERTED_ORDER = "invertedOrder";
    //部门列表
    private List<TelTotalBean.UserDeptCommunicateDataBean.CDeptBean> cDeptBeanList;
    //通话简列
    private List<TelTotalBean.UserDeptCommunicateDataBean.UserCommunicateDataBean> communicateDataBeanList = new ArrayList<>();
    private ListView telManageLv;
    private CommonAdapter<TelTotalBean.UserDeptCommunicateDataBean.UserCommunicateDataBean> adapter;
    private LinearLayout sortByTelLl;
    private LinearLayout sortByTimesLl;
    private LinearLayout[] layouts;
    private TextView deptNameTv;
    private RelativeLayout chooseDeptRl;
    private WindowManager.LayoutParams params;
    private OptionsPickerView pvOptions;
    private ProgressDialog progressDialogLoading;

    @Override
    protected int setLayout() {
        return R.layout.activity_manage_tel;
    }

    @Override
    protected void initView() {
        //选择部门
        chooseDeptRl = bindView(R.id.choose_dept_rl);
        //通话时长
        sortByTelLl = bindView(R.id.sort_by_tel_time);
        sortByTelLl.setTag(SEQUENTIAL);
        //通话次数
        sortByTimesLl = bindView(R.id.sort_by_tel_times);
        sortByTimesLl.setTag(NORMAL);
        layouts = new LinearLayout[]{sortByTelLl, sortByTimesLl};
        telManageLv = bindView(R.id.tel_manage_lv);
        //部门名称
        deptNameTv = bindView(R.id.dept_name_tv);
        adapter = new CommonAdapter<TelTotalBean.UserDeptCommunicateDataBean.UserCommunicateDataBean>(communicateDataBeanList, mContext, R.layout.tel_manage_item) {
            @Override
            public void setData(TelTotalBean.UserDeptCommunicateDataBean.UserCommunicateDataBean telTotalBean, int position, CommonViewHolder viewHolder) {
//                Integer tag = (Integer) viewHolder.getView(R.id.item_rl).getTag();
//                if (tag == null) {
//                    viewHolder.getView(R.id.item_rl).setTag(position % 2);
//                    tag = position % 2;
//                }
//                if (tag  == 0) {
//                    LinearLayout relativeLayout = viewHolder.getView(R.id.item_rl);
//                    relativeLayout.setBackgroundResource(R.drawable.bac_drawable);
//                }
                viewHolder.getConvertView().setBackgroundColor(getResources().getColor(position % 2 == 0 ? R.color.content_split_gray : R.color.theme_white));
                Log.d("TelManagerActivity", telTotalBean.getCName());
                viewHolder.setText(R.id.item_user_name, telTotalBean.getCName());
                viewHolder.setText(R.id.item_call_time_tv, DateUtils.changeSec(telTotalBean.getSumCommunicateduration()) + "");
                viewHolder.setText(R.id.item_call_times_tv, telTotalBean.getCountNum() + "");
            }
        };

        telManageLv.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(mContext, TelUserActivity.class);
            intent.putExtra("USER_ID", communicateDataBeanList.get(position).getCId());
            startActivity(intent);
        });
        adapter.setList(communicateDataBeanList);
        telManageLv.setAdapter(adapter);
        telManageLv.setDividerHeight(0);

    }

    @Override
    protected void initData() {
        getDate(-1);
        ImageView imageViewCall = bindView(R.id.sort_call_time);
        ImageView imageViewCallTimes = bindView(R.id.sort_call_times);
        // 通话时间排序
        sortByTelLl.setOnClickListener(v -> {
            sortByTimesLl.setTag(NORMAL);
            String tag = (String) sortByTelLl.getTag();
            imageViewCallTimes.setImageResource(R.mipmap.icon_px_def);
            if (tag.equals(SEQUENTIAL)) {
                imageViewCall.setImageResource(R.mipmap.icon_px_up);
                sortList(INVERTED_ORDER, 0);
            } else {
                imageViewCall.setImageResource(R.mipmap.icon_px_down);
                sortList(SEQUENTIAL, 0);
            }
        });

        //通话次数排序
        sortByTimesLl.setOnClickListener(v -> {
            sortByTelLl.setTag(NORMAL);
            String tag = (String) sortByTimesLl.getTag();
            imageViewCall.setImageResource(R.mipmap.icon_px_def);
            if (tag.equals(SEQUENTIAL)) {
                imageViewCallTimes.setImageResource(R.mipmap.icon_px_up);
                sortList(INVERTED_ORDER, 1);
            } else {
                imageViewCallTimes.setImageResource(R.mipmap.icon_px_down);
                sortList(SEQUENTIAL, 1);
            }
        });

        chooseDeptRl.setOnClickListener(v -> {
//            PikerPopup popup = new PikerPopup(mContext, position -> {
//                getDate(cDeptBeanList.get(position).getCDeptid());
//                deptNameTv.setText(cDeptBeanList.get(position).getCDeptname());
//            }, cDeptBeanList, new CommonAdapter<TelTotalBean.UserDeptCommunicateDataBean.CDeptBean>(cDeptBeanList, mContext, R.layout.piker_item) {
//                @Override
//                public void setData(TelTotalBean.UserDeptCommunicateDataBean.CDeptBean cDeptBean, int position, CommonViewHolder viewHolder) {
//                    viewHolder.setText(R.id.picker_item_tv, cDeptBean.getCDeptname());
//                }
//            });
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


    }
    private void resetDeptView() {
        List<String> options=new ArrayList<>();
        String all="全部";
        options.add(all);
        for (TelTotalBean.UserDeptCommunicateDataBean.CDeptBean deptBean:cDeptBeanList){
            options.add(deptBean.getCDeptname());
        }

        pvOptions = new OptionsPickerView.Builder(this, (options1, option2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            String tx =options1==0?all:cDeptBeanList.get(options1-1).getCDeptname();
            int dept =options1==0?-1: cDeptBeanList.get(options1-1).getCDeptid();
            deptNameTv.setText(tx);
            getDate(dept);
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
    private void getDate(int dept) {
        String url = UrlValue.SELECT_LINK_DATE + MyApp.companyDd;
        if (dept != -1) {
            url += ("&cUdeptid=" + dept);
        }
        if (progressDialogLoading == null)
            progressDialogLoading = new ProgressDialog(this, R.mipmap.loading);
        progressDialogLoading.show();
        NetTool.getInstance().startRequest("get", url + UrlValue.FANS_MANAGER_PARAM_USER_ID + MyApp.userId, TelTotalBean.class, new OnHttpCallBack<TelTotalBean>() {
            @Override
            public void onSuccess(TelTotalBean response) {
                if (progressDialogLoading!=null)progressDialogLoading.dismiss();
                Log.d("TelManagerActivity", response.getCode());
                if (response.getCode().equals("1")) {

                    cDeptBeanList = response.getUserDeptCommunicateData().getCDept();
//                    TelTotalBean.UserDeptCommunicateDataBean.CDeptBean bean = new TelTotalBean.UserDeptCommunicateDataBean.CDeptBean();
//                    bean.setCDcompanyid(MyApp.companyDd);
//                    bean.setCDeptid(-1);
//                    bean.setCDeptname("全部部门");
//                    cDeptBeanList.add(0, bean);
                    resetDeptView();
                    communicateDataBeanList = response.getUserDeptCommunicateData().getUserCommunicateData();
                    Log.d("TelManagerActivity", "communicateDataBeanList.size():" + communicateDataBeanList.size());
                    adapter.setList(communicateDataBeanList);
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void sortList(String invertedOrder, int i) {
        SortList<TelTotalBean.UserDeptCommunicateDataBean.UserCommunicateDataBean> sortList = new SortList<>();
        layouts[i].setTag(invertedOrder);
        switch (i) {
            case 0:
                if (invertedOrder.equals(SEQUENTIAL)) {
                    sortList.Sort(communicateDataBeanList, "getSumCommunicateduration", "desc");
                } else {
                    sortList.Sort(communicateDataBeanList, "getSumCommunicateduration", null);
                }
                break;
            case 1:
                if (invertedOrder.equals(SEQUENTIAL)) {
                    sortList.Sort(communicateDataBeanList, "getCountNum", "desc");
                } else {
                    sortList.Sort(communicateDataBeanList, "getCountNum", null);
                }
                break;
        }
        adapter.setList(communicateDataBeanList);

    }
}
