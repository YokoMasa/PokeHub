package com.masalab.masato.githubfeed.view.activity.content;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.githubapi.Failure;
import com.masalab.masato.githubfeed.githubapi.GitHubUrls;
import com.masalab.masato.githubfeed.model.ContentNode;
import com.masalab.masato.githubfeed.view.ContentView;
import com.masalab.masato.githubfeed.view.activity.BaseActivity;
import com.pddstudio.highlightjs.HighlightJsView;
import com.pddstudio.highlightjs.models.Language;
import com.pddstudio.highlightjs.models.Theme;

/**
 * Created by Masato on 2018/02/25.
 */

public class ContentActivity extends BaseActivity implements ContentView {

    private ContentPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        ContentNode contentNode = getIntent().getParcelableExtra("content_node");
        presenter = new ContentPresenter(this, contentNode);
        Toolbar toolbar = findViewById(R.id.ccontent_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(contentNode.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void showBitmapContent(Bitmap bitmap) {
        ImageView imageView = (ImageView) findViewById(R.id.content_image_view);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageBitmap(bitmap);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.content_mother);
        linearLayout.setBackgroundColor(getResources().getColor(R.color.black));
    }

    @Override
    public void showHtmlContent(String html) {
        WebView webView = (WebView) findViewById(R.id.content_web_view);
        webView.setVisibility(View.VISIBLE);
        webView.loadDataWithBaseURL(GitHubUrls.BASE_HTML_URL, html, "text/html", "utf-8", null);
    }

    @Override
    public void showCodeContent(String code) {
        HighlightJsView highlightJsView = (HighlightJsView) findViewById(R.id.content_code);
        highlightJsView.setVisibility(View.VISIBLE);
        highlightJsView.setTheme(Theme.GITHUB);
        highlightJsView.setHighlightLanguage(Language.AUTO_DETECT);
        highlightJsView.setShowLineNumbers(true);
        highlightJsView.setZoomSupportEnabled(true);
        highlightJsView.setSource(code);
    }

    @Override
    public void showErrorView(Failure failure, String message) {
        showErrorFragment(R.id.content_mother, failure, message);
    }

    @Override
    public void hideErrorView() {
        removeErrorFragment();
    }

    @Override
    public void showLoadingView() {
        showLoadingFragment(R.id.content_mother);
    }

    @Override
    public void hideLoadingView() {
        removeLoadingFragment();
    }

    @Override
    public void onTryAgain() {

    }

}
