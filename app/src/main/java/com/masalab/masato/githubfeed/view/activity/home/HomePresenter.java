package com.masalab.masato.githubfeed.view.activity.home;

import com.masalab.masato.githubfeed.githubapi.GitHubApi;
import com.masalab.masato.githubfeed.githubapi.GitHubApiResult;
import com.masalab.masato.githubfeed.model.Profile;
import com.masalab.masato.githubfeed.presenter.BasePresenter;
import com.masalab.masato.githubfeed.view.HomeView;

/**
 * Created by Masato on 2018/01/19.
 */

public class HomePresenter extends BasePresenter {

    private HomeView view;
    private Profile profile;

    @Override
    public void tryAgain() {
        view.hideErrorView();
        view.showLoadingView();
        GitHubApi.getApi().fetchProfile(this::handleFetchProfileResult);
    }

    public void onLogOutSelected() {
        GitHubApi.getApi().deleteToken();
        view.showLogInView();
    }

    public void onGlobalFeedSelected() {
        view.closeDrawer();
        view.showGlobalFeed();
    }

    public void onTrendingSelected() {
        view.closeDrawer();
        view.showTrending();
    }

    public void onProfileSelected() {
        view.closeDrawer();
        view.showProfile(profile.url);
    }

    private void handleFetchProfileResult(GitHubApiResult result) {
        view.hideLoadingView();
        if (result.isSuccessful) {
            profile = (Profile) result.resultObject;
            view.setUpContent(profile);
        } else {
            view.showErrorView(result.failure, result.errorMessage);
        }
    }

    public HomePresenter(HomeView feedView) {
        this.view = feedView;
        view.showLoadingView();
        GitHubApi.getApi().fetchProfile(this::handleFetchProfileResult);
    }

    public HomePresenter(HomeView homeView, Profile profile) {
        this.view = homeView;
        this.profile = profile;
        view.setUpContent(profile);
    }
}
