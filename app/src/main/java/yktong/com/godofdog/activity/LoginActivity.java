package yktong.com.godofdog.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import cn.jpush.android.api.JPushInterface;
import space.eileen.free_util.ProgressDialog;
import space.eileen.tools.UIMsgTools;
import space.eileen.tools.XString;
import yktong.com.godofdog.R;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.CompanyRequestBean;
import yktong.com.godofdog.bean.UserBean;
import yktong.com.godofdog.bean.UserLoginBean;
import yktong.com.godofdog.bean.user_beans.UserRequestBean;
import yktong.com.godofdog.tool.datebase.DBTool;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OkHttpUtil;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.value.UrlValue;

import static yktong.com.godofdog.activity.SignOnActivity.signCompanyId;
import static yktong.com.godofdog.activity.SignOnActivity.signOnResult;
import static yktong.com.godofdog.activity.SignOnActivity.signType;
import static yktong.com.godofdog.activity.SignOnActivity.signType_f;
import static yktong.com.godofdog.activity.SignOnActivity.signType_s;
import static yktong.com.godofdog.activity.SignOnActivity.signUserPhone;
import static yktong.com.godofdog.activity.SignOnActivity.signUserPwd;

/**
 * Created by vampire on 2017/6/20.
 */

public class LoginActivity extends BaseActivity {

    private EditText companyIdEt; //企业ID
    private EditText phoneEt; // 手机号
    private EditText et_pwd; // 验证码
    private Button requesCodeBtn; // 请求验证码
    private Button loginBtn;  // 登录
    private TextView tv_to_sign_on;
    private TextView tv_to_find_pwd;
    private ProgressDialog progressDialog;
    private LinearLayout ll_company_name;
    private TextView tv_company_name;

    @Override
    protected int setLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        bindView();
        tv_to_sign_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignOnActivity.class);
                intent.putExtra(signType, signType_s);
                startActivityForResult(intent, signOnResult);
            }
        });
        tv_to_find_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignOnActivity.class);
                intent.putExtra(signType, signType_f);
                startActivityForResult(intent, signOnResult);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });
        companyIdEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getCompanyId().trim().length() != 0) {
                    requestCompanyName(getCompanyId());
                } else {
                    cleanCompany();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    String getCompanyId() {
        return companyIdEt.getText().toString();
    }

    void cleanCompany() {
        runOnUiThread(() -> {
            ll_company_name.setVisibility(View.GONE);
        });
    }

    private void requestCompanyName(String companyId) {
        NetTool.getInstance().startRequest(OkHttpUtil.GET, UrlValue.REQUEST_COMPANY_NAME + companyId, CompanyRequestBean.class, new OnHttpCallBack<CompanyRequestBean>() {
            @Override
            public void onSuccess(CompanyRequestBean response) {
                response.doResponse(new CompanyRequestBean.CompanyNameResponseStatus() {
                    @Override
                    public void noCompany(String msg) {
                        cleanCompany();
                    }

                    @Override
                    public void doSuccess() {
                        setCompanyName(response.getCompanyName());
                    }
                });
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    void setCompanyName(String companyName) {
        runOnUiThread(() -> {
            tv_company_name.setText(companyName);
            ll_company_name.setVisibility(View.VISIBLE);
        });
    }

    @Override
    protected void initData() {

        initLogin();

    }

    private void initLogin() {
        UserBean userBean = DBTool.getInstance().queryUser();
        if (userBean != null) {
            companyIdEt.setText(userBean.getcCompanyid() + "");
            phoneEt.setText(userBean.getcLoginname() + "");
            et_pwd.setText(userBean.getcPassword() + "");
            doLogin();
        }
    }

    // 绑定布局
    private void bindView() {
        companyIdEt = bindView(R.id.company_id_et);
        phoneEt = bindView(R.id.login_phone_et);
        et_pwd = bindView(R.id.et_pwd);
        tv_to_sign_on = bindView(R.id.tv_to_sign_on);
        tv_to_find_pwd = bindView(R.id.tv_to_find_pwd);
        requesCodeBtn = bindView(R.id.login_request_code_btn);
        loginBtn = bindView(R.id.login_btn);
        ll_company_name = bindView(R.id.ll_company_name);
        tv_company_name = bindView(R.id.tv_company_name);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == signOnResult) {
            doLogin(data);
        }
    }

    private void doLogin(Intent data) {
        try {
            String companyId = data.getStringExtra(signCompanyId);
            String pwd = data.getStringExtra(signUserPwd);
            String phone = data.getStringExtra(signUserPhone);
            companyIdEt.setText(companyId);
            phoneEt.setText(phone);
            et_pwd.setText(pwd);
            doLogin();
        } catch (NullPointerException e) {
//            e.printStackTrace();
        }
    }

    private void doLogin() {
        if (checkData()) {
            if (progressDialog == null)
                progressDialog = new ProgressDialog(this, R.mipmap.loading);
            progressDialog.show();
//            CustomProgressDialog.show(this,"登录","正在登录");
            //请求登录
            String registrationID = JPushInterface.getRegistrationID(MyApp.getmContext());
            UserRequestBean userBean = new UserRequestBean();
            userBean.setCompanyId(Integer.valueOf(companyIdEt.getText().toString()));
            userBean.setPhoneNum(phoneEt.getText().toString());
            userBean.setPassword(et_pwd.getText().toString());
            userBean.setcLogintype(registrationID);
            userBean.setImei(android.os.Build.SERIAL);
            String json = new Gson().toJson(userBean);
            Log.d("#####userbean", json);
            NetTool.getInstance().postRequest(UrlValue.REQUEST_USER_LOGIN, json, UserLoginBean.class, new OnHttpCallBack<UserLoginBean>() {
                @Override
                public void onSuccess(UserLoginBean response) {
                    if (response == null) return;
                    doSuccess(response);
                    progressDialog.dismiss();
                }

                @Override
                public void onError(Throwable e) {
                    UIMsgTools.showToastNetError(LoginActivity.this);
                    progressDialog.dismiss();
                }
            });
        }
    }

    private void doSuccess(UserLoginBean response) {

        response.doResponse(new UserLoginBean.LoginResponseStatus() {
            @Override
            public void errorUser(String msg) {
                response.showErrorInfo(LoginActivity.this, msg);
            }

            @Override
            public void noUser(String msg) {
                response.showErrorInfo(LoginActivity.this, msg);
            }

            @Override
            public void onNoCompany(String msg) {
                response.showErrorInfo(LoginActivity.this, msg);
            }

            @Override
            public void doSuccess() {
                MyApp.bindPersonInfo(response.getUser());
                saveToLocal(response.getUser());
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//        intent.putExtra()//用户id
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });
    }

    private void saveToLocal(UserBean user) {
        user.setcPassword(et_pwd.getText().toString());
        DBTool.getInstance().saveUserBean(user);
    }

    private boolean checkData() {
        if (XString.isMobileNO(phoneEt.getText().toString())) {
            if (et_pwd.getText().toString().trim().length() != 0) {
                if (companyIdEt.getText().toString().trim().length() != 0) {
                    return true;

                } else {
                    Toast.makeText(this, "公司ID不能为空！", Toast.LENGTH_SHORT).show();
                    return false;
                }

            } else {
                Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(this, "手机号码有误！", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
