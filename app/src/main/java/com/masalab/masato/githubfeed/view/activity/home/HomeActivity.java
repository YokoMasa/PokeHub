package com.masalab.masato.githubfeed.view.activity.home;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.githubapi.Failure;
import com.masalab.masato.githubfeed.githubapi.GitHubUrls;
import com.masalab.masato.githubfeed.model.Profile;
import com.masalab.masato.githubfeed.navigator.Navigator;
import com.masalab.masato.githubfeed.view.HomeView;
import com.masalab.masato.githubfeed.view.activity.BaseActivity;
import com.masalab.masato.githubfeed.view.adapter.FragmentListPagerAdapter;
import com.masalab.masato.githubfeed.view.fragment.BaseFragment;
import com.masalab.masato.githubfeed.view.fragment.eventlist.EventListFragment;
import com.masalab.masato.githubfeed.view.fragment.FragmentFactory;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Masato on 2018/01/19.
 */

public class HomeActivity extends BaseActivity implements HomeView, AdapterView.OnItemClickListener {

    private HomePresenter presenter;
    private ActionBarDrawerToggle mActionBarToggle;
    private DrawerLayout drawerLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentListPagerAdapter pagerAdapter;
    private Profile profile;
    private int savedPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.feed_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ListView listView = (ListView) findViewById(R.id.feed_nav_menu_list);
        listView.setAdapter(ArrayAdapter.createFromResource(this, R.array.nav_menu_array, R.layout.feed_nav_menu_list_element));
        listView.setOnItemClickListener(this);

        tabLayout = (TabLayout) findViewById(R.id.feed_tab_layout);
        viewPager = (ViewPager) findViewById(R.id.feed_view_pager);
        pagerAdapter = new FragmentListPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        drawerLayout = (DrawerLayout) findViewById(R.id.feed_drawer_layout);
        mActionBarToggle = new MDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(mActionBarToggle);
        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        } else {
            presenter = new HomePresenter(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu, menu);
        return true;
    }

    private void restoreState(Bundle savedInstanceState) {
        savedPage = savedInstanceState.getInt("page");
        Profile profile = savedInstanceState.getParcelable("profile");
        if (profile != null) {
            presenter = new HomePresenter(this, profile);
        } else {
            presenter = new HomePresenter(this);
        }
    }

    @Override
    public void showLoadingView() {
        showLoadingFragment(R.id.feed_mother);
    }

    @Override
    public void hideLoadingView() {
        removeLoadingFragment();
    }

    @Override
    public void onTryAgain() {

    }

    @Override
    public void showErrorView(Failure failure, String message) {
        showErrorFragment(R.id.feed_mother, failure, message);
    }

    @Override
    public void hideErrorView() {
        removeErrorFragment();
    }

    @Override
    public void setUpContent(Profile profile) {
        this.profile = profile;
        setUpFragments(profile);
        setUpDrawerContent(profile);
    }

    private void setUpDrawerContent(Profile profile) {
        CircleImageView imageView = (CircleImageView) findViewById(R.id.feed_nav_menu_icon);
        AppCompatTextView name = (AppCompatTextView) findViewById(R.id.feed_nav_menu_name);
        Picasso.with(this).load(profile.iconUrl).into(imageView);
        name.setText(profile.name);
    }

    private void setUpFragments(Profile profile) {
        EventListFragment eventListFragment =
                FragmentFactory.createEventListFragment(GitHubUrls.getEventUrl(profile), getString(R.string.tab_action));
        addFragment(eventListFragment);

        EventListFragment receivedEventListFragment =
                FragmentFactory.createEventListFragment(GitHubUrls.getReceivedEventUrl(profile), getString(R.string.tab_received_action));
        addFragment(receivedEventListFragment);
    }

    private void addFragment(BaseFragment baseFragment) {
        doSafeFTTransaction(() -> {
            pagerAdapter.addFragment(baseFragment);
            viewPager.setCurrentItem(savedPage);
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                presenter.onProfileSelected();
                break;
            case 1:
                presenter.onGlobalFeedSelected();
                break;
            case 2:
                presenter.onTrendingSelected();
                break;
            case 3:
                presenter.onLogOutSelected();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search) {
            Navigator.navigateToSearchView(this);
            return true;
        }
        if (mActionBarToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void closeDrawer() {
        drawerLayout.closeDrawers();
    }

    @Override
    public void showLogInView() {
        Navigator.navigateToLogInView(this);
    }

    @Override
    public void showGlobalFeed() {
        Navigator.navigateToGlobalFeedView(this);
    }

    @Override
    public void showTrending() {
        Navigator.navigateToTrending(this);
    }

    @Override
    public void showProfile(String url) {
        Navigator.navigateToProfileView(this, url);
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToast(int stringId) {
        Toast.makeText(this, stringId, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("page", viewPager.getCurrentItem());
        outState.putParcelable("profile", profile);
    }

    private class MDrawerToggle extends ActionBarDrawerToggle {
        public MDrawerToggle(Activity activity, DrawerLayout drawerLayout, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
        }
    }

}
