package com.masalab.masato.githubfeed.view.fragment.difffilelist;

import com.masalab.masato.githubfeed.githubapi.GitHubApi;
import com.masalab.masato.githubfeed.githubapi.GitHubApiResult;
import com.masalab.masato.githubfeed.model.diff.DiffFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Masato on 2018/02/08.
 */

public class DiffFileListPresenter implements DiffFileListContract.Presenter {

    private DiffFileListContract.View view;
    private String url;
    private List<DiffFile> diffFiles;

    @Override
    public void setView(DiffFileListContract.View view) {
        this.view = view;
    }

    @Override
    public void loadDiffFiles() {
        if (diffFiles != null) {
            view.showDiffFiles(diffFiles);
        } else {
            view.showLoadingView();
            GitHubApi.getApi().fetchDiffFileList(url, this::handleResult);
        }
    }

    @Override
    public void tryAgain() {
        view.hideErrorView();
        view.showLoadingView();
        GitHubApi.getApi().fetchDiffFileList(url, this::handleResult);
    }

    private void handleResult(GitHubApiResult result) {
        view.hideLoadingView();
        if (result.isSuccessful) {
            ArrayList<DiffFile> diffFiles = (ArrayList<DiffFile>) result.resultObject;
            view.showDiffFiles(diffFiles);
        } else {
            view.showErrorView(result.failure, result.errorMessage);
        }
    }

    public DiffFileListPresenter(String url) {
        this.url = url;
    }

    public DiffFileListPresenter(List<DiffFile> diffFiles) {
        this.diffFiles = diffFiles;
    }

}
