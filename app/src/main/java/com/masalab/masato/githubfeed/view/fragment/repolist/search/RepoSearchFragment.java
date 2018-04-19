package com.masalab.masato.githubfeed.view.fragment.repolist.search;

import com.masalab.masato.githubfeed.view.fragment.repolist.RepoListContract;
import com.masalab.masato.githubfeed.view.fragment.repolist.RepoListFragment;

/**
 * Created by Masato on 2018/03/10.
 */

public class RepoSearchFragment extends RepoListFragment implements RepoSearchContract.View {

    private RepoSearchContract.Presenter presenter;

    @Override
    public void showSearchResult(String q) {
        presenter.onSearchQuery(q);
    }

    @Override
    protected RepoListContract.Presenter onCreateRepoListPresenter() {
        String q = getArguments().getString("query");
        if (q == null || q.equals("")) {
            throw new RuntimeException("no query");
        }
        presenter = new RepoSearchPresenter(q);
        return presenter;
    }

    public RepoSearchFragment() {
        setSwipeRefreshEnabled(false);
    }

}
