package com.masalab.masato.githubfeed.view.fragment.difffilelist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.githubapi.Failure;
import com.masalab.masato.githubfeed.model.diff.DiffFile;
import com.masalab.masato.githubfeed.view.fragment.BaseFragment;

import java.util.List;

/**
 * Created by Masato on 2018/02/06.
 */

public class DiffFileListFragment extends BaseFragment implements DiffFileListContract.View {

    private DiffFileListAdapter adapter;
    private DiffFileListContract.Presenter presenter;

    @Override
    public boolean isZombie() {
        return presenter == null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        String url = getArguments().getString("url");
        List<DiffFile> diffFiles = getArguments().getParcelableArrayList("diff_files");
        if (diffFiles != null) {
            presenter = new DiffFileListPresenter(diffFiles);
        } else if (url != null) {
            presenter = new DiffFileListPresenter(url);
        } else {
            throw new RuntimeException("no valid argument");
        }
        presenter.setView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_diff_file_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        presenter.loadDiffFiles();
    }

    private void initViews(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.diff_file_list_recyclerView);
        adapter = new DiffFileListAdapter(getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void showLoadingView() {
        showLoadingFragment(R.id.diff_file_list_mother);
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
        showErrorFragment(R.id.diff_file_list_mother, failure, message);
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
