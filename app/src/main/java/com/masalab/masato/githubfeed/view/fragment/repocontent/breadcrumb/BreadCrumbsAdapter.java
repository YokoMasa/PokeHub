package com.masalab.masato.githubfeed.view.fragment.repocontent.breadcrumb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.masalab.masato.githubfeed.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Masato on 2018/02/26.
 */

public class BreadCrumbsAdapter extends RecyclerView.Adapter implements BreadCrumbViewHolder.BreadCrumbClickListener {

    private List<BreadCrumb> breadCrumbs = new ArrayList<>();
    private BreadCrumbAdapterListener listener;
    private LayoutInflater inflater;
    private String divider;

    public void setListener(BreadCrumbAdapterListener listener) {
        this.listener = listener;
    }

    public void addBreadCrumb(BreadCrumb breadCrumb) {
        addDivider();
        breadCrumbs.add(breadCrumb);
        notifyDataSetChanged();
    }

    public void goBack() {
        Log.i("gh_feed", "go back");
        if (2 <= getItemCount()) {
            Log.i("gh_feed", "go back successful");
            breadCrumbs = breadCrumbs.subList(0, getItemCount() - 2);
        }
        notifyDataSetChanged();
    }

    private void addDivider() {
        BreadCrumb dividerCrumb = new BreadCrumb();
        dividerCrumb.isDivider = true;
        dividerCrumb.name = divider;
        breadCrumbs.add(dividerCrumb);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.bread_crumb, parent, false);
        return new BreadCrumbViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BreadCrumb breadCrumb = breadCrumbs.get(position);
        BreadCrumbViewHolder breadCrumbViewHolder = (BreadCrumbViewHolder) holder;
        breadCrumbViewHolder.bindBreadCrumb(breadCrumb, position);
        breadCrumbViewHolder.setListener(this);
    }

    @Override
    public int getItemCount() {
        return breadCrumbs.size();
    }

    @Override
    public void onClick(BreadCrumb breadCrumb, int index) {
        if (breadCrumb.isDivider) {
            return;
        }
        if (listener != null) {
            listener.onBreadCrumbClicked(breadCrumb);
        }
        if (index < getItemCount()) {
            this.breadCrumbs = breadCrumbs.subList(0, index + 1);
        }
    }

    public BreadCrumbsAdapter(Context context, String divider) {
        this.inflater = LayoutInflater.from(context);
        this.divider = divider;
    }

    public interface BreadCrumbAdapterListener {
        public void onBreadCrumbClicked(BreadCrumb breadCrumb);
    }

}
