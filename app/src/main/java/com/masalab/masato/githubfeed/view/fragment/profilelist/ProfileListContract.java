package com.masalab.masato.githubfeed.view.fragment.profilelist;

import com.masalab.masato.githubfeed.model.Profile;
import com.masalab.masato.githubfeed.view.fragment.paginatinglist.PaginatingListContract;

/**
 * Created by Masato on 2018/03/10.
 */

public interface ProfileListContract {

    public interface View extends PaginatingListContract.View {
        public void showProfile(Profile profile);
    }

    public interface Presenter extends PaginatingListContract.Presenter {
        public void setView(View view);
    }

}
