package com.masalab.masato.githubfeed.view.fragment.paginatinglist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.masalab.masato.githubfeed.R;

import static com.masalab.masato.githubfeed.view.fragment.paginatinglist.PaginatingListContract.View.*;

/**
 *
 * Created by Masato on 2018/01/29.
 *
 * このクラスはツイッターのように、あるスクロール地点まで行くと次のページのものを取ってくるという機能を持った
 * RecyclerView用のアダプターです。このアダプターはリストを持っておらず、viewに関することだけを処理します。
 * リストに関する処理はlistenerを通じてPaginatingListPresenterが行います。
 *
 */

public class PaginatingListAdapter extends RecyclerView.Adapter {

    private LayoutInflater inflater;
    private PaginatingListListAdapterListener listener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == LOADING_VIEW){
            viewHolder = listener.onCreateLoadingViewHolder(parent);
            if (viewHolder == null) {
                view = inflater.inflate(R.layout.feed_loading, parent, false);
                viewHolder = new LoadingViewHolder(view);
            }
        } else if (viewType == NOTHING_TO_SHOW_VIEW) {
            viewHolder = listener.onCreateNothingToShowViewHolder(parent);
            if (viewHolder == null) {
                view = inflater.inflate(R.layout.feed_nothing_to_show, parent, false);
                viewHolder = new NothingToShowViewHolder(view);
            }
        } else {
            viewHolder = listener.onCreateViewHolder(parent, viewType);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        listener.onFetchIfNeeded(position);
        if (getItemViewType(position) != NOTHING_TO_SHOW_VIEW
                && getItemViewType(position) != LOADING_VIEW) {
            listener.onBindViewHolder(holder, position);
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        listener.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return listener.onGetItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return listener.onGetItemViewType(position);
    }

    public PaginatingListAdapter(LayoutInflater inflater, PaginatingListListAdapterListener listener) {
        this.inflater = inflater;
        this.listener = listener;
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class NothingToShowViewHolder extends RecyclerView.ViewHolder {
        NothingToShowViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface PaginatingListListAdapterListener {
        public int onGetItemViewType(int position);

        public RecyclerView.ViewHolder onCreateLoadingViewHolder(ViewGroup parent);

        public RecyclerView.ViewHolder onCreateNothingToShowViewHolder(View parent);

        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

        public int onGetItemCount();

        public void onFetchIfNeeded(int position);

        public void onViewRecycled(RecyclerView.ViewHolder holder);
    }
}
