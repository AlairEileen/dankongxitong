package yktong.com.godofdog.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import yktong.com.godofdog.value.Strings;

/**
 * Created by vampire on 2017/6/13.
 */

public class MainAdapter extends FragmentPagerAdapter implements Strings {
    private ArrayList<Fragment> fragments;

    public MainAdapter(FragmentManager fm) {
        super(fm);
    }

    public MainAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
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

//        ImageSpan imageSpan=new ImageSpan(getItem(position).getContext(),UNSELECTED[position]);
//        SpannableString spannableString =new SpannableString(" ");
//        spannableString.setSpan(imageSpan,0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


//        return TITLE[position];
        return null;
    }

}
