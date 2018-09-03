package yktong.com.godofdog.activity;

import android.widget.TextView;

import space.eileen.free_util.ProgressDialog;
import yktong.com.godofdog.R;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.bean.versons.AppUpdateInfoResponseBean;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OkHttpUtil;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.value.UrlValue;

public class VersionInfoActivity extends BaseActivity {

    public final static String VERSION_ID = "VERSION_ID";
    private TextView content;
    private ProgressDialog progressDialogLoading;


    @Override
    protected int setLayout() {
        return R.layout.activity_version_info;
    }

    @Override
    protected void initView() {
        content = bindView(R.id.tv_update_content);
    }

    @Override
    protected void initData() {
        int versionId=getIntent().getIntExtra(VERSION_ID,-1);
        if (versionId==-1)return;
        if (progressDialogLoading == null)
            progressDialogLoading = new ProgressDialog(this, R.mipmap.loading);
        progressDialogLoading.show();
        NetTool.getInstance().startRequest(OkHttpUtil.GET, UrlValue.REQUEST_VERSION_INFO+versionId, AppUpdateInfoResponseBean.class, new OnHttpCallBack<AppUpdateInfoResponseBean>() {
            @Override
            public void onSuccess(AppUpdateInfoResponseBean response) {
                if (progressDialogLoading!=null)progressDialogLoading.dismiss();
                response.doResponse(() -> {
                    runOnUiThread(() -> {
                        content.setText(response.getAppUpdateInfoBean().getcUpdatecontent());
                    });
                });
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }
}
