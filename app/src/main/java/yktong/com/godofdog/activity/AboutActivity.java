package yktong.com.godofdog.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import yktong.com.godofdog.R;
import yktong.com.godofdog.base.BaseActivity;

public class AboutActivity extends BaseActivity {


    @Override
    protected int setLayout() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        TextView tv_version = bindView(R.id.tv_version);
        initVersion(tv_version);
        LinearLayout ll_version = bindView(R.id.ll_version);
        ll_version.setOnClickListener(v -> {
            startActivity(new Intent(this, VersionListActivity.class));
        });
    }

    private void initVersion(TextView tv_version) {
        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            tv_version.setText(getString(R.string.app_name) + versionName);
        } catch (PackageManager.NameNotFoundException e) {

        }
    }

    @Override
    protected void initData() {

    }
}
