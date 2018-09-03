package yktong.com.godofdog.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import yktong.com.godofdog.bean.sns.ClassifyBean;

/**
 * Created by Eileen on 2017/7/4.
 */

public class MatterAddAdapter extends FragmentPagerAdapter {
    private final String[] strings;
    private ArrayList<Fragment> fragments;
    private ClassifyBean bean;

    public MatterAddAdapter(FragmentManager supportFragmentManager, ArrayList<Fragment> fragments, String[] strings) {
        super(supportFragmentManager);
        this.strings = strings;
        this.fragments = fragments;
    }

    public void setBean(ClassifyBean bean) {
        this.bean = bean;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments != null ? fragments.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (null == bean) {
            return strings[position];
        } else {
            return bean.getCLibraryStort().get(position).getCLsortname();
        }
    }
}
