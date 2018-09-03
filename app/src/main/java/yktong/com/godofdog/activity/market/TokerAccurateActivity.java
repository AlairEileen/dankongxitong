package yktong.com.godofdog.activity.market;

import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import space.eileen.free_util.DialogOK;
import space.eileen.free_util.GenderRadioView;
import space.eileen.free_util.ProgressDialog;
import yktong.com.godofdog.R;
import yktong.com.godofdog.activity.manage.DateManageActivity;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.DataManagerBean;
import yktong.com.godofdog.bean.NormalResultBean;
import yktong.com.godofdog.bean.PhoneNumberFileBean;
import yktong.com.godofdog.bean.TaskBean;
import yktong.com.godofdog.bean.UserListBean;
import yktong.com.godofdog.manager.DeviceManager;
import yktong.com.godofdog.popup.SecondPickerPopup;
import yktong.com.godofdog.popup.TimePickerPopup;
import yktong.com.godofdog.popup.TokerDeviceSelectPopup;
import yktong.com.godofdog.popup.TokerNumAddPopup;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OkHttpUtil;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.value.UrlValue;

import static space.eileen.tools.XString.convertText;
import static space.eileen.tools.XString.isFutureTime;
import static yktong.com.godofdog.R.id.tv_yxjg;
import static yktong.com.godofdog.R.id.tv_yxsl;
import static yktong.com.godofdog.activity.market.TokerVerifyContentActivity.tokerVerifyContentId;

public class TokerAccurateActivity extends BaseActivity {

    public final static int REQUEST_DATA_CODE = 10010;
    public final static String DATA_NAME = "dataName";
    public final static String DATA_ID = "DATA_ID";
    private final int tokerVerifyContent = 1;
    LinearLayout ll_qdsj;
    LinearLayout ll_sjjg;
    LinearLayout ll_yznr;
    LinearLayout ll_add_num;
    LinearLayout ll_data_add;
    LinearLayout ll_sbxz;
    GenderRadioView grv_gender;
    private WindowManager.LayoutParams params;
    private TextView tv_qdsj;
    private TextView tv_sjjg;
    private TextView tv_yznr;
    private TextView tv_sbxz;
    private TextView tv_data_name;
    private TextView tv_add_num;
    private ArrayList<UserListBean.UserBean> userBeanList = new ArrayList<>();
    private int dataId = -1;
    private List<UserListBean.UserBean> selectedUserList;
    private String startTime;
    private int seconds;
    private String content;
    private int addNum;
    private List<UserListBean.UserBean> chooseDevices;
    private TextView sendTaskBtn;
    private int sex;
    private ProgressDialog progressDialog;
    private ProgressDialog progressDialogLoading;
    private boolean loadedUsers,loadedFile;

    @Override
    protected int setLayout() {
        return R.layout.activity_toker_accurate;
    }

