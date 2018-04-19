package com.masalab.masato.githubfeed.view.fragment.repooverview;

import com.masalab.masato.githubfeed.model.Repository;
import com.masalab.masato.githubfeed.view.BaseView;

/**
 * Created by Masato on 2018/03/08.
 */

public interface RepoOverviewContract {

    public interface View extends BaseView {
        public void showOverview(Repository repository);

        public void showReadMe(String readMeString);

        public void showNoReadMe();

        public void setStarActivated(boolean activated);

        public void setWatchActivated(boolean watched);

        public void showForkedFrom(Repository repository);

        public void showParent(Repository repository);
    }

    public interface Presenter {
        public void setView(View view);

        public void loadData();

        public void starPressed();

        public void subscribePressed();

        public void forkedFromPressed();

        public void tryAgain();
    }

}
