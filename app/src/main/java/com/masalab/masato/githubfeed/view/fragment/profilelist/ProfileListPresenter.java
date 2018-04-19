package com.masalab.masato.githubfeed.view.fragment.profilelist;

import com.masalab.masato.githubfeed.githubapi.GitHubApi;
import com.masalab.masato.githubfeed.githubapi.GitHubApiResult;
import com.masalab.masato.githubfeed.model.BaseModel;
import com.masalab.masato.githubfeed.model.Profile;
import com.masalab.masato.githubfeed.view.fragment.paginatinglist.PaginatingListPresenter;

import java.util.List;

/**
 * Created by Masato on 2018/03/10.
 */

public class ProfileListPresenter extends PaginatingListPresenter implements ProfileListContract.Presenter {

    private ProfileListContract.View view;
    private String url;

    @Override
    public void setView(ProfileListContract.View view) {
        this.view = view;
    }

    @Override
    public void onElementClicked(BaseModel element, int viewType) {
        Profile profile = (Profile) element;
        view.showProfile(profile);
    }

    @Override
    protected int onGetPaginatingItemViewType(BaseModel element) {
        return 0;
    }

    @Override
    protected void onFetchElement(int page) {
        GitHubApi.getApi().fetchProfileList(url, page, this::handleResult);
    }

    private void handleResult(GitHubApiResult result) {
        if (result.isSuccessful) {
            List<Profile> profiles = (List<Profile>) result.resultObject;
            onFetchSucceeded(profiles);
        } else {
            onFetchFailed(result.failure, result.errorMessage);
        }
    }

    public ProfileListPresenter(String url) {
        this.url = url;
    }
}
