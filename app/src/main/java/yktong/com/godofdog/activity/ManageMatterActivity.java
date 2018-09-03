package yktong.com.godofdog.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;

import space.eileen.free_util.ProgressDialog;
import yktong.com.godofdog.R;
import yktong.com.godofdog.activity.market.MarketingRegularActivity;
import yktong.com.godofdog.adapter.ManageMatterAdapter;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.bean.sns.ClassifyBean;
import yktong.com.godofdog.fragment.MatterManageCaseFragment;
import yktong.com.godofdog.fragment.MatterManageEventFragment;
import yktong.com.godofdog.fragment.MatterManageJokeFragment;
import yktong.com.godofdog.fragment.MatterManageOtherFragment;
import yktong.com.godofdog.fragment.MatterManageProductFragment;
import yktong.com.godofdog.fragment.MatterWechatFragment;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.value.UrlValue;

public class ManageMatterActivity extends BaseActivity {
    public String[] TITLE = new String[]{"产品", "案例", "活动", "笑话", "其他", "微头条"};
    public int code;
    MatterManageProductFragment matterManageProductFragment;
    MatterManageCaseFragment matterManageCaseFragment;
    MatterManageEventFragment matterManageEventFragment;
    MatterManageJokeFragment matterManageJokeFragment;
    MatterManageOtherFragment matterManageOtherFragment;
    private ManageMatterAdapter adapter;
    private MatterWechatFragment matterWechatFragment;
    private ProgressDialog progressDialogLoading;
    @Override
    protected int setLayout() {
        return R.layout.activity_manage_matter;
    }

    @Override
    protected void initView() {
        ViewPager viewPager = bindView(R.id.vp_matter_manage);
        TabLayout tabLayout = bindView(R.id.tbl_matter_manage);
        LinearLayout ll_search = bindView(R.id.ll_search);
        EditText et_search = bindView(R.id.et_search);
        et_search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String param = et_search.getText().toString();
                if (param.trim().length() == 0) return false;
                Intent intent = new Intent(this, MatterSearchActivity.class);
                intent.putExtra(MatterSearchActivity.SEARCH_PARAM, param);
                intent.putExtra("code", code);
                startActivityForResult(intent, MatterSearchActivity.RESULT_MANAGE_MATTER);
                return true;
            }
            return false;
        });


        ArrayList<Fragment> fragments = new ArrayList<>();
        matterManageProductFragment = new MatterManageProductFragment();
        matterManageCaseFragment = new MatterManageCaseFragment();
        matterManageEventFragment = new MatterManageEventFragment();
        matterManageJokeFragment = new MatterManageJokeFragment();
        matterManageOtherFragment = new MatterManageOtherFragment();
        matterWechatFragment = new MatterWechatFragment();
        fragments.add(matterManageProductFragment);
        fragments.add(matterManageCaseFragment);
        fragments.add(matterManageEventFragment);
        fragments.add(matterManageJokeFragment);
        fragments.add(matterManageOtherFragment);
        fragments.add(matterWechatFragment);
        adapter = new ManageMatterAdapter(getSupportFragmentManager(), fragments, this);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        setIndicator(tabLayout, 13, 13);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 5) ll_search.setVisibility(View.GONE);
                else ll_search.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    @Override
    protected void initData() {
        try {
            code = getIntent().getIntExtra("code", -1);

        } catch (NullPointerException e) {

        }

        NetTool.getInstance().startRequest("get", UrlValue.CLASSIFY_LIST, ClassifyBean.class, new OnHttpCallBack<ClassifyBean>() {
            @Override
            public void onSuccess(ClassifyBean response) {
                if (response.getCode().equals("1")) {
                    adapter.setBeen(response);
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void goMatterAdd(View view) {
        Intent intent = new Intent(this, MatterAddActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        matterManageProductFragment.onActivityResult(requestCode, resultCode, data);
        matterManageCaseFragment.onActivityResult(requestCode, resultCode, data);
        matterManageEventFragment.onActivityResult(requestCode, resultCode, data);
        matterManageJokeFragment.onActivityResult(requestCode, resultCode, data);
        matterManageOtherFragment.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MarketingRegularActivity.REQUEST_MATTER_DATA_CODE) {
            setResult(MarketingRegularActivity.REQUEST_MATTER_DATA_CODE, data);
            finish();
        }
    }

    public ProgressDialog getProgressDialogLoading() {
        if (progressDialogLoading == null)
            progressDialogLoading = new ProgressDialog(this, R.mipmap.loading);
        return progressDialogLoading;
    }
}
