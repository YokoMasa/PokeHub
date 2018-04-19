package com.masalab.masato.githubfeed.view.fragment.commitoverview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.githubapi.Failure;
import com.masalab.masato.githubfeed.model.Commit;
import com.masalab.masato.githubfeed.model.Profile;
import com.masalab.masato.githubfeed.model.diff.DiffFile;
import com.masalab.masato.githubfeed.navigator.Navigator;
import com.masalab.masato.githubfeed.view.fragment.BaseFragment;

import java.util.List;

/**
 * Created by Masato on 2018/02/19.
 */

public class CommitOverviewFragment extends BaseFragment implements CommitOverviewContract.View, CommitOverviewAdapter.OnProfileClickListener {

    private CommitOverviewAdapter adapter;
    private CommitOverviewContract.Presenter presenter;

    @Override
    public boolean isZombie() {
        return presenter == null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Commit commit = getArguments().getParcelable("commit");
        if (commit == null) {
            throw new RuntimeException("no valid argument");
        }

        this.adapter = new CommitOverviewAdapter(commit, getContext());
        adapter.setListener(this);
        presenter = new CommitOverviewPresenter(commit);
        presenter.setView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.general_recycler_view_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.general_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        presenter.loadDiffFiles();
    }

    @Override
    public void onProfileClicked(Profile profile) {
        Navigator.navigateToProfileView(getContext(), profile.url);
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
    public void onTryAgain() {
        presenter.tryAgain();
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
    public void showDiffFiles(List<DiffFile> diffFiles) {
        adapter.setDiffFiles(diffFiles);
    }
}
