package com.masalab.masato.githubfeed.view.fragment.repocontent.contentlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.githubapi.Failure;
import com.masalab.masato.githubfeed.model.ContentNode;
import com.masalab.masato.githubfeed.model.Repository;
import com.masalab.masato.githubfeed.navigator.Navigator;
import com.masalab.masato.githubfeed.view.fragment.BaseFragment;

/**
 * Created by Masato on 2018/02/25.
 */

public class ContentListFragment extends BaseFragment implements ContentListAdapter.OnContentClickListener, ContentListContract.View {

    private ContentListAdapter contentListAdapter;
    private ContentListContract.Presenter presenter;
    private OnContentsListChangeListener listener;
    private ContentNode currentNode;

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
            presenter = new ContentListPresenter(repository);
        } else {
            throw new RuntimeException("no valid argument");
        }
        presenter.setView(this);
        presenter.loadData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getParentFragment() instanceof OnContentsListChangeListener) {
            this.listener = (OnContentsListChangeListener) getParentFragment();
        }
        contentListAdapter = new ContentListAdapter(getContext());
        contentListAdapter.setOnContentClickListener(this);
        if (currentNode != null) {
            contentListAdapter.setContentNode(currentNode);
        }
        return inflater.inflate(R.layout.general_recycler_view_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.general_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(contentListAdapter);
    }

    @Override
    public boolean onBackPressed() {
        if (isVisibleInPager()) {
            if (presenter.onBackPressed()) {
                if (this.listener != null) {
                    listener.previousContent();
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void listContents(ContentNode contentNode) {
        this.currentNode = contentNode;
        if (contentListAdapter != null) {
            contentListAdapter.setContentNode(contentNode);
        }
    }

    @Override
    public void notifyNextContent(ContentNode contentNode) {
        if (this.listener != null) {
            listener.nextContent(contentNode);
        }
    }

    @Override
    public void showContent(ContentNode contentNode) {
        Navigator.navigateToContentView(getContext(), contentNode);
    }

    @Override
    public void showErrorView(Failure failure, String message) {
        showErrorFragment(R.id.general_recyclerView_mother, failure, message);
    }

    @Override
    public void hideErrorView() {
        removeErrorFragment();
    }

    @Override
    public void showLoadingView() {
        showLoadingFragment(R.id.general_recyclerView_mother);
    }

    @Override
    public void hideLoadingView() {
        removeLoadingFragment();
    }

    @Override
    public void performClick(ContentNode contentNode) {
        onContentClicked(contentNode);
    }

    @Override
    public void onContentClicked(ContentNode contentNode) {
        presenter.onContentClicked(contentNode);
    }

    @Override
    public void onTryAgain() {
        presenter.tryAgain();
    }

    public interface OnContentsListChangeListener {
        public void nextContent(ContentNode contentNode);

        public void previousContent();
    }
}
