package yktong.com.godofdog.activity.market;

import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import space.eileen.free_util.DialogOK;
import space.eileen.free_util.GenderRadioView;
import space.eileen.free_util.ProgressDialog;
import yktong.com.godofdog.R;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.NormalResultBean;
import yktong.com.godofdog.bean.QueryGroupBean;
import yktong.com.godofdog.bean.TaskBean;
import yktong.com.godofdog.bean.UserListBean;
import yktong.com.godofdog.manager.DeviceManager;
import yktong.com.godofdog.popup.SecondPickerPopup;
import yktong.com.godofdog.popup.TimePickerPopup;
import yktong.com.godofdog.popup.TokerDeviceSelectPopup;
import yktong.com.godofdog.popup.TokerNumAddPopup;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.value.UrlValue;

import static space.eileen.tools.XString.convertText;
import static space.eileen.tools.XString.isFutureTime;
import static yktong.com.godofdog.activity.market.TokerVerifyContentActivity.tokerVerifyContentId;

public class TokerCommunityActivity extends BaseActivity {

    private LinearLayout ll_qdsj;
    private LinearLayout ll_sjjg;
    private LinearLayout ll_yznr;
    private LinearLayout ll_add_num;
    private LinearLayout ll_sbxz;
    private WindowManager.LayoutParams params;
    private TextView tv_qdsj;
    private TextView tv_sjjg;
    private TextView tv_sbxz;
    private TextView tv_yznr;
    private TextView tv_add_num;
    private int tokerVerifyContent = 0;
    private List<QueryGroupBean.WechatqunBean> wechatqun;
    private ArrayList<UserListBean.UserBean> userBeanList;

    private TextView tvGroupName;
    private TextView sendTaskBtn;
    private String startTime;
    private int seconds = 50;
    private String content;
    private int addNums;
    private int deviceId = -1;
    private String cWqname;
    private int sex = 3;
    private ProgressDialog progressDialog;
    GenderRadioView grv_gender;
    private ProgressDialog progressDialogLoading;
    private boolean loadedUsers, loadedGroup;
    private OptionsPickerView pvOptions;

    @Override
    protected int setLayout() {
        return R.layout.activity_toker_community;
    }

