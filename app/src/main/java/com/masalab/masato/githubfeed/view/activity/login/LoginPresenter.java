package com.masalab.masato.githubfeed.view.activity.login;

import android.os.Handler;
import android.os.Looper;

import com.masalab.masato.githubfeed.githubapi.GitHubApi;
import com.masalab.masato.githubfeed.githubapi.GitHubApiResult;
import com.masalab.masato.githubfeed.model.Profile;
import com.masalab.masato.githubfeed.view.LoginView;

/**
 * Created by Masato on 2018/01/19.
 */

public class LoginPresenter {

    private LoginView view;

    public void onLoginButtonPressed() {
        view.startBrowser();
    }

    public void onCodeFetched(String code) {
        view.showLoginWaiting();
        view.disableLogInButton();
        GitHubApi.getApi().requestToken(code, this::execSuccessFlowIfSucceeded);
    }

    private void execSuccessFlowIfSucceeded(GitHubApiResult result) {
        if (result.isSuccessful) {
            execSuccessFlow();
        } else {
            view.enableLogInButton();
            view.showLoginError(result.failure);
        }
    }

    private void execSuccessFlow() {
        GitHubApi.getApi().fetchProfile(this::navigateToHomeViewIfSucceeded);
    }

    private void navigateToHomeViewIfSucceeded(GitHubApiResult result) {
        if (result.isSuccessful) {
            Profile profile = (Profile) result.resultObject;
            navigateToHomeView(profile);
        } else {
            view.enableLogInButton();
            view.showLoginError(result.failure);
        }
    }

    private void navigateToHomeView(Profile profile) {
        view.showProfile(profile);
        new Handler(Looper.getMainLooper()).postDelayed(view::showHomeView, 2000);
    }

    public LoginPresenter(LoginView view) {
        this.view = view;
    }
}
