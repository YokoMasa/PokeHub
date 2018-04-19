package com.masalab.masato.githubfeed.view.fragment.repolist.search;

import com.masalab.masato.githubfeed.githubapi.GitHubApi;
import com.masalab.masato.githubfeed.githubapi.GitHubApiResult;
import com.masalab.masato.githubfeed.model.BaseModel;
import com.masalab.masato.githubfeed.model.RepoSearchResult;
import com.masalab.masato.githubfeed.model.Repository;
import com.masalab.masato.githubfeed.view.fragment.paginatinglist.PaginatingListPresenter;
import com.masalab.masato.githubfeed.view.fragment.repolist.RepoListContract;

import java.util.ArrayList;

/**
 * Created by Masato on 2018/03/06.
 */

public class RepoSearchPresenter extends PaginatingListPresenter implements RepoSearchContract.Presenter {

    private RepoListContract.View view;
    private String q = "";

    @Override
    public void setView(RepoListContract.View view) {
        this.view = view;
    }

    @Override
    public void onSearchQuery(String q) {
        this.q = q;
        if (view != null) {
            reset();
        }
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
        GitHubApi.getApi().searchRepos(q, null, page, this::handleResult);
    }

    private void handleResult(GitHubApiResult result) {
        if (result.isSuccessful) {
            RepoSearchResult repoSearchResult = (RepoSearchResult) result.resultObject;
            if (repoSearchResult.q.equals(this.q)) {
                onFetchSucceeded(repoSearchResult.repos);
            }
        } else {
            onFetchSucceeded(new ArrayList<Repository>());
        }
    }

    public RepoSearchPresenter(String query) {
        this.q = query;
    }

}
