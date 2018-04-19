package com.masalab.masato.githubfeed.view;

import com.masalab.masato.githubfeed.githubapi.Failure;
import com.masalab.masato.githubfeed.model.Profile;

/**
 * Created by Masato on 2018/01/19.
 */

public interface LoginView {

    public void startBrowser();

    public void showLoginWaiting();

    public void disableLogInButton();

    public void enableLogInButton();

    public void showLoginError(Failure failure);

    public void showProfile(Profile profile);

    public void showHomeView();

    public void showToast(int stringId);

}
