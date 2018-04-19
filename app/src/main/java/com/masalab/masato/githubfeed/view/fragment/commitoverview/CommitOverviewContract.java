package com.masalab.masato.githubfeed.view.fragment.commitoverview;

import com.masalab.masato.githubfeed.model.diff.DiffFile;
import com.masalab.masato.githubfeed.view.BaseView;

import java.util.List;

/**
 * Created by Masato on 2018/03/08.
 */

public interface CommitOverviewContract {

    public interface View extends BaseView {
        public void showDiffFiles(List<DiffFile> diffFiles);
    }

    public interface Presenter {
        public void setView(View view);

        public void loadDiffFiles();

        public void tryAgain();
    }

}