    @Override
    protected void initView() {
        ll_qdsj = bindView(R.id.ll_qdsj);
        ll_sjjg = bindView(R.id.ll_sjjg);
        ll_yznr = bindView(R.id.ll_fwpl);
        ll_sbxz = bindView(R.id.ll_sbxz);
        ll_data_add = bindView(R.id.ll_data_add);
        ll_add_num = bindView(R.id.ll_add_num);
        tv_qdsj = bindView(R.id.tv_qdsj);
        tv_sjjg = bindView(tv_yxjg);
//        tv_submit = bindView(R.id.tv_submit);
        tv_yznr = bindView(R.id.tv_fwpl);
        tv_data_name = bindView(R.id.tv_data_name);
        tv_sbxz = bindView(R.id.tv_sbxz);
        tv_add_num = bindView(tv_yxsl);
        TextView tv_null = bindView(R.id.tv_null);
        TextView tv_male = bindView(R.id.tv_male);
        TextView tv_female = bindView(R.id.tv_female);
        sendTaskBtn = bindView(R.id.send_task_btn);

        grv_gender = bindView(R.id.grv_gender);

//        ll_qdsj.setOnClickListener(v -> {
//            TimePickerPopup timePickerPopup = new TimePickerPopup(TokerAccurateActivity.this, true,(year, month, dayOfMonth, hour, minute) -> tv_qdsj.setText(year + "-" + month + "-" + dayOfMonth + " " + hour + ":" + minute));
//            timePickerPopup.showAtLocation(findViewById(R.id.ll_qdsj), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//            params = getWindow().getAttributes();
//            //当弹出Popupwindow时，背景变半透明
//            params.alpha = 0.7f;
//            getWindow().setAttributes(params);
//            //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
//            timePickerPopup.setOnDismissListener(() -> {
//                params = getWindow().getAttributes();
//                params.alpha = 1f;
//                getWindow().setAttributes(params);
//            });
//
//        });
//        ll_sjjg.setOnClickListener(v -> {
//                    SecondPickerPopup timePickerPopup = new SecondPickerPopup(TokerAccurateActivity.this, (hour, minute, second) -> tv_sjjg.setText((hour * 60 * 60 + minute * 60 + second) + "秒"));
//                });
        // 启动时间

        ll_qdsj.setOnClickListener(v -> {
            TimePickerPopup timePickerPopup = new TimePickerPopup(TokerAccurateActivity.this, true, (year, month, dayOfMonth, hour, minute) -> {
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
        ll_yznr.setOnClickListener(v -> {
            Intent intent = new Intent(TokerAccurateActivity.this, TokerVerifyContentActivity.class);
            intent.putExtra(tokerVerifyContentId, tv_yznr.getText().toString());
            startActivityForResult(intent, tokerVerifyContent);
        });
        ll_add_num.setOnClickListener(v -> {
            TokerNumAddPopup numAddPopup = new TokerNumAddPopup(TokerAccurateActivity.this, num -> tv_add_num.setText(num + ""));

        });

        // 时间间隔
        ll_sjjg.setOnClickListener(v -> {
            SecondPickerPopup timePickerPopup = new SecondPickerPopup(TokerAccurateActivity.this, (second) -> {
                seconds = (second);
                tv_sjjg.setText(seconds + "秒");
            });
            timePickerPopup.showAtLocation(findViewById(R.id.ll_qdsj), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            params = getWindow().getAttributes();
            //当弹出Popupwindow时，背景变半透明
            params.alpha = 0.7f;
            getWindow().setAttributes(params);
            //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
            timePickerPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    params = getWindow().getAttributes();
                    params.alpha = 1f;
                    getWindow().setAttributes(params);
                }
            });
        });

        //验证内容
        ll_yznr.setOnClickListener(v -> {
            Intent intent = new Intent(TokerAccurateActivity.this, TokerVerifyContentActivity.class);
            intent.putExtra(tokerVerifyContentId, tv_yznr.getText().toString());
            startActivityForResult(intent, tokerVerifyContent);
        });

        // 添加数量
        ll_add_num.setOnClickListener(v -> {
            TokerNumAddPopup numAddPopup = new TokerNumAddPopup(TokerAccurateActivity.this, num -> {
                addNum = num;
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

        ll_sbxz.setOnClickListener(v -> {
            TokerDeviceSelectPopup numAddPopup = new TokerDeviceSelectPopup(TokerAccurateActivity.this, userBeanList, true, arg -> {
                tv_sbxz.setText(arg.size() + "");
                selectedUserList = arg;
            });
        });
        //设备选择
        ll_sbxz.setOnClickListener(v -> {
            TokerDeviceSelectPopup numAddPopup = new TokerDeviceSelectPopup(TokerAccurateActivity.this, userBeanList, true, arg -> {
                chooseDevices = arg;
                tv_sbxz.setText(chooseDevices.size() + "");
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
            TaskBean.CtaskBean ctaskBean = new TaskBean.CtaskBean();
            ctaskBean.setCTasktype(1);
            ctaskBean.setcIntervaltime(seconds);
            ctaskBean.setCNum(addNum);
            ctaskBean.setCStarttime(startTime);
            ctaskBean.setCSex(getSex());
//            Log.d("TokerAccurateActivity", content);
            String s = tv_yznr.getText().toString();
            ctaskBean.setcCheckmage(s);
            ctaskBean.setcFileid(dataId);
            List<TaskBean.CuserBean> been = new ArrayList<>();
            for (UserListBean.UserBean chooseDevice : chooseDevices) {
                TaskBean.CuserBean cuserBean = new TaskBean.CuserBean();
                cuserBean.setCId(chooseDevice.getCId());
                been.add(cuserBean);
            }
            bean.setCuser(been);
            bean.setCtask(ctaskBean);
            Gson gson = new Gson();
            String json = gson.toJson(bean);
            Log.d("InteractionRegularActiv", json);
            System.out.println("**********************" + json);
            NetTool.getInstance().postRequest(UrlValue.ADD_TAKS + MyApp.userId, json, NormalResultBean.class, new OnHttpCallBack<NormalResultBean>() {
                @Override
                public void onSuccess(NormalResultBean response) {
                    progressDialog.dismiss();
                    boolean equals = response.getCode().equals("1");
                    if (equals) {
//                        Log.d("InteractionRegularActiv", json);
//                        Toast.makeText(mContext, "任务添加成功", Toast.LENGTH_SHORT).show();
//                        finish();
                        new DialogOK(TokerAccurateActivity.this, "任务添加成功", () -> {
                            finish();
                        }).show();

                    }
                }

                @Override
                public void onError(Throwable e) {
                    progressDialog.dismiss();
                    e.fillInStackTrace();
                }
            });
        });
        ll_data_add.setOnClickListener((v) -> {
            Intent intent = new Intent(TokerAccurateActivity.this, DateManageActivity.class);
            intent.putExtra("code", REQUEST_DATA_CODE);
            startActivityForResult(intent, REQUEST_DATA_CODE);
        });

    }

    private int getSex() {
//        switch (radioGroup.getCheckedRadioButtonId()) {
//            case R.id.rb_male:
//                return 1;
//            case R.id.rb_female:
//                return 2;
//            case R.id.rb_null:
//                return 3;
//            default:
//                return 3;
//        }

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
                dataId == -1 ||
                tv_add_num.getText().toString().trim().length() == 0 ||
                tv_sjjg.getText().toString().trim().length() == 0 || (chooseDevices == null || chooseDevices.size() == 0)) {
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
//        NetTool.getInstance().startRequest("get",);
        userBeanList = DeviceManager.getInstance().getDevices();
        NetTool.getInstance().startRequest("get", UrlValue.SELECT_USER + MyApp.companyDd+UrlValue.FANS_MANAGER_PARAM_USER_ID+MyApp.userId, UserListBean.class, new OnHttpCallBack<UserListBean>() {
            @Override
            public void onSuccess(UserListBean response) {
                loadedUsers=true;
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
        initDefaultFile();
    }

    private void hiddenLoading() {
        if (loadedFile&&loadedUsers&&progressDialogLoading != null)
            progressDialogLoading.dismiss();
    }

    private void initDefaultFile() {
        NetTool.getInstance().startRequest(OkHttpUtil.POST, UrlValue.DATA_MANAGE_FIND_ALL + MyApp.companyDd, DataManagerBean.class, new OnHttpCallBack<DataManagerBean>() {
            @Override
            public void onSuccess(DataManagerBean response) {
                loadedFile=true;
                hiddenLoading();
                response.doResponse(() -> {
                    if (response.getFile() == null || response.getFile().size() == 0) return;
                    for (PhoneNumberFileBean aa : response.getFile()) {
                        if (aa.isSelected()) {
                            tv_data_name.setText(aa.getcFilename());
                            dataId = aa.getcFileid();
                        }
                    }
                });
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case tokerVerifyContent:
                try {
//                    content = data.getStringExtra(tokerVerifyContentId);
                    tv_yznr.setText(data.getStringExtra(tokerVerifyContentId));
                } catch (NullPointerException npe) {
                }
                break;
            case REQUEST_DATA_CODE:
                try {
                    String dataName = data.getStringExtra(DATA_NAME);
                    if (dataName != null) {
                        tv_data_name.setText(dataName);
                        dataId = data.getIntExtra(DATA_ID, -1);
                    }
                } catch (NullPointerException e) {

                }
                break;
        }
//        if (requestCode == tokerVerifyContent) {
//            try {
//                tv_yznr.setText(content);
//            } catch (NullPointerException npe) {
//            }
//        }
    }
}