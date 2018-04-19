package com.masalab.masato.githubfeed.view.activity.pr;

import com.masalab.masato.githubfeed.githubapi.GitHubApi;
import com.masalab.masato.githubfeed.githubapi.GitHubApiResult;
import com.masalab.masato.githubfeed.model.PullRequest;
import com.masalab.masato.githubfeed.presenter.BasePresenter;
import com.masalab.masato.githubfeed.view.PullRequestView;

/**
 * Created by Masato on 2018/02/14.
 */

public class PullRequestPresenter extends BasePresenter {

    private PullRequestView view;
    private String url;

    @Override
    public void tryAgain() {
        view.showLoadingView();
        view.hideErrorView();
        GitHubApi.getApi().fetchPullRequest(url, this::handleResult);
    }

    private void handleResult(GitHubApiResult result) {
        view.hideLoadingView();
        if (result.isSuccessful) {
            PullRequest pr = (PullRequest) result.resultObject;
            view.showPullRequest(pr);
            view.showRepoInfo(pr.repository);
        } else {
            view.showErrorView(result.failure, result.errorMessage);
        }
    }

    public PullRequestPresenter(PullRequestView view, String url) {
        this.view = view;
        this.url = url;
        view.showLoadingView();
        GitHubApi.getApi().fetchPullRequest(url, this::handleResult);
    }
}
