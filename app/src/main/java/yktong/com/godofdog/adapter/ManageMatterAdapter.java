package yktong.com.godofdog.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import yktong.com.godofdog.activity.ManageMatterActivity;
import yktong.com.godofdog.bean.sns.ClassifyBean;

/**
 * Created by vampire on 2017/6/13.
 */

public class ManageMatterAdapter extends FragmentPagerAdapter {
    private ManageMatterActivity manageMatterActivity;
    private ArrayList<Fragment> fragments;
    private ClassifyBean been;

    public ManageMatterAdapter(FragmentManager fm, ArrayList<Fragment> fragments, ManageMatterActivity manageMatterActivity) {
        super(fm);
        this.manageMatterActivity = manageMatterActivity;
        this.fragments = fragments;
    }

    public void setBeen(ClassifyBean bean) {
        this.been = bean;
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
        if (null == been) {
            return manageMatterActivity.TITLE[position];

        } else {
            been.getCLibraryStort().add(new ClassifyBean.CLibraryStortBean(6, "微头条"));
            return been.getCLibraryStort().get(position).getCLsortname();
        }

    }
}
