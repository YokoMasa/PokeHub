package com.masalab.masato.githubfeed.view.fragment.repocontent.contentlist;

import com.masalab.masato.githubfeed.model.ContentNode;
import com.masalab.masato.githubfeed.view.BaseView;

/**
 * Created by Masato on 2018/03/08.
 */

public interface ContentListContract {

    public interface View extends BaseView {
        public void listContents(ContentNode contentNode);

        public void showContent(ContentNode contentNode);

        public void performClick(ContentNode contentNode);

        public void notifyNextContent(ContentNode contentNode);
    }

    public interface Presenter {
        public void setView(View view);

        public void loadData();

        public boolean onBackPressed();

        public void onContentClicked(ContentNode contentNode);

        public void tryAgain();
    }

}
