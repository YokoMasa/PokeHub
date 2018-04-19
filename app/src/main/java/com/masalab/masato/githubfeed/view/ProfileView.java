package com.masalab.masato.githubfeed.view;

import com.masalab.masato.githubfeed.model.Profile;

/**
 * Created by Masato on 2018/03/07.
 */

public interface ProfileView extends BaseView {
    public void showProfile(Profile profile);

    public void showOrganization(Profile profile);

    public void showFollowButton();

    public void showUnFollowButton();

    public void disableFollowButton();
}
