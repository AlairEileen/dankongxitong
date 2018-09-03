package yktong.com.godofdog.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
import space.eileen.free_util.ProgressDialog;
import space.eileen.tools.ImageTools;
import space.eileen.tools.SelectImagesTool;
import space.eileen.tools.UriTools;
import yktong.com.godofdog.R;
import yktong.com.godofdog.activity.AboutActivity;
import yktong.com.godofdog.activity.SettingsActivity;
import yktong.com.godofdog.activity.SettingsCompanyActivity;
import yktong.com.godofdog.base.BaseFragment;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.NormalResultBean;
import yktong.com.godofdog.bean.UserAvatarBean;
import yktong.com.godofdog.bean.UserBean;
import yktong.com.godofdog.bean.UserLoginBean;
import yktong.com.godofdog.bean.jurisdiction_beans.ResponseUserRoleBean;
import yktong.com.godofdog.bean.user_beans.UserRequestBean;
import yktong.com.godofdog.manager.WorkManger;
import yktong.com.godofdog.popup.PutTextPopup;
import yktong.com.godofdog.tool.datebase.DBTool;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OkHttpUtil;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.util.SPUtil;
import yktong.com.godofdog.value.SpValue;
import yktong.com.godofdog.value.UrlValue;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * Created by vampire on 2017/6/13.
 */

public class UserFragment extends BaseFragment {
    String picPath = null;
    int code_result_pic = 8001;
    private ImageView iv_avatar;
    private TextView tv_user_name;
    private TextView tv_company_address;
    private TextView tv_company_phone;
    private TextView tv_job;
    private Uri picUri;
    private ProgressDialog progressDialog;
    private LinearLayout ll_settings_view;
    private ProgressDialog progressDialogSettingsButton;


    @Override
    protected int setLayout() {
        return R.layout.fragment_user;
    }

