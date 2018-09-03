package yktong.com.godofdog.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.mm.opensdk.utils.Log;

import java.util.Timer;
import java.util.TimerTask;

import space.eileen.free_util.ProgressDialog;
import space.eileen.tools.UIMsgTools;
import space.eileen.tools.XString;
import yktong.com.godofdog.R;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.bean.CompanyRequestBean;
import yktong.com.godofdog.bean.SignOnNextResponse;
import yktong.com.godofdog.bean.UserSignOnBean;
import yktong.com.godofdog.bean.user_beans.UserRequestBean;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OkHttpUtil;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.value.UrlValue;

import static yktong.com.godofdog.R.drawable.shape_login_un;

/**
 * Created by vampire on 2017/6/20.
 */

public class SignOnActivity extends BaseActivity {
    public static int signOnResult = 1000;
    public static String signType = "signType";
    public static String signType_s = "signOn";
    public static String signType_f = "findPwd";
    public static String signCompanyId = "user_company_id";
    public static String signUserPwd = "user_pwd";
    public static String signUserPhone = "user_phone";
    ViewManager viewManager;
    private boolean isSignOn, isFindPwd;
    private ProgressDialog progressDialog;

    @Override
    protected int setLayout() {
        return R.layout.activity_sign_on;
    }

    @Override
    protected void initView() {
        viewManager = new ViewManager();
    }

    private void doRequestCode() {
        viewManager.changeRequestCodeBtnBg(false, 60);
        NetTool.getInstance().startRequest(OkHttpUtil.POST, UrlValue.REQUEST_VERIFY_CODE + viewManager.getPhoneNumber(), Object.class, new OnHttpCallBack<Object>() {
            @Override
            public void onSuccess(Object response) {
                if (response != null) {
                    System.out.println("@#@@@@@@@@@@@@@@@@@##########requestCode:" + response.toString());
                }
            }

            @Override
            public void onError(Throwable e) {
                UIMsgTools.showToastNetError(SignOnActivity.this);

            }
        });
        viewManager.setNextRequestCode();
    }


    /**
     * 请求网络操作
     */
    private void doProcess() {
        String url = "";
        String companyId = "";
        if (isFindPwd) {
            url = UrlValue.REQUEST_FIND_PWD;
        } else if (isSignOn) {
            url = UrlValue.REQUEST_SIGN_ON;
        }
        if (viewManager.checkDataSignOnOrFind()) {
            //请求注册或者找回密码
            if (progressDialog == null)
                progressDialog = new ProgressDialog(this, R.mipmap.loading);
            progressDialog.show();
            viewManager.setSubmitEnabled(false);
            UserRequestBean userBean = new UserRequestBean();
            userBean.setCompanyId(Integer.valueOf(viewManager.getCompanyId()));
            userBean.setPhoneNum(viewManager.getPhoneNumber());
            userBean.setPassword(viewManager.getPassword());
            userBean.setVerifyCode(viewManager.getVerifyCode());
            if (isSignOn) {
                userBean.setVerifyCodeManager(viewManager.getManagerVerifyCode());
            }
            String json = new Gson().toJson(userBean);
            Log.d("#####userbean", json);
            NetTool.getInstance().postRequest(url, json, UserSignOnBean.class, new OnHttpCallBack<UserSignOnBean>() {
                @Override
                public void onSuccess(UserSignOnBean response) {
                    if (response == null) return;
                    doSuccess(response);
                    progressDialog.dismiss();
                    viewManager.setSubmitEnabled(true);
                }

                @Override
                public void onError(Throwable e) {
                    UIMsgTools.showToastNetError(SignOnActivity.this);
                    progressDialog.dismiss();
                    viewManager.setSubmitEnabled(true);
                }
            });
        }
    }

