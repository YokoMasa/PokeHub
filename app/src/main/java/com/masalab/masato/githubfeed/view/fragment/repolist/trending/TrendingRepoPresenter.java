package com.masalab.masato.githubfeed.view.fragment.repolist.trending;

import com.masalab.masato.githubfeed.githubapi.GitHubApi;
import com.masalab.masato.githubfeed.githubapi.GitHubApiResult;
import com.masalab.masato.githubfeed.model.BaseModel;
import com.masalab.masato.githubfeed.model.RepoSearchResult;
import com.masalab.masato.githubfeed.model.Repository;
import com.masalab.masato.githubfeed.view.fragment.paginatinglist.PaginatingListPresenter;
import com.masalab.masato.githubfeed.view.fragment.repolist.RepoListContract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Masato on 2018/03/05.
 */

public class TrendingRepoPresenter extends PaginatingListPresenter implements RepoListContract.Presenter {

    private RepoListContract.View view;
    private String q;

    @Override
    public void setView(RepoListContract.View view) {
        super.setView(view);
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
        GitHubApi.getApi().searchRepos(q, "stars", page, this::handleResult);
    }

    private void handleResult(GitHubApiResult result) {
        if (result.isSuccessful) {
            RepoSearchResult repoSearchResult = (RepoSearchResult) result.resultObject;
            onFetchSucceeded(repoSearchResult.repos);
        } else {
            onFetchFailed(result.failure, result.errorMessage);
        }
    }

    public TrendingRepoPresenter() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long sevenDaysInMillis = 7 * 24 * 60 * 60 * 1000;
        Date sevenDaysAgo = new Date(new Date().getTime() - sevenDaysInMillis);
        String dateString = dateFormat.format(sevenDaysAgo);
        q = "created:>" + dateString;
    }

}
