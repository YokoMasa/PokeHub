package com.masalab.masato.githubfeed.view.fragment.issuelist;

import com.masalab.masato.githubfeed.githubapi.GitHubUrls;
import com.masalab.masato.githubfeed.view.fragment.paginatinglist.PaginatingListPresenter;
import com.masalab.masato.githubfeed.githubapi.GitHubApi;
import com.masalab.masato.githubfeed.githubapi.GitHubApiResult;
import com.masalab.masato.githubfeed.model.BaseModel;
import com.masalab.masato.githubfeed.model.Issue;
import com.masalab.masato.githubfeed.model.Repository;

import java.util.ArrayList;

/**
 * Created by Masato on 2018/02/03.
 */

public class IssueListPresenter extends PaginatingListPresenter implements IssueListContract.Presenter {

    private String url;
    private IssueListContract.View view;

    @Override
    public void setView(IssueListContract.View view) {
        this.view = view;
    }

    @Override
    public int onGetPaginatingItemViewType(BaseModel element) {
        return 0;
    }

    @Override
    public void onElementClicked(BaseModel element, int viewType) {
        Issue issue = (Issue) element;
        view.showIssue(issue);
    }

    @Override
    protected void onFetchElement(int page) {
        GitHubApi.getApi().fetchIssueList(url, page, this::handleApiResult);
    }

    private void handleApiResult(GitHubApiResult result) {
        if (result.isSuccessful) {
            ArrayList<BaseModel> issues = (ArrayList<BaseModel>) result.resultObject;
            onFetchSucceeded(issues);
        } else {
            onFetchFailed(result.failure, result.errorMessage);
        }
    }

    public IssueListPresenter(Repository repository) {
        this(GitHubUrls.getIssueListUrl(repository));
    }

    public IssueListPresenter(String url) {
        this.url = url;
    }

}