    private void doSuccess(UserSignOnBean response) {
        response.doResponse(new UserSignOnBean.SignOnResponseStatus() {
            @Override
            public void onMostUser(String msg) {
                response.showErrorInfo(SignOnActivity.this, msg);
            }

            @Override
            public void onHasUser(String msg) {
                response.showErrorInfo(SignOnActivity.this, msg);
            }

            @Override
            public void onNoCompany(String msg) {
                response.showErrorInfo(SignOnActivity.this, msg);
            }

            @Override
            public void onFailedVerifyCode(String msg) {
                response.showErrorInfo(SignOnActivity.this, msg);
            }

            @Override
            public void onTimeoutVerifyCode(String msg) {
                response.showErrorInfo(SignOnActivity.this, msg);
            }

            @Override
            public void noUser(String msg) {
                response.showErrorInfo(SignOnActivity.this, msg);
            }

            @Override
            public void error_other(String msg) {
                response.showErrorInfo(SignOnActivity.this, msg);
            }

            @Override
            public void error_manager_verify_code(String msg) {
                response.showErrorInfo(SignOnActivity.this, msg);
            }

            @Override
            public void timeout_manager_verify_code(String msg) {
                response.showErrorInfo(SignOnActivity.this, msg);
            }

            @Override
            public void doSuccess() {
                goLogin(response);
            }
        });
    }

    private void goLogin(UserSignOnBean response) {
        getIntent().putExtra(signCompanyId, response.getcUser().getcCompanyid() + "");
        getIntent().putExtra(signUserPwd, viewManager.getPassword());
        getIntent().putExtra(signUserPhone, viewManager.getPhoneNumber());
        setResult(signOnResult, getIntent());
        finish();
    }


