package com.masalab.masato.githubfeed.view.fragment.difffilelist;

import com.masalab.masato.githubfeed.model.diff.DiffFile;
import com.masalab.masato.githubfeed.view.BaseView;

import java.util.List;

/**
 * Created by Masato on 2018/03/08.
 */

public interface DiffFileListContract {

    public interface View extends BaseView {
        public void showDiffFiles(List<DiffFile> diffFiles);
    }

    public interface Presenter {
        public void tryAgain();

        public void setView(View view);

        public void loadDiffFiles();
    }

}
