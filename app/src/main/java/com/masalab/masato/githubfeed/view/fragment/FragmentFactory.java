package com.masalab.masato.githubfeed.view.fragment;

import android.os.Bundle;

import com.masalab.masato.githubfeed.view.fragment.commentlist.CommentListFragment;
import com.masalab.masato.githubfeed.view.fragment.commitlist.CommitListFragment;
import com.masalab.masato.githubfeed.view.fragment.commitoverview.CommitOverviewFragment;
import com.masalab.masato.githubfeed.view.fragment.profilelist.ProfileListFragment;
import com.masalab.masato.githubfeed.view.fragment.repocontent.contentlist.ContentListFragment;
import com.masalab.masato.githubfeed.view.fragment.difffilelist.DiffFileListFragment;
import com.masalab.masato.githubfeed.view.fragment.eventlist.EventListFragment;
import com.masalab.masato.githubfeed.view.fragment.issuelist.IssueListFragment;
import com.masalab.masato.githubfeed.view.fragment.prlist.PullRequestListFragment;
import com.masalab.masato.githubfeed.view.fragment.repolist.RepoListFragment;
import com.masalab.masato.githubfeed.view.fragment.repolist.search.RepoSearchFragment;
import com.masalab.masato.githubfeed.view.fragment.repolist.trending.TrendingRepoFragment;
import com.masalab.masato.githubfeed.view.fragment.repooverview.RepoOverviewFragment;
import com.masalab.masato.githubfeed.githubapi.Failure;
import com.masalab.masato.githubfeed.githubapi.GitHubUrls;
import com.masalab.masato.githubfeed.model.Commit;
import com.masalab.masato.githubfeed.model.Issue;
import com.masalab.masato.githubfeed.model.Profile;
import com.masalab.masato.githubfeed.model.PullRequest;
import com.masalab.masato.githubfeed.model.Repository;
import com.masalab.masato.githubfeed.model.diff.DiffFile;
import com.masalab.masato.githubfeed.view.fragment.repocontent.RepoContentFragment;

import java.util.ArrayList;

/**
 * Created by Masato on 2018/02/07.
 */

public class FragmentFactory {

