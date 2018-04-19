package com.masalab.masato.githubfeed.view.fragment.repooverview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.githubapi.Failure;
import com.masalab.masato.githubfeed.githubapi.GitHubUrls;
import com.masalab.masato.githubfeed.model.Repository;
import com.masalab.masato.githubfeed.navigator.Navigator;
import com.masalab.masato.githubfeed.view.fragment.BaseFragment;

/**
 * Created by Masato on 2018/02/02.
 */

public class RepoOverviewFragment extends BaseFragment implements RepoOverviewContract.View, View.OnClickListener {

    private RepoOverviewContract.Presenter presenter;
    private ScrollView scrollView;
    private AppCompatTextView starCount;
    private AppCompatTextView watchCount;
    private AppCompatTextView forkCount;
    private AppCompatTextView forkedFrom;
    private ImageView star;
    private ImageView watch;
    private WebView readmeWebView;
    private int savedScrollX, savedScrollY;

    @Override
    public boolean isZombie() {
        return presenter == null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Repository repository = getArguments().getParcelable("repo");
        if (repository != null) {
            presenter = new RepoOverviewPresenter(repository);
        } else {
            throw new RuntimeException("no valid argument");
        }
        presenter.setView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repo_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.loadData();
        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        }
        initViews(view);
    }

    private void initViews(View view) {
        LinearLayout starLayout = view.findViewById(R.id.repo_star_layout);
        LinearLayout watchLayout = view.findViewById(R.id.repo_watch_layout);
        starLayout.setOnClickListener(this);
        watchLayout.setOnClickListener(this);
        star = view.findViewById(R.id.repo_star_image);
        watch = view.findViewById(R.id.repo_watch_image);
        starCount = view.findViewById(R.id.repo_star);
        watchCount = view.findViewById(R.id.repo_watch);
        forkCount = view.findViewById(R.id.repo_fork);
        forkedFrom = view.findViewById(R.id.repo_forked_from);
        readmeWebView = view.findViewById(R.id.repo_readme_web_view);
        readmeWebView.loadData(getString(R.string.repo_loading_readme), "text/html", "utf-8");
        scrollView = view.findViewById(R.id.repo_scroll_view);
    }

    private void restoreState(Bundle savedInstanceState) {
        savedScrollX = savedInstanceState.getInt("scroll_x");
        savedScrollY = savedInstanceState.getInt("scroll_y");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.repo_star_layout) {
            presenter.starPressed();
        } else {
            presenter.subscribePressed();
        }
    }

    @Override
    public void showLoadingView() {
        showLoadingFragment(R.id.repo_mother);
    }

    @Override
    public void hideLoadingView() {
        removeLoadingFragment();
    }

    @Override
    public void onTryAgain() {
        presenter.tryAgain();
    }

    @Override
    public void showErrorView(Failure failure, String message) {
        showErrorFragment(R.id.repo_mother, failure, message);
    }

    @Override
    public void hideErrorView() {
        removeErrorFragment();
    }

    @Override
    public void showOverview(Repository repository) {
        doSafeViewTask(() -> {
            starCount.setText(Integer.toString(repository.stars));
            watchCount.setText(Integer.toString(repository.watches));
            forkCount.setText(Integer.toString(repository.forks));
        });
    }

    @Override
    public void showForkedFrom(Repository repository) {
        doSafeViewTask(() -> {
            forkedFrom.setVisibility(View.VISIBLE);
            forkedFrom.setText(getString(R.string.repo_forked_from) + " " + repository.fullName);
            forkedFrom.setOnClickListener(view -> {
                presenter.forkedFromPressed();
            });
        });
    }

    @Override
    public void showParent(Repository repository) {
        Navigator.navigateToRepoView(getContext(), repository.baseUrl);
    }

    @Override
    public void showReadMe(String readMeHtml) {
        doSafeViewTask(() -> {
            readmeWebView.loadDataWithBaseURL(GitHubUrls.BASE_HTML_URL, readMeHtml, "text/html", "utf-8", null);
            readmeWebView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    if (newProgress == 100) {
                        scrollView.setScrollX(savedScrollX);
                        scrollView.setScrollY(savedScrollY);
                    }
                }
            });
        });
    }

    @Override
    public void showNoReadMe() {
        doSafeViewTask(() -> {
            readmeWebView.loadData(getString(R.string.repo_no_readme), "text/html", "utf-8");
        });
    }

    @Override
    public void setStarActivated(boolean activated) {
        doSafeViewTask(() -> {
            if (activated) {
                star.setImageResource(R.drawable.star_active);
            } else {
                star.setImageResource(R.drawable.star);
            }
        });
    }

    @Override
    public void setWatchActivated(boolean activated) {
        doSafeViewTask(() -> {
            if (activated) {
                watch.setImageResource(R.drawable.watch_active);
            } else {
                watch.setImageResource(R.drawable.watch);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("scroll_x", scrollView.getScrollX());
        outState.putInt("scroll_y", scrollView.getScrollY());
    }
}
