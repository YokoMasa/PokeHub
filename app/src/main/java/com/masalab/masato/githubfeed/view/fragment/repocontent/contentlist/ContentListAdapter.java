package com.masalab.masato.githubfeed.view.fragment.repocontent.contentlist;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.model.ContentNode;

/**
 * Created by Masato on 2018/02/25.
 */

public class ContentListAdapter extends RecyclerView.Adapter {

    private ContentNode contentNode;
    private LayoutInflater inflater;
    private OnContentClickListener listener;

    public void setOnContentClickListener(OnContentClickListener listener) {
        this.listener = listener;
    }

    public ContentNode getContentNode() {
        return contentNode;
    }

    public void setContentNode(ContentNode contentNode) {
        this.contentNode = contentNode;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.content_list_element, parent, false);
        return new ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ContentNode contentNode = this.contentNode.getChildNodes().get(position);
        ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
        contentViewHolder.bindContent(contentNode);
        contentViewHolder.setListener(this.listener);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
        contentViewHolder.setListener(null);
    }

    @Override
    public int getItemCount() {
        if (contentNode == null) {
            return 0;
        }
        return contentNode.getChildNodes().size();
    }

    public ContentListAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    private class ContentViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView name;
        ImageView imageView;
        ContentNode contentNode;
        OnContentClickListener listener;

        void setListener(OnContentClickListener listener) {
            this.listener = listener;
        }

        OnContentClickListener getListener() {
            return this.listener;
        }

        ContentNode getContentNode() {
            return this.contentNode;
        }

        void bindContent(ContentNode contentNode) {
            this.contentNode = contentNode;
            name.setText(contentNode.name);
            switch (contentNode.type) {
                case "file":
                    imageView.setImageResource(R.drawable.file);
                    break;
                case "dir":
                    imageView.setImageResource(R.drawable.folder);
                    break;
            }
        }

        ContentViewHolder(View itemView) {
            super(itemView);
            name = (AppCompatTextView) itemView.findViewById(R.id.content_list_element_name);
            imageView = (ImageView) itemView.findViewById(R.id.content_list_element_image);
            itemView.setOnClickListener(view -> {
                if (getListener() != null && getContentNode() != null) {
                    getListener().onContentClicked(getContentNode());
                }
            });
        }
    }

    public interface OnContentClickListener {
        public void onContentClicked(ContentNode contentNode);
    }
}
