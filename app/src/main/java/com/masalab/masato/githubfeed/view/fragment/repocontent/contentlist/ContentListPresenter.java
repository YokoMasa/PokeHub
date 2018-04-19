package com.masalab.masato.githubfeed.view.fragment.repocontent.contentlist;

import com.masalab.masato.githubfeed.githubapi.GitHubApi;
import com.masalab.masato.githubfeed.githubapi.GitHubApiResult;
import com.masalab.masato.githubfeed.githubapi.GitHubUrls;
import com.masalab.masato.githubfeed.model.ContentNode;
import com.masalab.masato.githubfeed.model.Repository;

import java.util.List;

/**
 * Created by Masato on 2018/02/25.
 */

public class ContentListPresenter implements ContentListContract.Presenter {

    private ContentListContract.View view;
    private String rootUrl;
    private ContentNode rootNode;
    private ContentNode currentNode;

    @Override
    public void setView(ContentListContract.View view) {
        this.view = view;
    }

    @Override
    public void tryAgain() {
        view.showLoadingView();
        GitHubApi.getApi().fetchContentList(rootUrl, this::handleRootNodeListResult);
    }

    @Override
    public void loadData() {
        view.showLoadingView();
        GitHubApi.getApi().fetchContentList(rootUrl, this::handleRootNodeListResult);
    }

    @Override
    public boolean onBackPressed() {
        if (currentNode == null) {
            return false;
        }
        if (currentNode.parent == null) {
            return false;
        }
        currentNode = currentNode.parent;
        view.listContents(currentNode);
        return true;
    }

    @Override
    public void onContentClicked(ContentNode contentNode) {
        switch (contentNode.type) {
            case "dir":
                currentNode = contentNode;
                openDir(contentNode);
                break;
            case "file":
                view.showContent(contentNode);
                break;
        }
    }

    private void openDir(ContentNode contentNode) {
        if (currentNode.getChildNodes() == null) {
            view.showLoadingView();
            GitHubApi.getApi().fetchContentList(currentNode.url, this::handleNodeListResult);
        } else {
            view.listContents(currentNode);
            view.notifyNextContent(currentNode);
        }
    }

    private void handleNodeListResult(GitHubApiResult result) {
        view.hideLoadingView();
        if (result.isSuccessful) {
            List<ContentNode> contentNodes = (List<ContentNode>) result.resultObject;
            currentNode.setChildNodes(contentNodes);
            view.listContents(currentNode);
            view.notifyNextContent(currentNode);
        } else {
            view.showErrorView(result.failure, result.errorMessage);
        }
    }

    private void handleRootNodeListResult(GitHubApiResult result) {
        view.hideLoadingView();
        rootNode = new ContentNode();
        if (result.isSuccessful) {
            List<ContentNode> contentNodes = (List<ContentNode>) result.resultObject;
            rootNode.setChildNodes(contentNodes);
            rootNode.name = "root";
            rootNode.type = "dir";
            currentNode = rootNode;
            view.listContents(rootNode);
            view.notifyNextContent(currentNode);
        } else {
            view.showErrorView(result.failure, result.errorMessage);
        }
    }

    public ContentListPresenter(Repository repository) {
        this.rootUrl = GitHubUrls.getRepoContentUrl(repository);
    }

}
