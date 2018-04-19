package com.masalab.masato.githubfeed.view.fragment.commentlist;

import com.masalab.masato.githubfeed.githubapi.GitHubApi;
import com.masalab.masato.githubfeed.githubapi.GitHubApiResult;
import com.masalab.masato.githubfeed.githubapi.GitHubUrls;
import com.masalab.masato.githubfeed.model.BaseModel;
import com.masalab.masato.githubfeed.model.Comment;
import com.masalab.masato.githubfeed.model.Issue;
import com.masalab.masato.githubfeed.model.PullRequest;
import com.masalab.masato.githubfeed.model.event.Event;
import com.masalab.masato.githubfeed.view.fragment.paginatinglist.PaginatingListPresenter;

import java.util.Collections;
import java.util.List;

/**
 * Created by Masato on 2018/02/27.
 */

public class CommentListPresenter extends PaginatingListPresenter implements CommentListContract.Presenter {

    private String commentsUrl;
    private String eventsUrl;
    private CommentListContract.View view;
    private List<BaseModel> commentsBuffer;

    @Override
    public void setView(CommentListContract.View view) {
        this.view = view;
    }

    @Override
    public int onGetPaginatingItemViewType(BaseModel element) {
        if (element instanceof Comment) {
            return CommentListContract.View.COMMENT_VIEW;
        } else if (element instanceof Event) {
            return CommentListContract.View.EVENT_VIEW;
        } else {
            return 0;
        }
    }

    @Override
    public void onElementClicked(BaseModel element, int viewType) {
        if (viewType == CommentListContract.View.COMMENT_VIEW) {
            Comment comment = (Comment) element;
            if (comment.author != null) {
                view.showProfile(comment.author);
            }
        }
    }

    @Override
    protected void onFetchElement(int page) {
        GitHubApi.getApi().fetchCommentList(commentsUrl, page, result -> {
            if (result.isSuccessful) {
                commentsBuffer = (List<BaseModel>) result.resultObject;
                if (eventsUrl != null) {
                    GitHubApi.getApi().fetchIssueEventList(eventsUrl, page, this::handleEventResult);
                } else {
                    onFetchSucceeded(commentsBuffer);
                }
            } else {
                onFetchFailed(result.failure, result.errorMessage);
            }
        });
    }

    private void handleEventResult(GitHubApiResult result) {
        if (result.isSuccessful) {
            List<BaseModel> events = (List<BaseModel>) result.resultObject;
            commentsBuffer.addAll(events);
            Collections.sort(commentsBuffer);
            onFetchSucceeded(commentsBuffer);
        } else {
            onFetchSucceeded(commentsBuffer);
            view.showToast("failed to load events.");
        }
    }

    public CommentListPresenter(Issue issue) {
        this.commentsUrl = issue.commentsUrl;
        this.eventsUrl = GitHubUrls.getIssueEventUrl(issue);
    }

    public CommentListPresenter(PullRequest pr) {
        this.commentsUrl = pr.commentsUrl;
        this.eventsUrl = GitHubUrls.getPREventUrl(pr);
    }

}
