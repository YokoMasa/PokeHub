package com.masalab.masato.githubfeed.view.activity.content;

import android.graphics.Bitmap;

import com.masalab.masato.githubfeed.githubapi.GitHubApi;
import com.masalab.masato.githubfeed.githubapi.GitHubApiResult;
import com.masalab.masato.githubfeed.model.ContentNode;
import com.masalab.masato.githubfeed.presenter.BasePresenter;
import com.masalab.masato.githubfeed.view.ContentView;

/**
 * Created by Masato on 2018/02/25.
 */

public class ContentPresenter extends BasePresenter {

    private static final String[] IMAGE_EXT = new String[]{".png", ".jpg", ".jpeg", ".gif", ".bmp", ".PNG", ".JPG", ".JPEG", ".GIF", ".BMP"};
    private static final String[] MD_EXT = new String[]{".md", ".MD"};

    private ContentView view;
    private ContentNode contentNode;

    @Override
    public void tryAgain() {
        view.hideErrorView();
        fetchContent(contentNode);
    }

    private void fetchContent(ContentNode contentNode) {
        view.showLoadingView();
        if (isImage(contentNode.name)) {
            GitHubApi.getApi().fetchContentBitmap(contentNode, this::handleBitmapContentResult);
        } else if (isMD(contentNode.name)) {
            GitHubApi.getApi().fetchContentHtml(contentNode, this::handleMDContentResult);
        } else {
            GitHubApi.getApi().fetchContent(contentNode, this::handleCodeContentResult);
        }
    }

    private void handleBitmapContentResult(GitHubApiResult result) {
        view.hideLoadingView();
        if (result.isSuccessful) {
            Bitmap bitmap = (Bitmap) result.resultObject;
            view.showBitmapContent(bitmap);
        }
    }

    private void handleMDContentResult(GitHubApiResult result) {
        view.hideLoadingView();
        if (result.isSuccessful) {
            String content = (String) result.resultObject;
            view.showHtmlContent(content);
        }
    }

    private void handleCodeContentResult(GitHubApiResult result) {
        view.hideLoadingView();
        if (result.isSuccessful) {
            String content = (String) result.resultObject;
            view.showCodeContent(content);
        }
    }

    private boolean isImage(String fileName) {
        return doesStringEndsWith(IMAGE_EXT, fileName);
    }

    private boolean isMD(String fileName) {
        return doesStringEndsWith(MD_EXT, fileName);
    }

    private boolean doesStringEndsWith(String[] array, String string) {
        for (String s : array) {
            if (string.endsWith(s)) {
                return true;
            }
        }
        return false;
    }

    public ContentPresenter(ContentView view, ContentNode contentNode) {
        this.view = view;
        this.contentNode = contentNode;
        fetchContent(contentNode);
    }
}
