package com.masalab.masato.githubfeed.view;

import com.masalab.masato.githubfeed.model.Repository;

/**
 * Created by Masato on 2018/02/07.
 */

public interface CommitView extends BaseView {
    public void showRepoInfo(Repository repository);
}
