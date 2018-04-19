package com.masalab.masato.githubfeed.view.fragment.commitlist;

import com.masalab.masato.githubfeed.githubapi.GitHubUrls;
import com.masalab.masato.githubfeed.view.fragment.paginatinglist.PaginatingListPresenter;
import com.masalab.masato.githubfeed.githubapi.GitHubApi;
import com.masalab.masato.githubfeed.githubapi.GitHubApiResult;
import com.masalab.masato.githubfeed.model.BaseModel;
import com.masalab.masato.githubfeed.model.Commit;
import com.masalab.masato.githubfeed.model.Repository;

import java.util.List;

/**
 * Created by Masato on 2018/02/06.
 */

public class CommitListPresenter extends PaginatingListPresenter implements CommitListContract.Presenter {

    private String url;
    private Repository repository;
    private CommitListContract.View view;

    @Override
    public void setView(CommitListContract.View view) {
        this.view = view;
    }

    @Override
    public int onGetPaginatingItemViewType(BaseModel element) {
        return 0;
    }

    @Override
    public void onElementClicked(BaseModel element, int viewType) {
        Commit commit = (Commit) element;
        commit.repository = repository;
        view.showCommit(commit);
    }

    @Override
    protected void onFetchElement(int page) {
        GitHubApi.getApi().fetchCommitList(url, page, this::handleResult);
    }

    private void handleResult(GitHubApiResult result) {
        if (result.isSuccessful) {
            List<BaseModel> commits = (List<BaseModel>) result.resultObject;
            onFetchSucceeded(commits);
        } else {
            onFetchFailed(result.failure, result.errorMessage);
        }
    }

    public CommitListPresenter(Repository repository) {
        this(GitHubUrls.getCommitListUrl(repository));
        this.repository = repository;
    }

    public CommitListPresenter(String url) {
        this.url = url;
        setFetchThreshold(25);
    }

}
