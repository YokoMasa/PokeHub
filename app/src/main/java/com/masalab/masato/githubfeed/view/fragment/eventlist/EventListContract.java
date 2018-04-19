package com.masalab.masato.githubfeed.view.fragment.eventlist;

import com.masalab.masato.githubfeed.model.event.Event;
import com.masalab.masato.githubfeed.view.fragment.paginatinglist.PaginatingListContract;

/**
 * Created by Masato on 2018/03/08.
 */

public interface EventListContract {

    public interface View extends PaginatingListContract.View {
        public void startEventAction(Event event);
    }

    public interface Presenter extends PaginatingListContract.Presenter {
        public void setView(View view);
    }

}
