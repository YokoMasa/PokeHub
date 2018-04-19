package com.masalab.masato.githubfeed.view.fragment.eventlist;

import com.masalab.masato.githubfeed.githubapi.GitHubApi;
import com.masalab.masato.githubfeed.githubapi.GitHubApiResult;
import com.masalab.masato.githubfeed.model.BaseModel;
import com.masalab.masato.githubfeed.model.event.Event;
import com.masalab.masato.githubfeed.view.fragment.paginatinglist.PaginatingListPresenter;

import java.util.List;

/**
 * Created by Masato on 2018/02/11.
 */

public class EventListPresenter extends PaginatingListPresenter implements EventListContract.Presenter {

    private static final int MAX_PAGE = 10;

    private String url;
    private EventListContract.View view;

    @Override
    public void setView(EventListContract.View view) {
        this.view = view;
    }

    @Override
    public int onGetPaginatingItemViewType(BaseModel element) {
        return 0;
    }

    @Override
    public void onElementClicked(BaseModel element, int viewType) {
        Event event = (Event) element;
        view.startEventAction(event);
    }

    @Override
    protected void onFetchElement(int page) {
        GitHubApi.getApi().fetchEventList(url, page, this::handleResult);
    }

    private void handleResult(GitHubApiResult result) {
        if (result.isSuccessful) {
            List<BaseModel> events = (List<BaseModel>) result.resultObject;
            onFetchSucceeded(events);
        } else {
            onFetchFailed(result.failure, result.errorMessage);
        }
    }

    public EventListPresenter(String url) {
        this.url = url;
        setMaxPage(MAX_PAGE);
    }
}
