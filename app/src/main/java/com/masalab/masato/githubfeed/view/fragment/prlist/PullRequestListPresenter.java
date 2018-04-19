package com.masalab.masato.githubfeed.view.fragment.prlist;

import com.masalab.masato.githubfeed.view.fragment.paginatinglist.PaginatingListPresenter;
import com.masalab.masato.githubfeed.githubapi.GitHubApi;
import com.masalab.masato.githubfeed.githubapi.GitHubApiResult;
import com.masalab.masato.githubfeed.model.BaseModel;
import com.masalab.masato.githubfeed.model.PullRequest;
import com.masalab.masato.githubfeed.model.Repository;

import java.util.List;

/**
 * Created by Masato on 2018/02/08.
 */

public class PullRequestListPresenter extends PaginatingListPresenter implements PullRequestListContract.Presenter {

    private Repository repository;
    private PullRequestListContract.View view;

    @Override
    public void setView(PullRequestListContract.View view) {
        super.setView(view);
        this.view = view;
    }

    @Override
    public int onGetPaginatingItemViewType(BaseModel element) {
        return 0;
    }

    @Override
    public void onElementClicked(BaseModel element, int viewType) {
        PullRequest pr = (PullRequest) element;
        view.showPullRequest(pr);
    }

    @Override
    protected void onFetchElement(int page) {
        GitHubApi.getApi().fetchPullRequestList(repository, page, this::handleResult);
    }

    private void handleResult(GitHubApiResult result) {
        if (result.isSuccessful) {
            List<BaseModel> prList = (List<BaseModel>) result.resultObject;
            onFetchSucceeded(prList);
        } else {
            onFetchFailed(result.failure, result.errorMessage);
        }
    }

    public PullRequestListPresenter(Repository repository) {
        this.repository = repository;
    }

}
