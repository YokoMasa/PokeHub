package com.masalab.masato.githubfeed.view.fragment.paginatinglist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.githubapi.Failure;
import com.masalab.masato.githubfeed.model.BaseModel;
import com.masalab.masato.githubfeed.view.fragment.BaseFragment;

/**
 *
 * Created by Masato on 2018/01/29.
 *
 * このフラグメントはツイッターのように、あるスクロール地点まで行くと次のページのものを取ってくるという機能をもつ
 * フラグメントです。画面を下に引っ張って更新する機能も備えます。
 * このクラスのサブクラスはビューホルダーに関する処理を実装する必要があります。
 * また、ビジネスロジックの処理はonCreatePresenter()で渡されるPaginatingListPresenterによって行われます。
 *
 */

public abstract class PaginatingListFragment extends BaseFragment
        implements PaginatingListContract.View, PaginatingListAdapter.PaginatingListListAdapterListener {

    private PaginatingListContract.Presenter presenter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PaginatingListAdapter adapter;

    @Override
    public boolean isZombie() {
        return presenter == null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        presenter = onCreatePresenter();
        if (presenter == null) {
            throw new RuntimeException("presenter is null");
        }
        presenter.setView(this);
        presenter.refresh();
    }

    protected abstract PaginatingListContract.Presenter onCreatePresenter();

    @Override
    public void showLoadingView() {
        showLoadingFragment(R.id.feed_mother);
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
    public void showErrorView(Failure failure, String errorMessage) {
        showErrorFragment(R.id.feed_mother, failure, errorMessage);
    }

    @Override
    public void hideErrorView() {
        removeErrorFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    @Override
    public int onGetItemViewType(int position) {
        return presenter.getItemViewType(position);
    }

    @Override
    final public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreatePaginatingViewHolder(parent, viewType);
    }

    @Override
    public RecyclerView.ViewHolder onCreateLoadingViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateNothingToShowViewHolder(View parent) {
        return null;
    }

    protected abstract PaginatingListViewHolder onCreatePaginatingViewHolder(ViewGroup parent, int viewType);

    @Override
    final public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseModel element = presenter.getItem(position);
        int viewType = presenter.getItemViewType(position);
        PaginatingListViewHolder viewHolder = (PaginatingListViewHolder) holder;
        onBindViewHolder(viewHolder, element, viewType);
        viewHolder.notifyWhenClicked(element, new OnElementClickListener() {
            @Override
            public void onClick(BaseModel element) {
                presenter.onElementClicked(element, viewType);
            }
        });
    }

    protected abstract void onBindViewHolder(PaginatingListViewHolder holder, BaseModel element, int viewType);

    @Override
    final public void onFetchIfNeeded(int position) {
        presenter.fetchElementIfNeeded(position);
    }

    @Override
    public int onGetItemCount() {
        return presenter.getItemCount();
    }

    @Override
    public void updateAdapter() {
        doSafeViewTask(() -> {
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void stopRefreshing() {
        doSafeViewTask(() -> {
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    public void setSwipeRefreshEnabled(boolean enabled) {
        doSafeViewTask(() -> {
            swipeRefreshLayout.setEnabled(enabled);
        });
    }

    private void initViews(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.feed_swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refresh();
            }
        });

        adapter = new PaginatingListAdapter(getLayoutInflater(), this);
        RecyclerView elementRecyclerView = (RecyclerView) view.findViewById(R.id.feed_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        elementRecyclerView.setLayoutManager(layoutManager);
        elementRecyclerView.setAdapter(adapter);
    }

    public abstract class PaginatingListViewHolder extends RecyclerView.ViewHolder {

        View itemView;

        void notifyWhenClicked(final BaseModel element, final OnElementClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(element);
                }
            });
        }

        public PaginatingListViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

    private interface OnElementClickListener {
        void onClick(BaseModel element);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
