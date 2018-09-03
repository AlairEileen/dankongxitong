package yktong.com.godofdog.activity;

import android.content.Intent;
import android.widget.Toast;

import space.eileen.free_util.ProgressDialog;
import yktong.com.godofdog.R;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.jurisdiction_beans.ResponseUserRoleBean;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OkHttpUtil;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.value.UrlValue;

public class SettingsActivity extends BaseActivity {


    private ProgressDialog progressDialogSettingsButton;

    @Override
    protected int setLayout() {
        return R.layout.activity_settings;
    }

    @Override
    protected void initView() {
        bindView(R.id.ll_szqx).setOnClickListener(v -> {
            if (progressDialogSettingsButton == null)
                progressDialogSettingsButton = new ProgressDialog(SettingsActivity.this, R.mipmap.loading);
            progressDialogSettingsButton.show();
            NetTool.getInstance().startRequest(OkHttpUtil.GET, UrlValue.IS_ADMIN_USER + MyApp.userId, ResponseUserRoleBean.class, new OnHttpCallBack<ResponseUserRoleBean>() {
                @Override
                public void onSuccess(ResponseUserRoleBean response) {
                    response.doResponse(new ResponseUserRoleBean.ResponseUserRoleStatus() {
                        @Override
                        public void not_admin(String msg) {
                            if (progressDialogSettingsButton != null)
                                progressDialogSettingsButton.dismiss();
                            SettingsActivity.this.runOnUiThread(() -> {
                                Toast.makeText(SettingsActivity.this, msg, Toast.LENGTH_SHORT).show();
                                SettingsActivity.this.finish();
                            });
                        }

                        @Override
                        public void cannot_do(String msg) {
                            if (progressDialogSettingsButton != null)
                                progressDialogSettingsButton.dismiss();
                            SettingsActivity.this.runOnUiThread(() -> {
                                Toast.makeText(SettingsActivity.this, msg, Toast.LENGTH_SHORT).show();
                                SettingsActivity.this.finish();
                            });
                        }

                        @Override
                        public void doSuccess() {
                            if (progressDialogSettingsButton != null)
                                progressDialogSettingsButton.dismiss();
                            startActivity(new Intent(SettingsActivity.this, JurisdictionActivity.class));

                        }
                    });
                }

                @Override
                public void onError(Throwable e) {

                }
            });

        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        NetTool.getInstance().startRequest(OkHttpUtil.GET, UrlValue.IS_ADMIN_USER + MyApp.userId, ResponseUserRoleBean.class, new OnHttpCallBack<ResponseUserRoleBean>() {
            @Override
            public void onSuccess(ResponseUserRoleBean response) {
                response.doResponse(new ResponseUserRoleBean.ResponseUserRoleStatus() {
                    @Override
                    public void not_admin(String msg) {
                        SettingsActivity.this.runOnUiThread(() -> {
//                            Toast.makeText(SettingsActivity.this, msg, Toast.LENGTH_SHORT).show();
                            SettingsActivity.this.finish();
                        });
                    }

                    @Override
                    public void cannot_do(String msg) {
                        SettingsActivity.this.runOnUiThread(() -> {
//                            Toast.makeText(SettingsActivity.this, msg, Toast.LENGTH_SHORT).show();
                            SettingsActivity.this.finish();
                        });
                    }

                    @Override
                    public void doSuccess() {

                    }
                });
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }
}
