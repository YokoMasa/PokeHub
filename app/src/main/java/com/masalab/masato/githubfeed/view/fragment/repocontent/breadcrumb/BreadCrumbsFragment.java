package com.masalab.masato.githubfeed.view.fragment.repocontent.breadcrumb;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.masalab.masato.githubfeed.R;

/**
 * Created by Masato on 2018/02/26.
 */

public class BreadCrumbsFragment extends Fragment implements BreadCrumbsView, BreadCrumbsAdapter.BreadCrumbAdapterListener {

    private BreadCrumbsAdapter adapter;

    @Override
    public void addBreadCrumb(BreadCrumb breadCrumb) {
        if (adapter != null) {
            adapter.addBreadCrumb(breadCrumb);
        }
    }

    @Override
    public void goBack() {
        if (adapter != null) {
            adapter.goBack();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bread_crumbs, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.bread_crumbs_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (adapter == null) {
            adapter = new BreadCrumbsAdapter(getContext(), "/");
            adapter.setListener(this);
        }
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBreadCrumbClicked(BreadCrumb breadCrumb) {
        if (getParentFragment() instanceof OnBreadCrumbClickListener) {
            OnBreadCrumbClickListener listener = (OnBreadCrumbClickListener) getParentFragment();
            listener.onBreadCrumbClicked(breadCrumb);
        }
    }

    public interface OnBreadCrumbClickListener {
        public void onBreadCrumbClicked(BreadCrumb breadCrumb);
    }

}
