package com.masalab.masato.githubfeed.view.fragment.repolist.search;

import com.masalab.masato.githubfeed.view.fragment.repolist.RepoListContract;

/**
 * Created by Masato on 2018/03/10.
 */

public interface RepoSearchContract {

    public interface View extends RepoListContract.View {
        public void showSearchResult(String q);
    }

    public interface Presenter extends RepoListContract.Presenter {
        public void onSearchQuery(String q);
    }

}