    @Override
    protected void initData() {
        try {
            String signTypeValue = getIntent().getStringExtra(signType);
            if (signTypeValue.equals(signType_f)) {
                isFindPwd = true;
                viewManager.changeSubmitTextToFindPassword();
            } else if (signTypeValue.equals(signType_s)) {
                isSignOn = true;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void doRequestManagerCode() {
        viewManager.changeManagerRequestCodeBtnBg(false, 60);
        NetTool.getInstance().startRequest(OkHttpUtil.POST,
                UrlValue.REQUEST_MANAGER_VERIFY_CODE + viewManager.getCompanyId() +
                        UrlValue.REQUEST_MANAGER_VERIFY_CODE_PHONE + viewManager.getPhoneNumber(),
                Object.class, new OnHttpCallBack<Object>() {
                    @Override
                    public void onSuccess(Object response) {
                        if (response != null) {
                            System.out.println("@#@@@@@@@@@@@@@@@@@##########requestCode:" + response.toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        UIMsgTools.showToastNetError(SignOnActivity.this);

                    }
                });
        viewManager.setNextRequestCode();
    }

    private void doNext() {
        if (!viewManager.canNext()) return;
        //如果验证成功执行

        UserRequestBean userBean = new UserRequestBean();
        userBean.setCompanyId(Integer.valueOf(viewManager.getCompanyId()));
        userBean.setPhoneNum(viewManager.getPhoneNumber());
        userBean.setVerifyCode(viewManager.getVerifyCode());
        String json = new Gson().toJson(userBean);
        Log.d("#####userbean", json);
        NetTool.getInstance().postRequest(UrlValue.REQUEST_SIGN_ON_NEXT, json, SignOnNextResponse.class, new OnHttpCallBack<SignOnNextResponse>() {
            @Override
            public void onSuccess(SignOnNextResponse response) {
                response.doResponse(new SignOnNextResponse.SignOnNextResponseStatus() {
                    @Override
                    public void onMostUser(String msg) {
                        response.showErrorInfo(SignOnActivity.this, msg);
                    }

                    @Override
                    public void error_person_verify_code(String msg) {
                        response.showErrorInfo(SignOnActivity.this, msg);
                    }

                    @Override
                    public void timeout_person_verify_code(String msg) {
                        response.showErrorInfo(SignOnActivity.this, msg);
                    }

                    @Override
                    public void has_person(String msg) {
                        response.showErrorInfo(SignOnActivity.this, msg);
                    }

                    @Override
                    public void no_company(String msg) {
                        response.showErrorInfo(SignOnActivity.this, msg);
                    }

                    @Override
                    public void error_other(String msg) {
                        response.showErrorInfo(SignOnActivity.this, msg);
                    }

                    @Override
                    public void doSuccess() {
                        viewManager.goSubmitView();
                    }
                });
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void requestCompanyName(String companyId) {
        NetTool.getInstance().startRequest(OkHttpUtil.GET, UrlValue.REQUEST_COMPANY_NAME + companyId, CompanyRequestBean.class, new OnHttpCallBack<CompanyRequestBean>() {
            @Override
            public void onSuccess(CompanyRequestBean response) {
                response.doResponse(new CompanyRequestBean.CompanyNameResponseStatus() {
                    @Override
                    public void noCompany(String msg) {
                        viewManager.cleanCompany();
                    }

                    @Override
                    public void doSuccess() {
                        viewManager.setCompanyName(response.getCompanyName());
                    }
                });
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    class ViewManager {
        private final LinearLayout ll_sign_on_1, ll_company_name, ll_sign_on_0, ll_manager_verify_code;
        private final TextView tv_company_name, login_request_manager_code_btn, requesCodeBtn;
        private final EditText et_manager_verify_code, companyIdEt, et_pwd_pre, et_pwd, phoneEt, codeEt;
        private Button submitBtn;
        private int submitType = 0;//0.下一步，1.注 册，2.找回密码
        private boolean requestCodeWait;
        private boolean requestCodeManagerWait;

        ViewManager() {
            ll_sign_on_0 = bindView(R.id.ll_sign_on_0);
            ll_company_name = bindView(R.id.ll_company_name);
            tv_company_name = bindView(R.id.tv_company_name);

            ll_sign_on_1 = bindView(R.id.ll_sign_on_1);
            ll_manager_verify_code = bindView(R.id.ll_manager_verify_code);
            et_manager_verify_code = bindView(R.id.et_manager_verify_code);
            login_request_manager_code_btn = bindView(R.id.login_request_manager_code_btn);

            submitBtn = bindView(R.id.login_btn);

            companyIdEt = bindView(R.id.company_id_et);
            et_pwd_pre = bindView(R.id.et_pwd_pre);
            et_pwd = bindView(R.id.et_pwd);
            phoneEt = bindView(R.id.login_phone_et);
            codeEt = bindView(R.id.login_code_et);
            requesCodeBtn = bindView(R.id.login_request_code_btn);

            bindListeners();
        }

        public boolean isRequestCodeWait() {
            return requestCodeWait;
        }

        public boolean isRequestCodeManagerWait() {
            return requestCodeManagerWait;
        }

        void changeRequestCodeBtnBg(boolean canSend, int second) {
            changeRequestCodeBtnBg(requesCodeBtn, canSend, second);
        }

        void changeManagerRequestCodeBtnBg(boolean canSend, int second) {
            changeRequestCodeBtnBg(login_request_manager_code_btn, canSend, second);
        }

        private void changeRequestCodeBtnBg(TextView tv_requestCode, boolean canSend, int second) {
            runOnUiThread(() -> {
                if (canSend) {
                    tv_requestCode.setBackgroundResource(R.drawable.shape_login);
                    tv_requestCode.setText("获取验证码");
                } else {
                    tv_requestCode.setBackgroundResource(shape_login_un);
                    tv_requestCode.setText("还剩" + second + "秒再获取");
                }
            });
        }

        private void bindListeners() {

            submitBtn.setOnClickListener(v -> {
                if (submitType == 0) {
                    //执行下一步
                    doNext();
                } else if (submitType == 1) {
                    //执行注册
                    if (!checkData()) return;
                    doProcess();

                } else if (submitType == 2) {
                    //执行找回密码
                    if (!checkFindPwdData()) return;
                    doProcess();
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
                        viewManager.cleanCompany();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            requesCodeBtn.setOnClickListener(v -> {
                if (XString.isMobileNO(phoneEt.getText().toString())) {
                    doRequestCode();
                } else {
                    Toast.makeText(SignOnActivity.this, "手机号码有误！", Toast.LENGTH_SHORT).show();
                }
            });
            login_request_manager_code_btn.setOnClickListener(v -> {
                doRequestManagerCode();
            });

            phoneEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (viewManager.getPhoneNumber().trim().length() == 11) {
                        if (XString.isMobileNO(viewManager.getPhoneNumber()) && !requestCodeWait) {
                            viewManager.setSubmitEnabled(true);

                        } else {
                            viewManager.setSubmitEnabled(false);
                        }

                    } else {
                        viewManager.setSubmitEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        private boolean checkFindPwdData() {
            if (!canNext()) {
                return false;
            } else if (et_pwd.getText().toString().trim().length() == 0) {
                Toast.makeText(SignOnActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                return false;
            } else if (!et_pwd_pre.getText().toString().trim().equals(et_pwd.getText().toString().trim())) {
                Toast.makeText(SignOnActivity.this, "两次密码不相同！", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                return true;
            }
        }

        private void goSubmitView() {
            runOnUiThread(() -> {
                ll_sign_on_0.setVisibility(View.GONE);

                ll_sign_on_1.setVisibility(View.VISIBLE);
                ll_manager_verify_code.setVisibility(View.VISIBLE);

                changeSubmitTextToSubmit();
            });

        }


        void changeSubmitTextToFindPassword() {
            submitBtn.setText("找回密码");
            submitType = 2;
            ll_sign_on_1.setVisibility(View.VISIBLE);
        }

        public void changeSubmitTextToNext() {
            submitBtn.setText("下一步");
            submitType = 0;
        }

        void changeSubmitTextToSubmit() {
            submitBtn.setText("注 册");
            submitType = 1;
        }


        private boolean checkData() {
            //再加验证码判断
            if (!checkFindPwdData()) {
                return false;
            } else if (getManagerVerifyCode().trim().length() == 0) {
                Toast.makeText(SignOnActivity.this, "管理员验证码不能为空！", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                return true;
            }


        }

        private String getManagerVerifyCode() {
            return et_manager_verify_code.getText().toString();
        }

        void setNextRequestCode() {
            Timer timer = new Timer();
            if (submitType == 0) requestCodeWait = true;
            else if (submitType == 1) requestCodeManagerWait = true;
            TimerTask timerTask = new TimerTask() {
                int times = 0;
                int currentType = submitType;

                @Override
                public void run() {
                    if (currentType == 0) viewManager.changeRequestCodeBtnBg(false, 60 - times);
                    else if (currentType == 1) changeManagerRequestCodeBtnBg(false, 60 - times);
                    if (times == 60) {
                        if (currentType == 0) {
                            viewManager.changeRequestCodeBtnBg(true, 0);
                            requestCodeWait = false;
                        } else if (currentType == 1) {
                            changeManagerRequestCodeBtnBg(true, 0);
                            requestCodeManagerWait = false;
                        }
                        timer.cancel();
                    }
                    times++;
                }
            };
            timer.schedule(timerTask, 0, 1000);
        }

        String getPassword() {
            return et_pwd.getText().toString();
        }

        String getPhoneNumber() {

            return phoneEt.getText().toString();
        }

        void setSubmitEnabled(boolean enabled) {
            requesCodeBtn.setBackgroundResource(enabled ? R.drawable.shape_login : R.drawable.shape_login_un);
            requesCodeBtn.setEnabled(enabled);
        }

        String getVerifyCode() {
            return codeEt.getText().toString();
        }

        String getCompanyId() {
            return companyIdEt.getText().toString();
        }

        void cleanCompany() {
            runOnUiThread(() -> {
                ll_company_name.setVisibility(View.GONE);
            });
        }

        void setCompanyName(String companyName) {
            runOnUiThread(() -> {
                tv_company_name.setText(companyName);
                ll_company_name.setVisibility(View.VISIBLE);
            });
        }

        boolean canNext() {
            if (getCompanyId().trim().length() == 0) {
                Toast.makeText(SignOnActivity.this, "公司ID不能为空！", Toast.LENGTH_SHORT).show();
                return false;
            } else if (!XString.isMobileNO(getPhoneNumber())) {
                Toast.makeText(SignOnActivity.this, "手机号码有误！", Toast.LENGTH_SHORT).show();
                return false;
            } else if (getVerifyCode().trim().length() == 0) {
                Toast.makeText(SignOnActivity.this, "验证码不能为空！", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                return true;
            }
        }

        public boolean checkDataSignOnOrFind() {
            if (submitType == 1) {
                return checkData();
            } else if (submitType == 2) {
                return checkFindPwdData();
            }
            return false;
        }
    }


}
