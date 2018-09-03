package yktong.com.godofdog.activity;

import android.content.Intent;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import space.eileen.tools.UIMsgTools;
import yktong.com.godofdog.R;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.UserBean;
import yktong.com.godofdog.bean.UserLoginBean;
import yktong.com.godofdog.bean.user_beans.UserRequestBean;
import yktong.com.godofdog.manager.WorkManger;
import yktong.com.godofdog.tool.datebase.DBTool;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.util.SPUtil;
import yktong.com.godofdog.value.SpValue;
import yktong.com.godofdog.value.UrlValue;

/**
 * Created by vampire on 2017/6/13.
 */

public class WelActivity extends BaseActivity implements SpValue {

    @Override
    protected int setLayout() {
        return R.layout.activity_bac;
    }

    @Override
    protected void initView() {
        // 获取屏幕的默认分辨率
        Display display = getWindowManager().getDefaultDisplay();
        float width = display.getWidth() / 720;
        float height = display.getHeight() / 1280;
        Log.d("WelActivity", "width:" + width);
        Log.d("WelActivity", "height:" + height);
        SPUtil.putAndApply(MyApp.getmContext(), SCREEN_WIDE, width + "");
        SPUtil.putAndApply(MyApp.getmContext(), SCREEN_HEIGHT, height + "");
        String w = (String) SPUtil.get(MyApp.getmContext(), SCREEN_WIDE, "1");
        String h = (String) SPUtil.get(MyApp.getmContext(), SCREEN_HEIGHT, "1");
        Log.d("WelActivity", w);
        Log.d("WelActivity", h);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                testLogin();
            }
        }, 1500);

    }


    private void testLogin() {
        UserBean userBean = DBTool.getInstance().queryUser();
        if (userBean != null) {
            doLogin(userBean);
        } else {
            toLogin();
        }
    }

    private void doLogin(UserBean userBean) {
        String registrationID = JPushInterface.getRegistrationID(MyApp.getmContext());
        Log.d("WelActivity", registrationID);
//        //请求登录
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
                        doSuccess(response, userBean);

                    }

                    @Override
                    public void onError(Throwable e) {
                        UIMsgTools.showToastNetError(WelActivity.this);
                        toLogin();
                    }
                });

    }

    private void doSuccess(UserLoginBean response, UserBean userBean) {
        response.doResponse(new UserLoginBean.LoginResponseStatus() {
            @Override
            public void errorUser(String msg) {
                response.showErrorInfo(WelActivity.this, msg);
                doError(response, msg);
            }

            @Override
            public void noUser(String msg) {
                doError(response, msg);

            }

            @Override
            public void onNoCompany(String msg) {
                doError(response, msg);
            }

            @Override
            public void doSuccess() {
                response.getUser().setcPassword(userBean.getcPassword());
                MyApp.bindPersonInfo(response.getUser());
                saveToLocal(response.getUser());
                Intent intent = new Intent(WelActivity.this, MainActivity.class);
//        intent.putExtra()//用户id
                startActivity(intent);
                WelActivity.this.finish();
            }
        });

    }

    private void doError(UserLoginBean response, String msg) {
        response.showErrorInfo(WelActivity.this, msg);
        UIMsgTools.showToastNetError(WelActivity.this);
        toLogin();
    }

    private void toLogin() {
        Intent intent = new Intent(WelActivity.this, LoginActivity.class);
        SPUtil.putAndApply(MyApp.getmContext(), SpValue.IS_FIRST_TIME, true);
        mContext.startActivity(intent);
        finish();
    }

    private void saveToLocal(UserBean user) {
        DBTool.getInstance().saveUserBean(user);
    }

    @Override
    protected void initData() {
//        logInBmob();
        boolean rootAhth = WorkManger.getRootAhth();
        if (rootAhth) {

        } else {
            Toast.makeText(mContext, "未获取Root权限", Toast.LENGTH_SHORT).show();
        }
    }
}
