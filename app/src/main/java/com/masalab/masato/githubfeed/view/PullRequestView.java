package com.masalab.masato.githubfeed.view;

import com.masalab.masato.githubfeed.model.PullRequest;
import com.masalab.masato.githubfeed.model.Repository;

/**
 * Created by Masato on 2018/02/14.
 */

public interface PullRequestView extends BaseView {
    public void showPullRequest(PullRequest pr);

    public void showRepoInfo(Repository repository);
}
