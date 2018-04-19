package com.masalab.masato.githubfeed.view.activity.profile;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.githubapi.Failure;
import com.masalab.masato.githubfeed.githubapi.GitHubApi;
import com.masalab.masato.githubfeed.githubapi.GitHubApiResult;
import com.masalab.masato.githubfeed.model.Profile;
import com.masalab.masato.githubfeed.presenter.BasePresenter;
import com.masalab.masato.githubfeed.view.ProfileView;

/**
 * Created by Masato on 2018/03/07.
 */

public class ProfilePresenter extends BasePresenter {

    private String url;
    private ProfileView view;
    private Profile profile;
    private boolean isFollowedByCurrentUser;
    private boolean followActionPending;

    public void onFollowActionButtonPressed() {
        if (followActionPending) {
            return;
        }
        followActionPending = true;

        if (isFollowedByCurrentUser) {
            view.showFollowButton();
            GitHubApi.getApi().unFollowUser(profile, this::handleUnFollowResult);
        } else {
            view.showUnFollowButton();
            GitHubApi.getApi().followUser(profile, this::handleFollowResult);
        }
    }

    private void handleProfileResult(GitHubApiResult result) {
        view.hideLoadingView();
        if (result.isSuccessful) {
            Profile profile = (Profile) result.resultObject;
            this.profile = profile;
            if (profile.type.toLowerCase().equals("user")) {
                view.showProfile(profile);
                GitHubApi.getApi().isFollowedByCurrentUser(profile, this::handleCheckFollowingResult);
                if (GitHubApi.getApi().getCurrentUser().name.equals(profile.name)) {
                    view.disableFollowButton();
                }
            } else if (profile.type.toLowerCase().equals("organization")) {
                view.showOrganization(profile);
            } else {
                view.showToast("Unknown user type: " + profile.type);
            }
        }
    }

    private void handleFollowResult(GitHubApiResult result) {
        if (result.isSuccessful) {
            isFollowedByCurrentUser = true;
            view.showToast(R.string.followed_user);
        } else {
            view.showFollowButton();
            view.showToast(R.string.failed_to_follow_user);
        }
        followActionPending = false;
    }

    private void handleUnFollowResult(GitHubApiResult result) {
        if (result.isSuccessful) {
            isFollowedByCurrentUser = false;
            view.showToast(R.string.unfollowed_user);
        } else {
            view.showUnFollowButton();
            view.showToast(R.string.failed_to_unfollow_user);
        }
        followActionPending = false;
    }

    private void handleCheckFollowingResult(GitHubApiResult result) {
        if (result.isSuccessful) {
            isFollowedByCurrentUser = true;
            view.showUnFollowButton();
        } else if (result.failure == Failure.NOT_FOUND){
            isFollowedByCurrentUser = false;
            view.showFollowButton();
        }
    }

    @Override
    public void tryAgain() {

    }

    public ProfilePresenter(ProfileView view, String url) {
        this.url = url;
        this.view = view;
        view.showLoadingView();
        GitHubApi.getApi().fetchProfile(url, this::handleProfileResult);
    }

}
