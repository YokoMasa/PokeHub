package com.masalab.masato.githubfeed.view.fragment.repolist;

import com.masalab.masato.githubfeed.githubapi.GitHubApi;
import com.masalab.masato.githubfeed.githubapi.GitHubApiResult;
import com.masalab.masato.githubfeed.model.BaseModel;
import com.masalab.masato.githubfeed.model.Repository;
import com.masalab.masato.githubfeed.view.fragment.paginatinglist.PaginatingListPresenter;

import java.util.List;

/**
 * Created by Masato on 2018/03/04.
 */

public class RepoListPresenter extends PaginatingListPresenter implements RepoListContract.Presenter {

    private RepoListContract.View view;
    private String url;

    @Override
    public void setView(RepoListContract.View view) {
        this.view = view;
    }

    @Override
    public int onGetPaginatingItemViewType(BaseModel element) {
        return 0;
    }

    @Override
    public void onElementClicked(BaseModel element, int viewType) {
        Repository repository = (Repository) element;
        view.showRepo(repository);
    }

    @Override
    protected void onFetchElement(int page) {
        GitHubApi.getApi().fetchRepos(url, page, this::handleResult);
    }

    private void handleResult(GitHubApiResult result) {
        if (result.isSuccessful) {
            List<Repository> repositories = (List<Repository>)result.resultObject;
            onFetchSucceeded(repositories);
        } else {
            onFetchFailed(result.failure, result.errorMessage);
        }
    }

    public RepoListPresenter(String url) {
        this.url = url;
    }

}
