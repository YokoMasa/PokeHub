package com.masalab.masato.githubfeed.view.fragment.commitlist;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.model.Repository;
import com.masalab.masato.githubfeed.view.fragment.paginatinglist.PaginatingListContract;
import com.masalab.masato.githubfeed.view.fragment.paginatinglist.PaginatingListFragment;
import com.masalab.masato.githubfeed.model.BaseModel;
import com.masalab.masato.githubfeed.model.Commit;
import com.masalab.masato.githubfeed.navigator.Navigator;
import com.masalab.masato.githubfeed.util.DateUtil;

/**
 * Created by Masato on 2018/02/06.
 */

public class CommitListFragment extends PaginatingListFragment implements CommitListContract.View {

    @Override
    protected PaginatingListContract.Presenter onCreatePresenter() {
        CommitListContract.Presenter presenter = null;
        Repository repository = getArguments().getParcelable("repo");
        String url = getArguments().getString("url");
        if (repository != null) {
            presenter = new CommitListPresenter(repository);
        } else if (url != null) {
            presenter = new CommitListPresenter(url);
        } else {
            throw new RuntimeException("no valid argument");
        }
        presenter.setView(this);
        return presenter;
    }

    @Override
    public void showCommit(Commit commit) {
        Navigator.navigateToCommitView(getContext(), commit);
    }

    @Override
    protected PaginatingListViewHolder onCreatePaginatingViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.commit_list_element, parent, false);
        return new CommitViewHolder(view);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {

    }

    @Override
    protected void onBindViewHolder(PaginatingListViewHolder holder, BaseModel element, int viewType) {
        CommitViewHolder commitViewHolder = (CommitViewHolder) holder;
        Commit commit = (Commit) element;
        commitViewHolder.bindCommit(commit);
    }

    private class CommitViewHolder extends PaginatingListViewHolder {

        AppCompatTextView date;
        AppCompatTextView comment;
        AppCompatTextView sha;

        void bindCommit(Commit commit) {
            comment.setText(commit.getShortenedComment());
            sha.setText(commit.getShortenedSha());
            if (commit.committerDate != null) {
                date.setText(DateUtil.getReadableDateForFeed(commit.committerDate, getContext()));
            } else if (commit.authorDate != null) {
                date.setText(DateUtil.getReadableDateForFeed(commit.authorDate, getContext()));
            }
        }

        public CommitViewHolder(View itemView) {
            super(itemView);
            date = (AppCompatTextView) itemView.findViewById(R.id.commit_list_element_date);
            comment = (AppCompatTextView) itemView.findViewById(R.id.commit_list_element_comment);
            sha = (AppCompatTextView) itemView.findViewById(R.id.commit_list_element_sha);
        }
    }
}
