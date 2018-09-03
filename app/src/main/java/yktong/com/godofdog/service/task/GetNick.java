package yktong.com.godofdog.service.task;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;

import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.NormalResultBean;
import yktong.com.godofdog.manager.WorkManger;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.util.PerformClickUtils;
import yktong.com.godofdog.util.SPUtil;
import yktong.com.godofdog.util.SupportUtil;
import yktong.com.godofdog.value.SpValue;
import yktong.com.godofdog.value.UrlValue;

/**
 * Created by vampire on 2017/8/24.
 */

public class GetNick {
    private static final GetNick ourInstance = new GetNick();

    public static GetNick getInstance() {
        return ourInstance;
    }

    private GetNick() {
    }

    public void getWxNick(AccessibilityService service, SupportUtil supportUtil, String atyName) {
        String text = PerformClickUtils.getText(service, "com.tencent.mm:id/gp");
        if (text != null) {
            Log.d("GetNick", text);
            String username = MyApp.mSettingHelper.getString(SpValue.USER_ID, "");
            MyApp.mSettingHelper.setString(username, text);
            if (!username.isEmpty()) {
                NetTool.getInstance().startRequest("get", UrlValue.PROJECT_URL+"addNickname.action?username=" + username + "&nickname=" + text+"&userid="+MyApp.userId, NormalResultBean.class, new OnHttpCallBack<NormalResultBean>() {
                    @Override
                    public void onSuccess(NormalResultBean response) {
                        Log.d("GetNick","username : "+ username);
                        Log.d("GetNick", response.getCode());
                        if (response.getCode().equals("1")){
                            MyApp.mSettingHelper.setBoolean(SpValue.IS_CHECK_NAME, false);
                            WorkManger.getInstence().doTask(SpValue.STATE_NORMAL);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
            }

        }
    }
}
