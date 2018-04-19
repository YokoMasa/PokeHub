package com.masalab.masato.githubfeed.view.activity.repo;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.model.Repository;
import com.masalab.masato.githubfeed.navigator.Navigator;
import com.masalab.masato.githubfeed.view.RepoView;
import com.masalab.masato.githubfeed.view.activity.ViewPagerActivity;
import com.masalab.masato.githubfeed.view.fragment.commitlist.CommitListFragment;
import com.masalab.masato.githubfeed.view.fragment.FragmentFactory;
import com.masalab.masato.githubfeed.view.fragment.issuelist.IssueListFragment;
import com.masalab.masato.githubfeed.view.fragment.prlist.PullRequestListFragment;
import com.masalab.masato.githubfeed.view.fragment.repocontent.RepoContentFragment;
import com.masalab.masato.githubfeed.view.fragment.repooverview.RepoOverviewFragment;

/**
 * Created by Masato on 2018/01/27.
 */

public class RepoActivity extends ViewPagerActivity implements RepoView {

    private Repository repository;
    private RepoPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        } else {
            String url = getIntent().getStringExtra("url");
            presenter = new RepoPresenter(this, url);
        }
    }

    private void restoreState(Bundle savedInstanceState) {
        Repository repository = savedInstanceState.getParcelable("repository");
        if (repository != null) {
            setUpContent(repository);
        } else {
            String url = getIntent().getStringExtra("url");
            presenter = new RepoPresenter(this, url);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_menu3, menu);
        return true;
    }

    @Override
    public void onTryAgain() {
        presenter.tryAgain();
    }

    @Override
    public void setUpContent(Repository repository) {
        this.repository = repository;
        getSupportActionBar().setTitle(repository.name);
        getSupportActionBar().setSubtitle(repository.owner);

        RepoOverviewFragment overviewFragment =
                FragmentFactory.createRepoOverviewFragment(repository, getString(R.string.tab_overview));
        addFragment(overviewFragment);

        RepoContentFragment repoContentFragment =
                FragmentFactory.createRepoContentFragment(repository, getString(R.string.tab_code));
        addFragment(repoContentFragment);

        CommitListFragment commitListFragment =
                FragmentFactory.createCommitListFragment(repository, getString(R.string.tab_commits));
        addFragment(commitListFragment);

        IssueListFragment issueListFragment =
                FragmentFactory.createIssueListFragment(repository, getString(R.string.tab_issues));
        addFragment(issueListFragment);

        PullRequestListFragment pullRequestListFragment =
                FragmentFactory.createPullRequestListFragment(repository, getString(R.string.tab_pull_requests));
        addFragment(pullRequestListFragment);

        restoreViewPager();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu_id_open_in_browser:
                if (repository != null) {
                    Navigator.navigateToExternalBrowser(this, repository.htmlUrl);
                }
                break;
            case R.id.menu_id_owner:
                if (repository != null) {
                    Navigator.navigateToProfileView(this, repository.ownerProfile.url);
                }
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable("repository", repository);
    }
}
