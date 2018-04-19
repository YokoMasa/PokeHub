package com.masalab.masato.githubfeed.view;

/**
 * Created by Masato on 2018/02/07.
 */

public interface MainView {
    public void showLogInView();

    public void showHomeView();

    public void showToast(int stringId);

    public void showTryAgainDialog();
}
