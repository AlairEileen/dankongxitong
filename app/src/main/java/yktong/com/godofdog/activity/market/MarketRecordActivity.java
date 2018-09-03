package yktong.com.godofdog.activity.market;

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
import yktong.com.godofdog.fragment.MarketRecordFinished;
import yktong.com.godofdog.fragment.MarketRecordUnfinished;

/**
 * Created by Eileen on 2017/7/31.
 */

public class MarketRecordActivity extends BaseActivity {

    public String[] TITLE = new String[]{"未完成任务", "已完成任务"};

    private MarketRecordFinished marketRecordFinished;
    private MarketRecordUnfinished marketRecordUnfinished;
    private MatterAddAdapter adapter;
    private ClassifyBean bean;
    private ProgressDialog progressDialogLoading;

    @Override
    protected int setLayout() {
        return R.layout.activity_market_record;
    }

    @Override
    protected void initView() {
        ViewPager viewPager = bindView(R.id.vp_matter_add);
        TabLayout tabLayout = bindView(R.id.tbl_matter_add);
        marketRecordFinished = new MarketRecordFinished();
        marketRecordUnfinished = new MarketRecordUnfinished();

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(marketRecordUnfinished);
        fragments.add(marketRecordFinished);
        adapter = new MatterAddAdapter(getSupportFragmentManager(), fragments, TITLE);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void initData() {

    }
    public ProgressDialog getProgressDialogLoading() {
        if (progressDialogLoading == null)
            progressDialogLoading = new ProgressDialog(this, R.mipmap.loading);
        return progressDialogLoading;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
