package yktong.com.godofdog.activity.market;

import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import space.eileen.free_util.DialogOK;
import space.eileen.free_util.ProgressDialog;
import yktong.com.godofdog.R;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.NormalResultBean;
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
import static yktong.com.godofdog.activity.market.InteractionCommentActivity.InteractionCommentContentId;

public class InteractionRegularActivity extends BaseActivity {
    LinearLayout ll_qdsj;
    LinearLayout ll_sjjg;
    LinearLayout ll_yznr;
    LinearLayout ll_add_num;
    LinearLayout ll_sbxz;
    private WindowManager.LayoutParams params;
    private TextView tv_qdsj;
    private TextView tv_sjjg;
    private TextView tv_plnr;
    private TextView tv_sbxz;
    private TextView tv_add_num;
    private int tokerVerifyContent = 1;
    private ArrayList<UserListBean.UserBean> userBeanList;
    private String startTime;
    private int secondTime;
    private int times;
    private List<UserListBean.UserBean> choosedList;
    private TextView sendTaskBtn;
    private int plfs;
    private ProgressDialog progressDialog;
    private ProgressDialog progressDialogLoading;

    @Override
    protected int setLayout() {
        return R.layout.activity_interaction_regular;
    }

    @Override
    protected void initView() {
        ll_qdsj = bindView(R.id.ll_qdsj);
        ll_sjjg = bindView(R.id.ll_sjjg);
        // TODO: 2017/7/21  评论内容 隐藏了
        ll_yznr = bindView(R.id.ll_fwpl);
        ll_sbxz = bindView(R.id.ll_sbxz);
        ll_add_num = bindView(R.id.ll_add_num);
        tv_qdsj = bindView(R.id.tv_qdsj);
        tv_sjjg = bindView(R.id.tv_yxjg);
        TextView tv_dz = bindView(R.id.tv_dz);
        TextView tv_pl = bindView(R.id.tv_pl);
        RadioGroup rg_hdfs = bindView(R.id.rg_hdfs);
        tv_plnr = bindView(R.id.tv_plnr);
        tv_sbxz = bindView(R.id.tv_sbxz);
        tv_add_num = bindView(R.id.tv_yxsl);
        sendTaskBtn = bindView(R.id.send_task_btn);
        tv_dz.setOnClickListener(v -> rg_hdfs.check(R.id.rb_dz));
        tv_pl.setOnClickListener(v -> rg_hdfs.check(R.id.rb_pl));

        rg_hdfs.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) (group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_dz:
                    plfs = 1;
                    break;
                case R.id.rb_pl:
                    plfs = 2;
                    break;
            }
        });

        // 启动时间
        ll_qdsj.setOnClickListener(v -> {
            TimePickerPopup timePickerPopup = new TimePickerPopup(InteractionRegularActivity.this, true, (year, month, dayOfMonth, hour, minute) -> {
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
            SecondPickerPopup timePickerPopup = new SecondPickerPopup(InteractionRegularActivity.this, (second) -> {
                secondTime = (second);
                tv_sjjg.setText(secondTime + "秒");
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
        ll_yznr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InteractionRegularActivity.this, InteractionCommentActivity.class);
                intent.putExtra(InteractionCommentContentId, tv_plnr.getText().toString());
                startActivityForResult(intent, tokerVerifyContent);
            }
        });

        // 执行次数
        ll_add_num.setOnClickListener(v -> {
            TokerNumAddPopup numAddPopup = new TokerNumAddPopup(InteractionRegularActivity.this, num -> {
                times = num;
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

        //设备选择
        ll_sbxz.setOnClickListener(v -> {
            TokerDeviceSelectPopup numAddPopup = new TokerDeviceSelectPopup(InteractionRegularActivity.this, userBeanList, true, arg -> {
                choosedList = arg;
                tv_sbxz.setText(choosedList.size() + "");
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

        sendTaskBtn.setOnClickListener(v -> {
            if (hasNo()) return;
            if (progressDialog == null)
                progressDialog = new ProgressDialog(this, R.mipmap.loading);
            progressDialog.show();
            TaskBean bean = new TaskBean();
            List<TaskBean.CuserBean> cuserBeen = new ArrayList<TaskBean.CuserBean>();
            for (UserListBean.UserBean userBean : choosedList) {
                TaskBean.CuserBean cuserBean = new TaskBean.CuserBean();
                cuserBean.setCId(userBean.getCId());
                cuserBeen.add(cuserBean);
            }
            TaskBean.CtaskBean ctaskBean = new TaskBean.CtaskBean();
            ctaskBean.setcInteractive(1);
            ctaskBean.setCStarttime(startTime);
            ctaskBean.setCNum(times);
            ctaskBean.setcIntervaltime(secondTime);
            ctaskBean.setCTasktype(4);
            bean.setCtask(ctaskBean);
            bean.setCuser(cuserBeen);
            Gson gson = new Gson();
            String json = gson.toJson(bean);
            Log.d("InteractionRegularActiv", json);
            NetTool.getInstance().postRequest(UrlValue.ADD_TAKS+MyApp.userId, json, NormalResultBean.class, new OnHttpCallBack<NormalResultBean>() {
                @Override
                public void onSuccess(NormalResultBean response) {
                    progressDialog.dismiss();
                    boolean equals = response.getCode().equals("1");
                    if (equals) {
                        new DialogOK(InteractionRegularActivity.this, "任务添加成功", () -> {
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

    private boolean hasNo() {
        if (tv_sbxz.getText().toString().trim().length() == 0 ||
                startTime == null || startTime.trim().length() == 0 ||
                tv_add_num.getText().toString().trim().length() == 0 ||
                tv_sjjg.getText().toString().trim().length() == 0||(choosedList==null||choosedList.size()==0)) {
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

        NetTool.getInstance().startRequest("get", UrlValue.SELECT_USER + MyApp.companyDd+UrlValue.FANS_MANAGER_PARAM_USER_ID+MyApp.userId, UserListBean.class, new OnHttpCallBack<UserListBean>() {
            @Override
            public void onSuccess(UserListBean response) {
                if (progressDialogLoading!=null)progressDialogLoading.dismiss();
                if (response.getCode().equals("1")) {
                    userBeanList = (ArrayList<UserListBean.UserBean>) response.getUser();
                    DeviceManager.getInstance().saveDeviceBeanList(userBeanList);
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == tokerVerifyContent) {
            try {
                tv_plnr.setText(data.getStringExtra(InteractionCommentContentId));
            } catch (NullPointerException npe) {
            }
        }
    }
}