    @Override
    protected void initView() {

        iv_avatar = bindView(R.id.iv_avatar);
        ll_settings_view = bindView(R.id.ll_settings_view);
        TextView tv_gssz = bindView(R.id.tv_gssz);
        tv_user_name = bindView(R.id.tv_user_name);

        bindView(R.id.ll_username).setOnClickListener(v -> {
            setUserName();
        });
        tv_company_address = bindView(R.id.tv_company_address);
        tv_company_phone = bindView(R.id.tv_company_phone);
        LinearLayout ll_about = bindView(R.id.ll_about);
        LinearLayout ll_settings = bindView(R.id.ll_settings);
        ll_settings.setOnClickListener(v -> {
            if (progressDialogSettingsButton == null)
                progressDialogSettingsButton = new ProgressDialog(getActivity(), R.mipmap.loading);
            progressDialogSettingsButton.show();
            NetTool.getInstance().startRequest(OkHttpUtil.GET, UrlValue.IS_ADMIN_USER + MyApp.userId, ResponseUserRoleBean.class, new OnHttpCallBack<ResponseUserRoleBean>() {
                @Override
                public void onSuccess(ResponseUserRoleBean response) {
                    response.doResponse(new ResponseUserRoleBean.ResponseUserRoleStatus() {
                        @Override
                        public void not_admin(String msg) {
                            if (progressDialogSettingsButton != null)
                                progressDialogSettingsButton.dismiss();
                            getActivity().runOnUiThread(() -> {
                                changeSettingViewShow(false);
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                            });
                        }

                        @Override
                        public void cannot_do(String msg) {
                            if (progressDialogSettingsButton != null)
                                progressDialogSettingsButton.dismiss();
                            getActivity().runOnUiThread(() -> {
                                changeSettingViewShow(false);
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                            });
                        }

                        @Override
                        public void doSuccess() {
                            if (progressDialogSettingsButton != null)
                                progressDialogSettingsButton.dismiss();
                            startActivity(new Intent(getActivity(), SettingsActivity.class));
                        }
                    });
                }

                @Override
                public void onError(Throwable e) {

                }
            });
        });

        ll_about.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AboutActivity.class));
        });
        tv_job = bindView(R.id.tv_job);
        tv_gssz.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SettingsCompanyActivity.class);
            startActivity(intent);
        });
        iv_avatar.setOnClickListener((v) -> {
            SelectImagesTool.showAddPicView(getActivity(), picPath);

        });

        bindView(R.id.ll_refresh).setOnClickListener(v -> {
            Log.d("UserFragment", "click");
            WorkManger.getInstence().doTask(SpValue.STATE_QUERY_FRIENDS_NUM);
        });
    }

    private void setUserName() {
        PutTextPopup.build(getActivity(), "修改昵称", R.id.tv_user_name, text -> {

            if (text != null) {
                if (text.trim().length() < 1) {
                    Toast.makeText(getActivity(), "昵称不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                UserRequestBean userBean = new UserRequestBean();
                userBean.setId(MyApp.userId);
                userBean.setName(text);
                String json = new Gson().toJson(userBean);
                Log.d("############initData", json);
                NetTool.getInstance().postRequest(UrlValue.UPDATE_NICK, json, NormalResultBean.class, new OnHttpCallBack<NormalResultBean>() {
                    @Override
                    public void onSuccess(NormalResultBean response) {
                        if (response.getCode().equals("1")) {
                            Log.d("UserFragment", "修改成功");
                        }
                        String registrationID = JPushInterface.getRegistrationID(MyApp.getmContext());
                        UserBean userBean = DBTool.getInstance().queryUser();
                        UserRequestBean userRequestBean = new UserRequestBean();
                        userRequestBean.setCompanyId(Integer.valueOf(userBean.getcCompanyid()));
                        userRequestBean.setPhoneNum(userBean.getcLoginname());
                        userRequestBean.setPassword(userBean.getcPassword());
                        userRequestBean.setcLogintype(registrationID);
                        userRequestBean.setImei(android.os.Build.SERIAL);
                        String json = new Gson().toJson(userRequestBean);
                        Log.d("#####userbean", json);
                        NetTool.getInstance().postRequest(UrlValue.REQUEST_USER_LOGIN, json, UserLoginBean.class, new OnHttpCallBack<UserLoginBean>() {
                            @Override
                            public void onSuccess(UserLoginBean response) {
                                if (response == null) return;
                                response.doResponse(() -> {
                                    MyApp.bindPersonInfo(response.getUser());
                                    setInfo();
                                });
                            }

                            @Override
                            public void onError(Throwable e) {
//
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
            }
        });
    }

    @Override
    protected void initData() {
        setInfo();
        tv_job.setText(MyApp.cDutyname);
        initAvatar();
    }


    private void setInfo() {
        tv_company_address.setText(MyApp.companyAddress);
        tv_company_phone.setText(MyApp.userPhone);
        tv_user_name.setText(MyApp.userName);
    }

    private void initAvatar() {
        setAvatar(null, R.mipmap.pic_def);

        NetTool.getInstance().startRequest(OkHttpUtil.GET, UrlValue.REQUEST_USER_AVATAR + MyApp.userId, UserAvatarBean.class, new OnHttpCallBack<UserAvatarBean>() {
            @Override
            public void onSuccess(UserAvatarBean response) {
                bindAvatar(response);
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        changeAvatar(requestCode, resultCode, data);
        if (requestCode == code_result_pic) {
            uploadAvatar();
        }
    }

    private void changeAvatar(int requestCode, int resultCode, Intent data) {
        ArrayList<String> paths = SelectImagesTool.onActivityResult(requestCode, resultCode, data);
        if (paths != null && paths.size() > 0) {
            picPath = paths.get(0);
            picUri = ImageTools.cropImage(getActivity(), code_result_pic, Uri.parse("file://" + "/" + picPath), 120, 120, getExternalStorageDirectory().getPath() + "/dx_temp/");
        }
    }

    private void uploadAvatar() {
        if (progressDialog == null)
            progressDialog = new ProgressDialog(getActivity(), R.mipmap.loading);
        progressDialog.show();
        HashMap<String, Object> params = new HashMap<String, Object>();
        File file = new File(UriTools.getRealFilePath(getActivity(), picUri));
        params.put("file", file);
        params.put("userid", MyApp.userId);

        NetTool.getInstance().upLoadFile(UrlValue.UPLOAD_FILE_AVATAR, params, UserAvatarBean.class, new OkHttpUtil.ReqProgressCallBack<UserAvatarBean>() {
            @Override
            public void onProgress(long total, long current) {

            }

            @Override
            public void onSuccess(UserAvatarBean response) {
                if (file.exists()) file.delete();
                bindAvatar(response);
                progressDialog.dismiss();
            }

            @Override
            public void onFailed(String msg) {
                if (file.exists()) file.delete();
                progressDialog.dismiss();


            }
        });

    }

    private void bindAvatar(UserAvatarBean response) {
        if (response.getCode() == 1) {
//            Log.d("UserFragment", response.getcUserImageUrl().getcUserImageUrl());
            SPUtil.putAndApply(MyApp.getmContext(), "photo", response.getcUserImageUrl().getcUserImageUrl());
            setAvatar(response.getcUserImageUrl().getcUserImageUrl(), -1);
        }
        response.doResponse(() -> setAvatar(response.getcUserImageUrl().getcUserImageUrl(), -1));
    }

    private void setAvatar(String url, int uri) {
        getActivity().runOnUiThread(() -> Glide.with(getActivity()).load(uri == -1 ? url : uri).asBitmap().into(new BitmapImageViewTarget(iv_avatar) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    circularBitmapDrawable.setCircular(true);
                }
                iv_avatar.setImageDrawable(circularBitmapDrawable);
            }
        }));
    }

    public void onShow() {
        NetTool.getInstance().startRequest(OkHttpUtil.GET, UrlValue.IS_ADMIN_USER + MyApp.userId, ResponseUserRoleBean.class, new OnHttpCallBack<ResponseUserRoleBean>() {
            @Override
            public void onSuccess(ResponseUserRoleBean response) {
                response.doResponse(new ResponseUserRoleBean.ResponseUserRoleStatus() {
                    @Override
                    public void not_admin(String msg) {
                        changeSettingViewShow(false);
                    }

                    @Override
                    public void cannot_do(String msg) {
                        getActivity().runOnUiThread(() -> {
                            changeSettingViewShow(false);
//                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void doSuccess() {
                        changeSettingViewShow(true);
                    }
                });
            }

            @Override
            public void onError(Throwable e) {

            }
        });

    }

    private void changeSettingViewShow(boolean status) {
        if (ll_settings_view == null) return;
        ll_settings_view.setVisibility(status ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        onShow();
    }
}
