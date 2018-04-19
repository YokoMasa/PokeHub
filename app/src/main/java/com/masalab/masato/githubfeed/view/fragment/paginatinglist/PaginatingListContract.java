package com.masalab.masato.githubfeed.view.fragment.paginatinglist;

import com.masalab.masato.githubfeed.model.BaseModel;
import com.masalab.masato.githubfeed.view.BaseView;

/**
 * Created by Masato on 2018/03/08.
 */

public interface PaginatingListContract {

    public interface View extends BaseView {
        public static final int LOADING_VIEW = 2498;
        public static final int NOTHING_TO_SHOW_VIEW = 3011;

        public void stopRefreshing();

        public void updateAdapter();
    }

    public interface Presenter {
        public BaseModel getItem(int position);

        public int getItemCount();

        public int getItemViewType(int position);

        public void tryAgain();

        public void refresh();

        public void fetchElementIfNeeded(int position);

        public void setView(View view);

        public void onElementClicked(BaseModel element, int viewType);
    }

}
