package com.masalab.masato.githubfeed.view.fragment.repolist;

import com.masalab.masato.githubfeed.model.Repository;
import com.masalab.masato.githubfeed.view.fragment.paginatinglist.PaginatingListContract;

/**
 * Created by Masato on 2018/03/08.
 */

public interface RepoListContract {

    public interface View extends PaginatingListContract.View {
        public void showRepo(Repository repository);
    }

    public interface Presenter extends PaginatingListContract.Presenter {
        public void setView(View view);
    }

}
