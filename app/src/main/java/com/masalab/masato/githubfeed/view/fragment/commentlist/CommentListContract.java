package com.masalab.masato.githubfeed.view.fragment.commentlist;

import com.masalab.masato.githubfeed.model.Profile;
import com.masalab.masato.githubfeed.view.fragment.paginatinglist.PaginatingListContract;

/**
 * Created by Masato on 2018/03/08.
 */

public interface CommentListContract {

    public interface View extends PaginatingListContract.View {

        public static final int COMMENT_VIEW = 321;
        public static final int EVENT_VIEW = 123;

        public void showProfile(Profile profile);
    }

    public interface Presenter extends PaginatingListContract.Presenter {
        public void setView(View view);
    }
}
