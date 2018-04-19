package com.masalab.masato.githubfeed.view.fragment.repolist.trending;

import com.masalab.masato.githubfeed.view.fragment.repolist.RepoListContract;
import com.masalab.masato.githubfeed.view.fragment.repolist.RepoListFragment;

/**
 * Created by Masato on 2018/03/10.
 */

public class TrendingRepoFragment extends RepoListFragment {

    @Override
    protected RepoListContract.Presenter onCreateRepoListPresenter() {
        return new TrendingRepoPresenter();
    }

}
