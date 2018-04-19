package com.masalab.masato.githubfeed.view.fragment.commitoverview;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.model.Commit;
import com.masalab.masato.githubfeed.model.Profile;
import com.masalab.masato.githubfeed.model.diff.DiffCodeLine;
import com.masalab.masato.githubfeed.model.diff.DiffFile;
import com.masalab.masato.githubfeed.util.DateUtil;
import com.masalab.masato.githubfeed.view.fragment.shared.MultiViewTypeAdapter;
import com.masalab.masato.githubfeed.view.fragment.shared.viewholders.DiffCodeLineViewHolder;
import com.masalab.masato.githubfeed.view.fragment.shared.viewholders.DiffFileHeaderViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Masato on 2018/02/19.
 */

public class CommitOverviewAdapter extends MultiViewTypeAdapter {

    private static final int HEADER_VIEW = 0;
    private static final int DIFF_HEADER_VIEW = 1;
    private static final int DIFF_CODE_LINE_VIEW = 2;

    private Context context;
    private LayoutInflater inflater;
    private OnProfileClickListener listener;

    public OnProfileClickListener getListener() {
        return listener;
    }

    public void setListener(OnProfileClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(RecyclerView.ViewHolder holder, ViewInfo viewInfo) {
        switch (viewInfo.viewType) {
            case HEADER_VIEW:
                Commit commit = (Commit) viewInfo.info;
                CommitHeaderViewHolder commitHeaderViewHolder = (CommitHeaderViewHolder) holder;
                commitHeaderViewHolder.bindCommit(commit);
                break;
            case DIFF_HEADER_VIEW:
                DiffFile diffFile = (DiffFile) viewInfo.info;
                DiffFileHeaderViewHolder diffFileHeaderViewHolder = (DiffFileHeaderViewHolder) holder;
                diffFileHeaderViewHolder.bindDiffFile(diffFile);
                break;
            case DIFF_CODE_LINE_VIEW:
                DiffCodeLine diffCodeLine = (DiffCodeLine) viewInfo.info;
                DiffCodeLineViewHolder diffCodeLineViewHolder = (DiffCodeLineViewHolder) holder;
                diffCodeLineViewHolder.bindCodeLine(diffCodeLine);
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case HEADER_VIEW:
                view = inflater.inflate(R.layout.commit_header, parent, false);
                return new CommitHeaderViewHolder(view);
            case DIFF_HEADER_VIEW:
                view = inflater.inflate(R.layout.diff_header, parent, false);
                return new DiffFileHeaderViewHolder(view);
            case DIFF_CODE_LINE_VIEW:
                view =inflater.inflate(R.layout.diff_code_line, parent, false);
                return new DiffCodeLineViewHolder(view, context);
        }
        return null;
    }

    public void setDiffFiles(List<DiffFile> diffFiles) {
        for (DiffFile diffFile : diffFiles) {
            addViewInfo(diffFile, DIFF_HEADER_VIEW);
            for (DiffCodeLine diffCodeLine : diffFile.codeLines) {
                addViewInfo(diffCodeLine, DIFF_CODE_LINE_VIEW);
            }
        }
        notifyDataSetChanged();
    }

    public CommitOverviewAdapter(Commit commit, Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        addViewInfo(commit, HEADER_VIEW, 0);
        notifyDataSetChanged();
    }

    class CommitHeaderViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView commitTitle;
        private LinearLayout committerWrapper;
        private AppCompatTextView committerName;
        private AppCompatTextView committerDate;
        private CircleImageView committerIcon;
        private LinearLayout authorWrapper;
        private AppCompatTextView authorName;
        private AppCompatTextView authorDate;
        private CircleImageView authorIcon;

        void bindCommit(Commit commit) {
            commitTitle.setText(commit.getShortenedComment());

            if (commit.committer != null) {
                committerWrapper.setVisibility(View.VISIBLE);
                committerWrapper.setOnClickListener(view -> {
                    if (getListener() != null) {
                        getListener().onProfileClicked(commit.committer);
                    }
                });
                committerName.setText(commit.committer.name);
                committerDate.setText(DateUtil.getReadableDateForFeed(commit.committerDate, context));
                Picasso.with(context).load(commit.committer.iconUrl).into(committerIcon);
            }

            if (commit.author != null) {
                authorWrapper.setVisibility(View.VISIBLE);
                authorWrapper.setOnClickListener(view -> {
                    if (getListener() != null) {
                        getListener().onProfileClicked(commit.author);
                    }
                });
                authorName.setText(commit.author.name);
                authorDate.setText(DateUtil.getReadableDateForFeed(commit.authorDate, context));
                Picasso.with(context).load(commit.author.iconUrl).into(authorIcon);
            }
        }

        private void findViews(View view) {
            commitTitle = (AppCompatTextView) view.findViewById(R.id.commit_header_message);
            committerWrapper = (LinearLayout) view.findViewById(R.id.commit_header_committer);
            committerName = (AppCompatTextView) view.findViewById(R.id.commit_header_committer_name);
            committerDate = (AppCompatTextView) view.findViewById(R.id.commit_header_committer_date);
            committerIcon = (CircleImageView) view.findViewById(R.id.commit_header_committer_icon);
            authorWrapper = (LinearLayout) view.findViewById(R.id.commit_header_author);
            authorName = (AppCompatTextView) view.findViewById(R.id.commit_header_author_name);
            authorDate = (AppCompatTextView) view.findViewById(R.id.commit_header_author_date);
            authorIcon = (CircleImageView) view.findViewById(R.id.commit_header_author_icon);
        }

        CommitHeaderViewHolder(View itemView) {
            super(itemView);
            findViews(itemView);
        }

    }

    public interface OnProfileClickListener {
        public void onProfileClicked(Profile profile);
    }
}
