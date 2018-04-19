package com.masalab.masato.githubfeed.view.fragment.issuelist;

import com.masalab.masato.githubfeed.model.Issue;
import com.masalab.masato.githubfeed.view.fragment.paginatinglist.PaginatingListContract;

/**
 * Created by Masato on 2018/03/08.
 */

public interface IssueListContract {

    public interface View extends PaginatingListContract.View {
        public void showIssue(Issue issue);
    }

    public interface Presenter extends PaginatingListContract.Presenter {
        public void setView(View view);
    }

}
