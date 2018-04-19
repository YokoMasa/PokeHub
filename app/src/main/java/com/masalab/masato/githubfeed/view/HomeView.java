package com.masalab.masato.githubfeed.view;

import com.masalab.masato.githubfeed.model.Profile;

/**
 * Created by Masato on 2018/01/19.
 */

public interface HomeView extends BaseView{

    public void closeDrawer();

    public void setUpContent(Profile profile);

    public void showLogInView();

    public void showGlobalFeed();

    public void showTrending();

    public void showProfile(String url);

}
