package com.masalab.masato.githubfeed.view.fragment.commitoverview;

import com.masalab.masato.githubfeed.githubapi.GitHubApi;
import com.masalab.masato.githubfeed.githubapi.GitHubApiResult;
import com.masalab.masato.githubfeed.model.Commit;
import com.masalab.masato.githubfeed.model.diff.DiffFile;

import java.util.List;

/**
 * Created by Masato on 2018/02/19.
 */

public class CommitOverviewPresenter implements CommitOverviewContract.Presenter {

    private CommitOverviewContract.View view;
    private Commit commit;
    private List<DiffFile> diffFiles;

    @Override
    public void setView(CommitOverviewContract.View view) {
        this.view = view;
    }

    @Override
    public void loadDiffFiles() {
        if (diffFiles == null) {
            view.showLoadingView();
            GitHubApi.getApi().fetchCommitDiffFileList(commit, this::handleDiffFileListResult);
        } else {
            view.showDiffFiles(diffFiles);
        }
    }

    @Override
    public void tryAgain() {
        view.hideErrorView();
        view.showLoadingView();
        GitHubApi.getApi().fetchCommitDiffFileList(commit, this::handleDiffFileListResult);
    }

    private void handleDiffFileListResult(GitHubApiResult result) {
        view.hideLoadingView();
        if (result.isSuccessful) {
            List<DiffFile> diffFiles = (List<DiffFile>) result.resultObject;
            this.diffFiles = diffFiles;
            view.showDiffFiles(diffFiles);
        } else {
            view.showErrorView(result.failure, result.errorMessage);
        }
    }

    public CommitOverviewPresenter(Commit commit) {
        this.commit = commit;
    }

}
