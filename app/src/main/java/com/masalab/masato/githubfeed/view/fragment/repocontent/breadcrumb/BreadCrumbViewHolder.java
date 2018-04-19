package com.masalab.masato.githubfeed.view.fragment.repocontent.breadcrumb;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.masalab.masato.githubfeed.R;

/**
 * Created by Masato on 2018/02/26.
 */

public class BreadCrumbViewHolder extends RecyclerView.ViewHolder {

    public AppCompatTextView text;
    private BreadCrumbClickListener listener;
    private BreadCrumb breadCrumb;
    private int index;

    public int getIndex() {
        return index;
    }

    BreadCrumbClickListener getListener() {
        return this.listener;
    }

    BreadCrumb getBreadCrumb() {
        return this.breadCrumb;
    }

    public void setListener(BreadCrumbClickListener listener) {
        this.listener = listener;
    }

    public void bindBreadCrumb(BreadCrumb breadCrumb, int index) {
        this.breadCrumb = breadCrumb;
        text.setText(breadCrumb.name);
        this.index = index;
    }

    public BreadCrumbViewHolder(View itemView) {
        super(itemView);
        this.text = (AppCompatTextView) itemView.findViewById(R.id.bread_crumb_text);
        itemView.setOnClickListener(view -> {
            if (getListener() != null && getBreadCrumb() != null) {
                getListener().onClick(getBreadCrumb(), getIndex());
            }
        });
    }

    public interface BreadCrumbClickListener {
        public void onClick(BreadCrumb breadCrumb, int index);
    }
}
