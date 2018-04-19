package com.masalab.masato.githubfeed.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.masalab.masato.githubfeed.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Masato on 2018/02/02.
 */

public class FragmentListPagerAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> fragmentList = new ArrayList<>();
    private FragmentManager mFragmentManager;
    private int currentPosition;

    public void notifyVisibility(int position) {
        if (getCount() <= position) {
            return;
        }
        Log.i("gh_feed", "visible: " + position);
        fragmentList.get(currentPosition).setVisibilityInPager(false);
        fragmentList.get(position).setVisibilityInPager(true);
        currentPosition = position;
    }

    public void addFragment(BaseFragment fragment) {
        fragmentList.add(fragment);
        notifyDataSetChanged();
    }

    public void addFragment(BaseFragment fragment, int position) {
        fragmentList.add(position, fragment);
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentList.get(position).getName();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public FragmentListPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
    }
}
