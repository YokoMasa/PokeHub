package com.masalab.masato.githubfeed.view.activity.repo;

import com.masalab.masato.githubfeed.githubapi.GitHubApi;
import com.masalab.masato.githubfeed.githubapi.GitHubApiResult;
import com.masalab.masato.githubfeed.model.Repository;
import com.masalab.masato.githubfeed.presenter.BasePresenter;
import com.masalab.masato.githubfeed.view.RepoView;

/**
 * Created by Masato on 2018/01/27.
 */

public class RepoPresenter extends BasePresenter {

    private RepoView view;
    private String repoUrl;

    @Override
    public void tryAgain() {
        view.showLoadingView();
        view.hideErrorView();
        GitHubApi.getApi().fetchRepository(repoUrl, this::handleFetchRepositoryResult);
    }

    private void handleFetchRepositoryResult(GitHubApiResult result) {
        view.hideLoadingView();
        if (result.isSuccessful) {
            Repository repository = (Repository) result.resultObject;
            view.setUpContent(repository);
        } else {
            view.showErrorView(result.failure, result.errorMessage);
        }
    }

    public RepoPresenter(RepoView view, String url) {
        this.view = view;
        this.repoUrl = url;
        view.showLoadingView();
        GitHubApi.getApi().fetchRepository(repoUrl, this::handleFetchRepositoryResult);
    }
}
