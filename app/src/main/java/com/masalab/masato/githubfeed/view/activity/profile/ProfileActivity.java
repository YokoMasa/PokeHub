package com.masalab.masato.githubfeed.view.activity.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.githubapi.Failure;
import com.masalab.masato.githubfeed.githubapi.GitHubUrls;
import com.masalab.masato.githubfeed.model.Profile;
import com.masalab.masato.githubfeed.navigator.Navigator;
import com.masalab.masato.githubfeed.view.ProfileView;
import com.masalab.masato.githubfeed.view.activity.BaseActivity;
import com.masalab.masato.githubfeed.view.adapter.FragmentListPagerAdapter;
import com.masalab.masato.githubfeed.view.fragment.BaseFragment;
import com.masalab.masato.githubfeed.view.fragment.eventlist.EventListFragment;
import com.masalab.masato.githubfeed.view.fragment.FragmentFactory;
import com.masalab.masato.githubfeed.view.fragment.ProfileOverviewFragment;
import com.masalab.masato.githubfeed.view.fragment.profilelist.ProfileListFragment;
import com.masalab.masato.githubfeed.view.fragment.repolist.RepoListFragment;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Masato on 2018/03/06.
 */

public class ProfileActivity extends BaseActivity implements ProfileView {

    private FragmentListPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private ProfilePresenter presenter;
    private int savedPage;
    private Profile profile;
    private boolean isOrg;
    private boolean menuAdded;
    private boolean followButtonDisabled;
    private int followButtonIconRes = R.drawable.user_add;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.profile_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pagerAdapter = new FragmentListPagerAdapter(getSupportFragmentManager());
        TabLayout tabLayout = (TabLayout) findViewById(R.id.profile_tab_layout);
        viewPager = (ViewPager) findViewById(R.id.profile_view_pager);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        } else {
            setUpPresenter();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (profile == null) {
            return false;
        }

        if (isOrg) {
            getMenuInflater().inflate(R.menu.activity_menu2, menu);
        } else {
            getMenuInflater().inflate(R.menu.activity_menu4, menu);
            MenuItem menuItem = menu.findItem(R.id.menu_id_follow_action);
            if (menuItem != null) {
                menuItem.setIcon(followButtonIconRes);
            }
            if (followButtonDisabled) {
                menu.removeItem(R.id.menu_id_follow_action);
            }
        }
        menuAdded = true;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        /*
        if (profile == null) {
            return false;
        }

        if (menuAdded) {
            MenuItem menuItem = menu.findItem(R.id.menu_id_follow_action);
            if (menuItem != null) {
                menuItem.setIcon(followButtonIconRes);
            }
            if (followButtonDisabled) {
                menu.removeItem(R.id.menu_id_follow_action);
            }
        } else {
            if (isOrg) {
                getMenuInflater().inflate(R.menu.activity_menu2, menu);
            } else {
                getMenuInflater().inflate(R.menu.activity_menu4, menu);
            }
            menuAdded = true;
        }
        */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_id_open_in_browser:
                if (profile != null) {
                    Navigator.navigateToExternalBrowser(this, profile.htmlUrl);
                }
                break;
            case R.id.menu_id_follow_action:
                presenter.onFollowActionButtonPressed();
                break;
        }
        return true;
    }

    private void setUpPresenter() {
        String url = getIntent().getStringExtra("url");
        presenter = new ProfilePresenter(this, url);
    }

    private void restoreState(Bundle savedInstanceState) {
        savedPage = savedInstanceState.getInt("page");
        profile = savedInstanceState.getParcelable("profile");

        if (profile != null) {
            showProfile(profile);
        } else {
            setUpPresenter();
        }
    }

    private void addFragment(BaseFragment fragment) {
        doSafeFTTransaction(() -> {
            pagerAdapter.addFragment(fragment);
        });
    }

    @Override
    public void showFollowButton() {
        followButtonIconRes = R.drawable.user_add;
        invalidateOptionsMenu();
    }

    @Override
    public void showUnFollowButton() {
        followButtonIconRes = R.drawable.user_remove;
        invalidateOptionsMenu();
    }

    @Override
    public void disableFollowButton() {
        followButtonDisabled = true;
        invalidateOptionsMenu();
    }

    @Override
    public void showProfile(Profile profile) {
        this.profile = profile;
        setUpHeader(profile.name, profile.iconUrl);
        setUpProfileFragments(profile);
        invalidateOptionsMenu();
    }

    private void setUpProfileFragments(Profile profile) {
        ProfileOverviewFragment profileOverviewFragment =
                FragmentFactory.createProfileOverviewFragment(profile, getString(R.string.tab_overview));
        addFragment(profileOverviewFragment);

        EventListFragment eventListFragment = FragmentFactory.createEventListFragment(profile, getString(R.string.tab_action));
        addFragment(eventListFragment);

        RepoListFragment repoListFragment = FragmentFactory.createRepoListFragment(profile, getString(R.string.tab_repository));
        addFragment(repoListFragment);

        RepoListFragment starredFragment = FragmentFactory.createStarredRepoListFragment(profile, getString(R.string.tab_starred));
        addFragment(starredFragment);

        ProfileListFragment following =
                FragmentFactory.createProfileListFragment(GitHubUrls.getFollowingsUrl(profile), getString(R.string.tab_following));
        addFragment(following);

        ProfileListFragment followers =
                FragmentFactory.createProfileListFragment(GitHubUrls.getFollowersUrl(profile), getString(R.string.tab_followers));
        addFragment(followers);
    }

    @Override
    public void showOrganization(Profile profile) {
        this.profile = profile;
        this.isOrg = true;
        setUpHeader(profile.name, profile.iconUrl);
        setUpOrgFragments(profile);
        invalidateOptionsMenu();
    }

    private void setUpOrgFragments(Profile profile) {
        ProfileOverviewFragment profileOverviewFragment =
                FragmentFactory.createProfileOverviewFragment(profile, getString(R.string.tab_overview));
        addFragment(profileOverviewFragment);

        EventListFragment eventListFragment = FragmentFactory.createOrgEventListFragment(profile, getString(R.string.tab_action));
        addFragment(eventListFragment);

        RepoListFragment repoListFragment = FragmentFactory.createOrgRepoListFragment(profile, getString(R.string.tab_repository));
        addFragment(repoListFragment);

        ProfileListFragment profileListFragment = FragmentFactory.createOrgMemberListFragment(profile, getString(R.string.tab_members));
        addFragment(profileListFragment);
    }

    private void setUpHeader(String name, String imageUrl) {
        doSafeFTTransaction(() -> {
            AppCompatTextView nameView = findViewById(R.id.profile_name);
            CircleImageView imageView = (CircleImageView) findViewById(R.id.profile_image);
            nameView.setText(name);
            Picasso.with(this).load(imageUrl).into(imageView);
            CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.profile_collapsing_toolbar);
            collapsingToolbarLayout.setTitle(name);
        });
    }

    @Override
    public void showErrorView(Failure failure, String message) {
        showErrorFragment(R.id.profile_mother, failure, message);
    }

    @Override
    public void hideErrorView() {
        removeErrorFragment();
    }

    @Override
    public void showLoadingView() {
        showLoadingFragment(R.id.profile_mother);
    }

    @Override
    public void hideLoadingView() {
        removeLoadingFragment();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onTryAgain() {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("page", viewPager.getCurrentItem());
        outState.putParcelable("profile", profile);
    }
}
