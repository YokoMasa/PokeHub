package com.masalab.masato.githubfeed.view.fragment.prlist;

import com.masalab.masato.githubfeed.model.PullRequest;
import com.masalab.masato.githubfeed.view.fragment.paginatinglist.PaginatingListContract;

/**
 * Created by Masato on 2018/03/08.
 */

public interface PullRequestListContract {

    public interface View extends PaginatingListContract.View {
        public void showPullRequest(PullRequest pr);
    }

    public interface Presenter extends PaginatingListContract.Presenter {
        public void setView(View view);
    }

}