    public static CommentListFragment createCommentListFragment(String url, String name) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        CommentListFragment commentListFragment = new CommentListFragment();
        commentListFragment.setArguments(bundle);
        commentListFragment.setName(name);
        return commentListFragment;
    }

    public static CommentListFragment createCommentListFragment(Issue issue, String name) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("issue", issue);
        CommentListFragment commentListFragment = new CommentListFragment();
        commentListFragment.setArguments(bundle);
        commentListFragment.setName(name);
        return commentListFragment;
    }

    public static CommentListFragment createCommentListFragment(PullRequest pr, String name) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("pr", pr);
        CommentListFragment commentListFragment = new CommentListFragment();
        commentListFragment.setArguments(bundle);
        commentListFragment.setName(name);
        return commentListFragment;
    }

    public static CommitListFragment createCommitListFragment(String url, String name) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        CommitListFragment commitListFragment = new CommitListFragment();
        commitListFragment.setArguments(bundle);
        commitListFragment.setName(name);
        return commitListFragment;
    }

    public static CommitListFragment createCommitListFragment(Repository repository, String name) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("repo", repository);
        CommitListFragment commitListFragment = new CommitListFragment();
        commitListFragment.setArguments(bundle);
        commitListFragment.setName(name);
        return commitListFragment;
    }

    public static DiffFileListFragment createDiffFileListFragment(String url, String name) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        DiffFileListFragment diffFileListFragment = new DiffFileListFragment();
        diffFileListFragment.setArguments(bundle);
        diffFileListFragment.setName(name);
        return diffFileListFragment;
    }

    public static DiffFileListFragment createDiffFileListFragment(ArrayList<DiffFile> diffFiles, String name) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("diff_files", diffFiles);
        DiffFileListFragment diffFileListFragment = new DiffFileListFragment();
        diffFileListFragment.setArguments(bundle);
        diffFileListFragment.setName(name);
        return diffFileListFragment;
    }

    public static IssueListFragment createIssueListFragment(Repository repository, String name) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("repo", repository);
        IssueListFragment issueListFragment = new IssueListFragment();
        issueListFragment.setArguments(bundle);
        issueListFragment.setName(name);
        return issueListFragment;
    }

    public static IssueListFragment createIssueListFragment(String url, String name) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        IssueListFragment issueListFragment = new IssueListFragment();
        issueListFragment.setArguments(bundle);
        issueListFragment.setName(name);
        return issueListFragment;
    }

    public static IssueOverviewFragment createIssueOverviewFragment(Issue issue, String name) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("issue", issue);
        IssueOverviewFragment issueOverviewFragment = new IssueOverviewFragment();
        issueOverviewFragment.setArguments(bundle);
        issueOverviewFragment.setName(name);
        return issueOverviewFragment;
    }

    public static RepoOverviewFragment createRepoOverviewFragment(Repository repository, String name) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("repo", repository);
        RepoOverviewFragment repoOverviewFragment = new RepoOverviewFragment();
        repoOverviewFragment.setArguments(bundle);
        repoOverviewFragment.setName(name);
        return repoOverviewFragment;
    }

    public static PullRequestListFragment createPullRequestListFragment(Repository repository, String name) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("repo", repository);
        PullRequestListFragment pullRequestListFragment = new PullRequestListFragment();
        pullRequestListFragment.setName(name);
        pullRequestListFragment.setArguments(bundle);
        return pullRequestListFragment;
    }

    public static PullRequestOverviewFragment createPullRequestOverviewFragment(PullRequest pr, String name) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("pull_request", pr);
        PullRequestOverviewFragment pullRequestOverviewFragment = new PullRequestOverviewFragment();
        pullRequestOverviewFragment.setName(name);
        pullRequestOverviewFragment.setArguments(bundle);
        return pullRequestOverviewFragment;
    }

    public static EventListFragment createEventListFragment(String url, String name) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        EventListFragment eventListFragment = new EventListFragment();
        eventListFragment.setName(name);
        eventListFragment.setArguments(bundle);
        return eventListFragment;
    }

    public static EventListFragment createEventListFragment(Profile profile, String name) {
        String url = GitHubUrls.getEventUrl(profile);
        return createEventListFragment(url, name);
    }

    public static EventListFragment createOrgEventListFragment(Profile profile, String name) {
        String url = GitHubUrls.getOrgsEventUrl(profile);
        return createEventListFragment(url, name);
    }

    public static CommitOverviewFragment createCommitOverviewFragment(Commit commit, String name) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("commit", commit);
        CommitOverviewFragment commitOverviewFragment = new CommitOverviewFragment();
        commitOverviewFragment.setName(name);
        commitOverviewFragment.setArguments(bundle);
        return commitOverviewFragment;
    }

    public static ErrorFragment createErrorFragment(Failure failure, String message) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("failure", failure);
        bundle.putString("message", message);
        ErrorFragment errorFragment = new ErrorFragment();
        errorFragment.setArguments(bundle);
        return errorFragment;
    }

    public static ContentListFragment createContentListFragment(Repository repository, String name) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("repo", repository);
        ContentListFragment contentListFragment = new ContentListFragment();
        contentListFragment.setArguments(bundle);
        contentListFragment.setName(name);
        return contentListFragment;
    }

    public static RepoContentFragment createRepoContentFragment(Repository repository, String name) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("repo", repository);
        RepoContentFragment repoContentFragment = new RepoContentFragment();
        repoContentFragment.setName(name);
        repoContentFragment.setArguments(bundle);
        return repoContentFragment;
    }

    public static RepoListFragment createRepoListFragment(String url, String name) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        RepoListFragment repoListFragment = new RepoListFragment();
        repoListFragment.setName(name);
        repoListFragment.setArguments(bundle);
        return repoListFragment;
    }

    public static RepoListFragment createRepoListFragment(Profile profile, String name) {
        String url = GitHubUrls.getRepoListUrl(profile);
        return createRepoListFragment(url, name);
    }

    public static RepoListFragment createStarredRepoListFragment(Profile profile, String name) {
        String url = GitHubUrls.getStarredRepoListUrl(profile);
        return createRepoListFragment(url, name);
    }

    public static RepoListFragment createTrendingRepoListFragment(String name) {
        RepoListFragment repoListFragment = new TrendingRepoFragment();
        repoListFragment.setName(name);
        return repoListFragment;
    }

    public static RepoListFragment createOrgRepoListFragment(Profile profile, String name) {
        String url = GitHubUrls.getOrgsReposUrl(profile);
        return createRepoListFragment(url, name);
    }

    public static RepoSearchFragment createRepoSearchFragment(String query) {
        RepoSearchFragment repoSearchFragment = new RepoSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("query", query);
        repoSearchFragment.setArguments(bundle);
        return repoSearchFragment;
    }

    public static ProfileListFragment createProfileListFragment(String url, String name) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        ProfileListFragment fragment = new ProfileListFragment();
        fragment.setArguments(bundle);
        fragment.setName(name);
        return fragment;
    }

    public static ProfileListFragment createOrgMemberListFragment(Profile profile, String name) {
        String url = GitHubUrls.getOrgsMemberUrl(profile);
        return createProfileListFragment(url, name);
    }

    public static ProfileOverviewFragment createProfileOverviewFragment(Profile profile, String name) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("profile", profile);
        ProfileOverviewFragment fragment = new ProfileOverviewFragment();
        fragment.setArguments(bundle);
        fragment.setName(name);
        return fragment;
    }

    private FragmentFactory(){}
}
