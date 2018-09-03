package yktong.com.godofdog.activity.market;

import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import space.eileen.free_util.DialogOK;
import space.eileen.free_util.ProgressDialog;
import space.eileen.free_util.list_view_check.ListCheckBean;
import space.eileen.free_util.list_view_check.ListCheckPopup;
import yktong.com.godofdog.R;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.NormalResultBean;
import yktong.com.godofdog.bean.TaskBean;
import yktong.com.godofdog.bean.UserListBean;
import yktong.com.godofdog.bean.WeFriendBean;
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
import static yktong.com.godofdog.base.MyApp.userId;

public class SmallRoutineActivity extends BaseActivity {
    public final static int REQUEST_MATTER_DATA_CODE = 10010;
    public final static String MATTER_JSON = "MATTER_JSON";
    //start
    LinearLayout ll_sjjg;
    LinearLayout ll_add_num;
    LinearLayout ll_yxdx;
    LinearLayout ll_sbxz;
    LinearLayout ll_qdsj;
    LinearLayout ll_matter_data;
    List<WeFriendBean.CWechatFriendBean> selectedFriend = new ArrayList<>();
    List<Integer> cwfidSelected = new ArrayList<>();
    private TextView tv_add_num;
    private TextView tv_yxdx;
    private TextView tv_sjjg;
    private TextView tv_sbxz;
    private TextView tv_qdsj;
    private WindowManager.LayoutParams params;
    private ArrayList<UserListBean.UserBean> userBeanList = new ArrayList<>();
    private List<WeFriendBean.CWechatFriendBean> cWechatFriend = new ArrayList<>();
    private List<TaskBean.CWechatFriendBean> selectedcWechatFriend = new ArrayList<>();
    private TextView sendTaskBtn;
    private int selectedDeviceId = -1;
    private int seconds;
    private int addNum;
    private ProgressDialog progressDialog;
    private EditText et_xcx;
    private int currentUserId = -1;
    private ProgressDialog progressDialogLoading;
    private boolean loadedUsers, loadedWCFriend;
    @Override
    protected int setLayout() {
        return R.layout.activity_small_routine;
    }
    private void hiddenLoading() {
        if (loadedWCFriend &&loadedUsers&&progressDialogLoading != null)
            progressDialogLoading.dismiss();
    }
    @Override
    protected void initView() {
        tv_add_num = bindView(R.id.tv_yxsl);
        tv_yxdx = bindView(R.id.tv_yxdx);
        tv_sjjg = bindView(R.id.tv_yxjg);
        tv_sbxz = bindView(R.id.tv_sbxz);
        et_xcx = bindView(R.id.et_xcx);
        ll_sjjg = bindView(R.id.ll_sjjg);
        ll_add_num = bindView(R.id.ll_add_num);
        ll_sbxz = bindView(R.id.ll_sbxz);
        ll_yxdx = bindView(R.id.ll_yxdx);
        ll_qdsj = bindView(R.id.ll_qdsj);
        ll_matter_data = bindView(R.id.ll_matter_data);
        tv_qdsj = bindView(R.id.tv_qdsj);
        sendTaskBtn = bindView(R.id.send_task_btn);

        ll_qdsj.setOnClickListener(v -> {
            TimePickerPopup timePickerPopup = new TimePickerPopup(this, true, (year, month, dayOfMonth, hour, minute) -> {
                String startTime = year + "-" + convertText(month) + "-" + convertText(dayOfMonth) + " " + convertText(hour) + ":" + convertText(minute) + ":00";
                try {
                    if (isFutureTime(startTime)) {
                        tv_qdsj.setText(startTime);
                    } else {
                        startTime = "";
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            );
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

        ll_sjjg.setOnClickListener(v -> {
            SecondPickerPopup timePickerPopup = new SecondPickerPopup(this, (second) -> {
                seconds = (second);
                tv_sjjg.setText(seconds + "秒");
            });
            timePickerPopup.showAtLocation(findViewById(R.id.ll_sjjg), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
        ll_add_num.setOnClickListener(v -> {
            TokerNumAddPopup numAddPopup = new TokerNumAddPopup(this, num -> {
                addNum = num;
                tv_add_num.setText(num + "");
            });
            numAddPopup.showAtLocation(findViewById(R.id.ll_add_num), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
            TokerDeviceSelectPopup numAddPopup = new TokerDeviceSelectPopup(this, userBeanList, false, arg -> {
                if (arg == null || arg.size() == 0) {
                    selectedDeviceId = -1;
                    tv_sbxz.setText("");
                    return;
                }
                tv_sbxz.setText(arg.get(0).getCName());
                getFriendList(arg.get(0).getCId());
                selectedDeviceId = arg.get(0).getCId();
            });
            numAddPopup.showAtLocation(findViewById(R.id.ll_sbxz), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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

        ll_yxdx.setOnClickListener(v -> {
            if (cWechatFriend == null || cWechatFriend.size() == 0) {
                Toast.makeText(this, "没有营销对象", Toast.LENGTH_SHORT).show();
                return;
            }
//            PikerPopup popup = new PikerPopup(mContext, position -> {
////                if (selectedFriend==null||selectedFriend.size()==0)return;
//                if (selectedFriend.contains(cWechatFriend.get(position))) {
//                    selectedFriend.remove(cWechatFriend.get(position));
//                } else {
//                    selectedFriend.add(cWechatFriend.get(position));
//                }
//                tv_yxdx.setText(cWechatFriend.get(position).getCWffriendname());
//
//            }, cWechatFriend, new CommonAdapter<WeFriendBean.CWechatFriendBean>(cWechatFriend, mContext, R.layout.piker_item) {
//                @Override
//                public void setData(WeFriendBean.CWechatFriendBean cWechatFriendBean, int position, CommonViewHolder viewHolder) {
//                    viewHolder.setText(R.id.picker_item_tv, cWechatFriendBean.getCWffriendname());
//                }
//            });
//            popup.showAtLocation(findViewById(R.id.ll_yxdx), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
            List<ListCheckBean> listCheckBeanList = new ArrayList<ListCheckBean>();
            for (WeFriendBean.CWechatFriendBean friend : cWechatFriend) {
                ListCheckBean listCheckBean = new ListCheckBean();
                listCheckBean.setId(friend.getCWfid());
                listCheckBean.setText(friend.getCWffriendname());
                listCheckBeanList.add(listCheckBean);
            }
            ListCheckPopup<ListCheckBean> numAddPopup = new ListCheckPopup<ListCheckBean>(this, listCheckBeanList, 10, arg -> {
                if (arg == null || arg.size() == 0) {
                    selectedcWechatFriend.clear();
                    tv_yxdx.setText("");
                    return;
                }
                if (arg.size() == 1) {
                    tv_yxdx.setText(arg.get(0).getText());
                } else {
                    tv_yxdx.setText(arg.size() + "");
                }
                selectedcWechatFriend.clear();
                for (ListCheckBean listCheckBean : arg) {
                    TaskBean.CWechatFriendBean cwf = new TaskBean.CWechatFriendBean();
                    cwf.setcWfid(listCheckBean.getId());
                    selectedcWechatFriend.add(cwf);
                }
            });
            numAddPopup.showAtLocation(findViewById(R.id.ll_sbxz), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
            ctaskBean.setCTasktype(6);
            ctaskBean.setcIntervaltime(seconds);
//            List<TaskBean.CWechatFriendBean> cWechatFriendBeenList = new ArrayList<TaskBean.CWechatFriendBean>();
//            for (WeFriendBean.CWechatFriendBean cWechatFriendBean : selectedFriend) {
//                TaskBean.CWechatFriendBean wechatFriendBean = new TaskBean.CWechatFriendBean();
//                wechatFriendBean.setcWfid(cWechatFriendBean.getCWfid());
//                cWechatFriendBeenList.add(wechatFriendBean);
//            }
            bean.setCWechatFriend(selectedcWechatFriend);
            ctaskBean.setCStarttime(tv_qdsj.getText().toString());
            ctaskBean.setcCxcname(et_xcx.getText().toString());
            List<TaskBean.CuserBean> been = new ArrayList<>();
            TaskBean.CuserBean cuserBean = new TaskBean.CuserBean();
            cuserBean.setCId(selectedDeviceId);
            been.add(cuserBean);
            ctaskBean.setCNum(addNum);
            bean.setCuser(been);
            bean.setCtask(ctaskBean);
            Gson gson = new Gson();
            String json = gson.toJson(bean);
            Log.d("InteractionRegularActiv", json);
            NetTool.getInstance().postRequest(UrlValue.ADD_TAKS + userId, json, NormalResultBean.class, new OnHttpCallBack<NormalResultBean>() {
                @Override
                public void onSuccess(NormalResultBean response) {
                    progressDialog.dismiss();
                    boolean equals = response.getCode().equals("1");
                    if (equals) {
//                        Log.d("InteractionRegularActiv", json);
//                        Toast.makeText(mContext, "任务添加成功", Toast.LENGTH_SHORT).show();
//                        finish();
                        new DialogOK(SmallRoutineActivity.this, "任务添加成功", () -> {
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
                tv_qdsj.getText().toString().trim().length() == 0 ||
                et_xcx.getText().toString().trim().length() == 0||
                selectedcWechatFriend==null||selectedcWechatFriend.size()==0/*
                tv_add_num.getText().toString().trim().length() == 0 */ ||
                tv_sjjg.getText().toString().trim().length() == 0) {
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
        getFriendList(userId);
    }

    private void getFriendList(int userId) {
        if (progressDialogLoading!=null)progressDialogLoading.show();
        if (userId == -1) {
            userId = userId;
        }
        selectedcWechatFriend.clear();
        tv_yxdx.setText("");

        NetTool.getInstance().startRequest("get", UrlValue.SELECT_FRIEND + userId, WeFriendBean.class, new OnHttpCallBack<WeFriendBean>() {
            @Override
            public void onSuccess(WeFriendBean response) {
                loadedWCFriend =true;
                hiddenLoading();
                if (response.getCode().equals("1")) {
                    cWechatFriend = response.getCWechatFriend();
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
    }
    //end


}