    @Override
    protected void initView() {
        ll_qdsj = bindView(R.id.ll_qdsj);
        ll_sjjg = bindView(R.id.ll_sjjg);
        ll_yznr = bindView(R.id.ll_fwpl);
        ll_sbxz = bindView(R.id.ll_sbxz);
        ll_add_num = bindView(R.id.ll_add_num);
        tv_qdsj = bindView(R.id.tv_qdsj);
        tv_sjjg = bindView(R.id.tv_yxjg);
        tv_sbxz = bindView(R.id.tv_sbxz);
        tv_yznr = bindView(R.id.tv_fwpl);
        TextView tv_male = bindView(R.id.tv_male);
        TextView tv_female = bindView(R.id.tv_female);
        TextView tv_null = bindView(R.id.tv_null);
        tv_add_num = bindView(R.id.tv_yxsl);
        tvGroupName = bindView(R.id.choose_group_tv);
        sendTaskBtn = bindView(R.id.send_task_btn);
        grv_gender = bindView(R.id.grv_gender);

        //启动时间
        ll_qdsj.setOnClickListener(v -> {
            TimePickerPopup timePickerPopup = new TimePickerPopup(TokerCommunityActivity.this, true, (year, month, dayOfMonth, hour, minute) -> {
                startTime = year + "-" + convertText(month) + "-" + convertText(dayOfMonth) + " " + convertText(hour) + ":" + convertText(minute) + ":00";
                try {
                    if (isFutureTime(startTime)) {
                        tv_qdsj.setText(startTime);
                    } else {
                        startTime = "";
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
            timePickerPopup.showAtLocation(findViewById(R.id.ll_qdsj), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            params = getWindow().getAttributes();
            //当弹出Popupwindow时，背景变半透明
            params.alpha = 0.7f;
            getWindow().setAttributes(params);
            //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
            timePickerPopup.setOnDismissListener(() -> {
                params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            });

        });

        //时间间隔
        ll_sjjg.setOnClickListener(v -> {
            SecondPickerPopup timePickerPopup = new SecondPickerPopup(TokerCommunityActivity.this, (second) -> {
                seconds = (second);
                tv_sjjg.setText(seconds + "秒");
            });
            timePickerPopup.showAtLocation(findViewById(R.id.ll_qdsj), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            params = getWindow().getAttributes();
            //当弹出Popupwindow时，背景变半透明
            params.alpha = 0.7f;
            getWindow().setAttributes(params);
            //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
            timePickerPopup.setOnDismissListener(() -> {
                params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            });
        });

        // 验证内容
        ll_yznr.setOnClickListener(v -> {
            Intent intent = new Intent(TokerCommunityActivity.this, TokerVerifyContentActivity.class);
            intent.putExtra(tokerVerifyContentId, tv_yznr.getText().toString());
            startActivityForResult(intent, tokerVerifyContent);
        });

        ll_add_num.setOnClickListener(v -> {
            TokerNumAddPopup numAddPopup = new TokerNumAddPopup(TokerCommunityActivity.this, num -> {
                addNums = num;
                tv_add_num.setText(num + "");
            });
            numAddPopup.showAtLocation(findViewById(R.id.ll_qdsj), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            params = getWindow().getAttributes();
            //当弹出Popupwindow时，背景变半透明
            params.alpha = 0.7f;
            getWindow().setAttributes(params);
            //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
            numAddPopup.setOnDismissListener(() -> {
                params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            });
        });
        // 选择设备
        ll_sbxz.setOnClickListener(v -> {
            TokerDeviceSelectPopup numAddPopup = new TokerDeviceSelectPopup(mContext, userBeanList, false, arg -> {
                if (arg == null || arg.size() == 0) {
                    deviceId = -1;
                    tv_sbxz.setText("");
                } else {
                    deviceId = arg.get(0).getCId();
                    tv_sbxz.setText(arg.get(0).getCName());
                }
                checkGroup(deviceId, MyApp.userId);
            });
            numAddPopup.showAtLocation(findViewById(R.id.ll_qdsj), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            params = getWindow().getAttributes();
            //当弹出Popupwindow时，背景变半透明
            params.alpha = 0.7f;
            getWindow().setAttributes(params);
            //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
            numAddPopup.setOnDismissListener(() -> {
                params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            });
        });

        // 选择群组
        bindView(R.id.choose_group_ll).setOnClickListener(v -> {
            if (pvOptions==null) {
                Toast.makeText(this, "群组为空！", Toast.LENGTH_SHORT).show();
                return;
            }
            pvOptions.show();
//            PikerPopup popup = new PikerPopup(mContext, position -> tvGroupName.setText(wechatqun.get(position).getCWqname()), wechatqun, new CommonAdapter<QueryGroupBean.WechatqunBean>(wechatqun, mContext, R.layout.piker_item) {
//                @Override
//                public void setData(QueryGroupBean.WechatqunBean wechatqunBean, int position, CommonViewHolder viewHolder) {
//                    cWqname = wechatqunBean.getCWqname();
//                    viewHolder.setText(R.id.picker_item_tv, cWqname);
//                }
//            });
//            popup.showAtLocation(findViewById(R.id.choose_group_ll), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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

        //点击事件
        sendTaskBtn.setOnClickListener(v -> {
            if (hasNo()) return;
            if (progressDialog == null)
                progressDialog = new ProgressDialog(this, R.mipmap.loading);
            progressDialog.show();
            TaskBean bean = new TaskBean();
            TaskBean.CtaskBean ctaskBean = new TaskBean.CtaskBean();
            List<TaskBean.CuserBean> cuserBeen = new ArrayList<TaskBean.CuserBean>();
            TaskBean.CuserBean cuserBean = new TaskBean.CuserBean();
            cuserBean.setCId(deviceId);
            cuserBeen.add(cuserBean);
            ctaskBean.setcCheckmage(content);
            ctaskBean.setCSex(getSex());
            ctaskBean.setcWchatqun(cWqname);
            ctaskBean.setCNum(addNums);
            ctaskBean.setCStarttime(startTime);
            ctaskBean.setCTasktype(2);
            ctaskBean.setcIntervaltime(seconds);
            bean.setCtask(ctaskBean);
            bean.setCuser(cuserBeen);
            Gson gson = new Gson();
            String json = gson.toJson(bean);
            Log.d("InteractionRegularActiv", json);
            NetTool.getInstance().postRequest(UrlValue.ADD_TAKS + MyApp.userId, json, NormalResultBean.class, new OnHttpCallBack<NormalResultBean>() {
                @Override
                public void onSuccess(NormalResultBean response) {
                    progressDialog.dismiss();
                    boolean equals = response.getCode().equals("1");
                    if (equals) {
//                        Log.d("InteractionRegularActiv", json);
//                        Toast.makeText(mContext, "任务添加成功", Toast.LENGTH_SHORT).show();
//                        finish();
                        new DialogOK(TokerCommunityActivity.this, "任务添加成功", () -> {
                            finish();
                        }).show();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    progressDialog.dismiss();
                }
            });

        });


    }

    private int getSex() {
        switch (grv_gender.getGenderChecked()) {
            case male:
                sex = 1;
                return sex;
            case female:
                sex = 2;
                return sex;
            case noGender:
                sex = 3;
                return sex;
            default:
                return 1;
        }
    }

    private boolean hasNo() {
        if (tv_sbxz.getText().toString().trim().length() == 0 ||
                startTime == null || startTime.trim().length() == 0 ||
                tvGroupName.getText().toString().trim().length() == 0 ||
                tv_add_num.getText().toString().trim().length() == 0 ||
                tv_sjjg.getText().toString().trim().length() == 0 || deviceId == -1) {
            Toast.makeText(this, "不能有空", Toast.LENGTH_SHORT).show();
            return true;
        }


        return false;
    }

    @Override
    protected void initData() {
        if (progressDialogLoading == null)
            progressDialogLoading = new ProgressDialog(this, R.mipmap.loading);
        progressDialogLoading.show();
        userBeanList = DeviceManager.getInstance().getDevices();
        NetTool.getInstance().startRequest("get", UrlValue.SELECT_USER + MyApp.companyDd + UrlValue.FANS_MANAGER_PARAM_USER_ID + MyApp.userId, UserListBean.class, new OnHttpCallBack<UserListBean>() {
            @Override
            public void onSuccess(UserListBean response) {
                loadedUsers = true;
                hiddenLoading();
                if (response.getCode().equals("1")) {

                    userBeanList = (ArrayList<UserListBean.UserBean>) response.getUser();
                    DeviceManager.getInstance().saveDeviceBeanList(userBeanList);
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });
        checkGroup(-1, MyApp.userId);
    }

    private void hiddenLoading() {
        if (loadedGroup && loadedUsers && progressDialogLoading != null)
            progressDialogLoading.dismiss();
    }

    private void checkGroup(int userId, int deafut) {
        tvGroupName.setText("");
        cWqname=null;
        progressDialogLoading.show();
        if (userId == -1) userId = deafut;
        NetTool.getInstance().startRequest("get", UrlValue.QUERY_GROUP + userId, QueryGroupBean.class, new OnHttpCallBack<QueryGroupBean>() {
            @Override
            public void onSuccess(QueryGroupBean response) {
                loadedGroup = true;
                hiddenLoading();
                if (response.getCode().equals("1")) {
                    wechatqun = response.getWechatqun();
                    resetGroup();
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void resetGroup() {
        if (wechatqun == null) {
            pvOptions = null;
            return;
        }
        List<String> groups = new ArrayList<>();
        for (int i = 0; i < wechatqun.size(); i++) {
            groups.add(wechatqun.get(i).getCWqname());
        }

        pvOptions = new OptionsPickerView.Builder(this, (options1, option2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            String tx = wechatqun.get(options1).getCWqname();
            cWqname = tx;
            tvGroupName.setText(tx);
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
        pvOptions.setPicker(groups);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            try {
                content = data.getStringExtra(tokerVerifyContentId);
                tv_yznr.setText(content);
            } catch (NullPointerException npe) {
            }
        }
    }
}
