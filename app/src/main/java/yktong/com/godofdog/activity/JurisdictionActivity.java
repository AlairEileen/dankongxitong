package yktong.com.godofdog.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import yktong.com.godofdog.R;
import yktong.com.godofdog.adapter.MatterAddAdapter;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.fragment.jurisdiction.JurisdictionDeptFragment;
import yktong.com.godofdog.fragment.jurisdiction.JurisdictionFragment;
import yktong.com.godofdog.fragment.jurisdiction.JurisdictionUserFragment;

public class JurisdictionActivity extends BaseActivity {

    public String[] TITLE = new String[]{"部门", "角色", "权限"};
    private MatterAddAdapter adapter;
    private JurisdictionUserFragment jurisdictionUserFragment;
    private JurisdictionDeptFragment jurisdictionDeptFragment;
    private JurisdictionFragment jurisdictionFragment;

    @Override
    protected int setLayout() {
        return R.layout.activity_jurisdiction;
    }

    @Override
    protected void initView() {
        ViewPager viewPager = bindView(R.id.vp_matter_add);
        TabLayout tabLayout = bindView(R.id.tbl_matter_add);

        JurisdictionUserFragment jurisdictionUserFragment = new JurisdictionUserFragment();
        JurisdictionDeptFragment jurisdictionDeptFragment = new JurisdictionDeptFragment();
        JurisdictionFragment jurisdictionFragment = new JurisdictionFragment();


        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(jurisdictionDeptFragment);
        fragments.add(jurisdictionUserFragment);
        fragments.add(jurisdictionFragment);
        adapter = new MatterAddAdapter(getSupportFragmentManager(), fragments, TITLE);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void initData() {

    }
}
