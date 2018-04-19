package com.masalab.masato.githubfeed.view.fragment.repooverview;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.githubapi.Failure;
import com.masalab.masato.githubfeed.githubapi.GitHubApi;
import com.masalab.masato.githubfeed.githubapi.GitHubApiResult;
import com.masalab.masato.githubfeed.model.Repository;

/**
 * Created by Masato on 2018/02/02.
 */

public class RepoOverviewPresenter implements RepoOverviewContract.Presenter {

    private RepoOverviewContract.View view;
    private Repository repository;
    private String contentHtml;
    private boolean isStarred;
    private boolean isSubscribed;
    private boolean everLoaded;

    @Override
    public void setView(RepoOverviewContract.View view) {
        this.view = view;
    }

    @Override
    public void forkedFromPressed() {
        view.showParent(repository.parent);
    }

    @Override
    public void starPressed() {
        if (isStarred) {
            unStarRepo();
        } else {
            starRepo();
        }
    }

    @Override
    public void subscribePressed() {
        if (isSubscribed) {
            unSubscribeRepo();
        } else {
            subscribeRepo();
        }
    }

    @Override
    public void tryAgain() {
        view.hideErrorView();
        loadData();
    }

    private void showData() {
        view.showOverview(repository);
        view.showReadMe(contentHtml);
        view.setStarActivated(isStarred);
        view.setWatchActivated(isSubscribed);
        if (repository.parent != null) {
            view.showForkedFrom(repository.parent);
        }
    }

    @Override
    public void loadData() {
        if (everLoaded) {
            showData();
        } else {
            view.showOverview(repository);
            if (repository.parent != null) {
                view.showForkedFrom(repository.parent);
            }
            checkIfRepoStarred(repository);
            checkIfRepoSubscribed(repository);
            fetchReadMe(repository);
            everLoaded = true;
        }
    }

    private void starRepo() {
        view.setStarActivated(true);
        GitHubApi.getApi().starRepository(repository, this::handleStarRepoResult);
    }

    private void handleStarRepoResult(GitHubApiResult result) {
        if (result.isSuccessful) {
            repository.stars++;
            view.showOverview(repository);
            view.showToast(R.string.repo_starred);
            isStarred = true;
        } else {
            view.showToast(R.string.repo_star_failed);
            view.setStarActivated(false);
        }
    }

    private void unStarRepo() {
        view.setStarActivated(false);
        GitHubApi.getApi().unStarRepository(repository, this::handleUnStarRepoResult);
    }

    private void handleUnStarRepoResult(GitHubApiResult result) {
        if (result.isSuccessful) {
            repository.stars--;
            view.showOverview(repository);
            view.showToast(R.string.repo_unstarred);
            isStarred = false;
        } else {
            view.showToast(R.string.repo_unstar_failed);
            view.setStarActivated(true);
        }
    }

    private void subscribeRepo() {
        view.setWatchActivated(true);
        GitHubApi.getApi().subscribeRepository(repository, this::handleSubscribeRepoResult);
    }

    private void handleSubscribeRepoResult(GitHubApiResult result) {
        if (result.isSuccessful) {
            repository.watches++;
            view.showOverview(repository);
            view.showToast(R.string.repo_subscribed);
            isSubscribed = true;
        } else {
            view.setWatchActivated(false);
            view.showToast(R.string.repo_subscribe_failed);
        }
    }

    private void unSubscribeRepo() {
        view.setWatchActivated(false);
        GitHubApi.getApi().unSubscribeRepository(repository, this::handleUnSubscribeRepoResult);
    }

    private void handleUnSubscribeRepoResult(GitHubApiResult result) {
        if (result.isSuccessful) {
            repository.watches--;
            view.showOverview(repository);
            view.showToast(R.string.repo_unsubscribed);
            isSubscribed = false;
        } else {
            view.setWatchActivated(true);
            view.showToast(R.string.repo_unsubscribe_failed);
        }
    }

    private void checkIfRepoStarred(Repository repository) {
        GitHubApi.getApi().isStarredByCurrentUser(repository, this::handleCheckStarResult);
    }

    private void handleCheckStarResult(GitHubApiResult result) {
        if (result.isSuccessful) {
            isStarred = true;
            view.setStarActivated(true);
        } else {
            isStarred = false;
        }
    }

    private void checkIfRepoSubscribed(Repository repository) {
        GitHubApi.getApi().isSubscribedByCurrentUser(repository, this::handleCheckSubscribeResult);
    }

    private void handleCheckSubscribeResult(GitHubApiResult result) {
        if (result.isSuccessful) {
            isSubscribed = true;
            view.setWatchActivated(true);
        } else {
            isSubscribed = false;
        }
    }

    private void fetchReadMe(Repository repository) {
        view.showLoadingView();
        GitHubApi.getApi().fetchReadMe(repository, this::handleFetchReadMeResult);
    }

    private void handleFetchReadMeResult(GitHubApiResult result) {
        view.hideLoadingView();
        if (result.isSuccessful) {
            contentHtml = (String) result.resultObject;
            view.showReadMe(contentHtml);
        } else {
            if (result.failure == Failure.NOT_FOUND) {
                view.showNoReadMe();
            } else {
                view.showErrorView(result.failure, result.errorMessage);
            }
        }
    }

    public RepoOverviewPresenter(Repository repository) {
        this.repository = repository;
    }

}
