package com.masalab.masato.githubfeed.view.activity.commit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.githubapi.Failure;
import com.masalab.masato.githubfeed.model.Commit;
import com.masalab.masato.githubfeed.model.Repository;
import com.masalab.masato.githubfeed.navigator.Navigator;
import com.masalab.masato.githubfeed.view.CommitView;
import com.masalab.masato.githubfeed.view.activity.BaseActivity;
import com.masalab.masato.githubfeed.view.fragment.commitoverview.CommitOverviewFragment;
import com.masalab.masato.githubfeed.view.fragment.FragmentFactory;

/**
 * Created by Masato on 2018/02/06.
 */

public class CommitActivity extends BaseActivity implements CommitView {

    private CommitPresenter presenter;
    private Commit commit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit);
        commit = getIntent().getParcelableExtra("commit");

        Toolbar toolbar = (Toolbar) findViewById(R.id.commit_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String actionBarTitle = getString(R.string.commit_action_bar_title) + commit.getShortenedSha();
        getSupportActionBar().setTitle(actionBarTitle);

        presenter = new CommitPresenter(this, commit);

        if (savedInstanceState == null) {
            initFragment();
        }
    }

    private void initFragment() {
        doSafeFTTransaction(() -> {
            CommitOverviewFragment fragment = FragmentFactory.createCommitOverviewFragment(commit, "");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.commit_overview, fragment, "f1");
            ft.commit();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu_id_open_in_browser:
                Navigator.navigateToExternalBrowser(this, commit.htmlUrl);
                break;
            case R.id.menu_id_open_repo:
                Navigator.navigateToRepoView(this, commit.getRepoUrl());
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void showLoadingView() {
        showLoadingFragment(R.id.commit_mother);
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
        showErrorFragment(R.id.commit_mother, failure, message);
    }

    @Override
    public void hideErrorView() {
        removeErrorFragment();
    }

    @Override
    public void showRepoInfo(Repository repository) {
        getSupportActionBar().setSubtitle(repository.fullName);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
