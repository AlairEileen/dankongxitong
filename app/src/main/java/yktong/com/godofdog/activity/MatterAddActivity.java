package yktong.com.godofdog.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import space.eileen.free_util.ProgressDialog;
import yktong.com.godofdog.R;
import yktong.com.godofdog.adapter.MatterAddAdapter;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.bean.sns.ClassifyBean;
import yktong.com.godofdog.fragment.MatterAddImgTextFragment;
import yktong.com.godofdog.fragment.MatterAddUrlFragment;

public class MatterAddActivity extends BaseActivity {
    public String[] TITLE = new String[]{"图文", "链接"};

    private MatterAddImgTextFragment matterAddImgTextFragment;
    private MatterAddUrlFragment matterAddUrlFragment;
    private MatterAddAdapter adapter;
    private ClassifyBean bean;
    private ProgressDialog progressDialogLoading;

    @Override
    protected int setLayout() {
        return R.layout.activity_matter_add;
    }
    public ProgressDialog getProgressDialogLoading() {
        if (progressDialogLoading == null)
            progressDialogLoading = new ProgressDialog(this, R.mipmap.loading);
        return progressDialogLoading;
    }
    @Override
    protected void initView() {
        ViewPager viewPager = bindView(R.id.vp_matter_add);
        TabLayout tabLayout = bindView(R.id.tbl_matter_add);
        matterAddImgTextFragment = new MatterAddImgTextFragment();
        matterAddUrlFragment = new MatterAddUrlFragment();

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(matterAddImgTextFragment);
        fragments.add(matterAddUrlFragment);
        adapter = new MatterAddAdapter(getSupportFragmentManager(), fragments, TITLE);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void initData() {


    }

    public ClassifyBean getBean() {
        return bean;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        matterAddImgTextFragment.onActivityResult(requestCode, resultCode, data);
        matterAddUrlFragment.onActivityResult(requestCode, resultCode, data);
    }
}
