package com.masalab.masato.githubfeed.view.activity.issue;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.model.Issue;
import com.masalab.masato.githubfeed.model.Repository;
import com.masalab.masato.githubfeed.navigator.Navigator;
import com.masalab.masato.githubfeed.view.IssueView;
import com.masalab.masato.githubfeed.view.activity.ViewPagerActivity;
import com.masalab.masato.githubfeed.view.fragment.commentlist.CommentListFragment;
import com.masalab.masato.githubfeed.view.fragment.FragmentFactory;
import com.masalab.masato.githubfeed.view.fragment.IssueOverviewFragment;

/**
 * Created by Masato on 2018/02/03.
 */

public class IssueActivity extends ViewPagerActivity implements IssueView {

    private Issue issue;
    private IssuePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
           restoreState(savedInstanceState);
        } else {
            String url = getIntent().getStringExtra("url");
            presenter = new IssuePresenter(this, url);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_menu1, menu);
        return true;
    }


    private void restoreState(Bundle savedInstanceState) {
        Issue issue = savedInstanceState.getParcelable("issue");
        if (issue != null) {
            showIssue(issue);
        } else {
            String url = getIntent().getStringExtra("url");
            presenter = new IssuePresenter(this, url);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu_id_open_in_browser:
                Navigator.navigateToExternalBrowser(this, issue.htmlUrl);
                break;
            case R.id.menu_id_open_repo:
                Navigator.navigateToRepoView(this, issue.repoUrl);
                break;
        }
        return true;
    }

    @Override
    public void onTryAgain() {
        presenter.tryAgain();
    }

    @Override
    public void showIssue(Issue issue) {
        this.issue = issue;
        String title = getString(R.string.issue_title) + " #" + issue.number;
        getSupportActionBar().setTitle(title);
        IssueOverviewFragment issueOverviewFragment =
                FragmentFactory.createIssueOverviewFragment(issue, getString(R.string.tab_overview));
        addFragment(issueOverviewFragment);

        CommentListFragment commentListFragment =
                FragmentFactory.createCommentListFragment(issue, getString(R.string.tab_comments));
        addFragment(commentListFragment);

        restoreViewPager();
    }

    @Override
    public void showRepoInfo(Repository repository) {
        getSupportActionBar().setSubtitle(repository.fullName);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable("issue", issue);
    }
}
