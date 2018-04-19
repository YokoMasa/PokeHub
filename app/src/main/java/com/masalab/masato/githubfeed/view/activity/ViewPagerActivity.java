package com.masalab.masato.githubfeed.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.githubapi.Failure;
import com.masalab.masato.githubfeed.view.adapter.FragmentListPagerAdapter;
import com.masalab.masato.githubfeed.view.fragment.BaseFragment;

/**
 * Created by Masato on 2018/02/03.
 */

public abstract class ViewPagerActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FragmentListPagerAdapter adapter;
    private int savedPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_view_pager_layout);
        viewPager = (ViewPager) findViewById(R.id.general_view_pager_view_pager);
        adapter = new FragmentListPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        tabLayout = (TabLayout) findViewById(R.id.general_view_pager_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.general_view_pager_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        if (savedInstanceState != null) {
            savedPage = savedInstanceState.getInt("page");
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        adapter.notifyVisibility(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void showLoadingView() {
        showLoadingFragment(R.id.general_view_pager_mother);
    }

    public void hideLoadingView() {
        removeLoadingFragment();
    }

    public void showErrorView(Failure failure, String errorMessage) {
        showErrorFragment(R.id.general_view_pager_mother, failure, errorMessage);
    }

    public void hideErrorView() {
        removeErrorFragment();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("page", viewPager.getCurrentItem());
    }

    protected void setPage(int page) {
        viewPager.setCurrentItem(page);
    }

    protected void addFragment(BaseFragment fragment) {
        doSafeFTTransaction(() -> {
            adapter.addFragment(fragment);
        });
    }

    protected void addFragment(BaseFragment fragment, int position) {
        doSafeFTTransaction(() -> {
            adapter.addFragment(fragment, position);
        });
    }

    protected void restoreViewPager() {
        viewPager.setCurrentItem(savedPage);
        adapter.notifyVisibility(savedPage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

}
