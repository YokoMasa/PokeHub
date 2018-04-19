package com.masalab.masato.githubfeed.view;

import com.masalab.masato.githubfeed.model.Issue;
import com.masalab.masato.githubfeed.model.Repository;

/**
 * Created by Masato on 2018/02/14.
 */

public interface IssueView extends BaseView {
    public void showIssue(Issue issue);

    public void showRepoInfo(Repository repository);
}
