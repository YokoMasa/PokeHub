package com.masalab.masato.githubfeed.view.activity.commit;

import com.masalab.masato.githubfeed.githubapi.GitHubApi;
import com.masalab.masato.githubfeed.githubapi.GitHubApiResult;
import com.masalab.masato.githubfeed.model.Commit;
import com.masalab.masato.githubfeed.model.Repository;
import com.masalab.masato.githubfeed.view.CommitView;

/**
 * Created by Masato on 2018/02/07.
 */

public class CommitPresenter {

    private CommitView view;

    private void handleRepoResult(GitHubApiResult result) {
        if (result.isSuccessful) {
            Repository repository = (Repository) result.resultObject;
            view.showRepoInfo(repository);
        } else {
            view.showToast(result.failure.textId);
        }
    }

    private void initContent(Commit commit) {
        if (commit.repository != null) {
            view.showRepoInfo(commit.repository);
        } else {
            GitHubApi.getApi().fetchRepository(commit.getRepoUrl(), this::handleRepoResult);
        }
    }

    public CommitPresenter(CommitView view, Commit commit) {
        this.view = view;
        initContent(commit);
    }
}
