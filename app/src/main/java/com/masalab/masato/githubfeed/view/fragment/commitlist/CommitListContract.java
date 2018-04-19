package com.masalab.masato.githubfeed.view.fragment.commitlist;

import com.masalab.masato.githubfeed.model.Commit;
import com.masalab.masato.githubfeed.view.fragment.paginatinglist.PaginatingListContract;

/**
 * Created by Masato on 2018/03/08.
 */

public interface CommitListContract {

    public interface View extends PaginatingListContract.View {
        public void showCommit(Commit commit);
    }

    public interface Presenter extends PaginatingListContract.Presenter{
        public void setView(View view);
    }

}
