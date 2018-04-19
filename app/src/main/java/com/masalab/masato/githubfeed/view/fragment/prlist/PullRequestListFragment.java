package com.masalab.masato.githubfeed.view.fragment.prlist;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.model.Repository;
import com.masalab.masato.githubfeed.view.fragment.paginatinglist.PaginatingListContract;
import com.masalab.masato.githubfeed.view.fragment.paginatinglist.PaginatingListFragment;
import com.masalab.masato.githubfeed.model.BaseModel;
import com.masalab.masato.githubfeed.model.Label;
import com.masalab.masato.githubfeed.model.PullRequest;
import com.masalab.masato.githubfeed.navigator.Navigator;
import com.masalab.masato.githubfeed.util.DateUtil;

/**
 * Created by Masato on 2018/02/08.
 */

public class PullRequestListFragment extends PaginatingListFragment implements PullRequestListContract.View {

    @Override
    protected PaginatingListContract.Presenter onCreatePresenter() {
        PullRequestListContract.Presenter presenter = null;
        Repository repository = getArguments().getParcelable("repo");
        if (repository != null) {
            presenter = new PullRequestListPresenter(repository);
        } else {
            throw new RuntimeException("no valid argument");
        }
        presenter.setView(this);
        return presenter;
    }

    @Override
    public void showPullRequest(PullRequest pr) {
        Navigator.navigateToPullRequestView(getContext(), pr.url);
    }

    @Override
    protected PaginatingListViewHolder onCreatePaginatingViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.pull_request_list_element, parent, false);
        return new PullRequestViewHolder(view);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {

    }

    @Override
    protected void onBindViewHolder(PaginatingListViewHolder holder, BaseModel element, int viewType) {
        PullRequest pr = (PullRequest) element;
        PullRequestViewHolder viewHolder = (PullRequestViewHolder) holder;
        viewHolder.bindPullRequest(pr);
    }

    private class PullRequestViewHolder extends PaginatingListViewHolder {

        AppCompatTextView date;
        AppCompatTextView number;
        AppCompatTextView title;

        void bindPullRequest(PullRequest pr) {
            date.setText(DateUtil.getReadableDateForFeed(pr.createdAt, getContext()));
            number.setText(getString(R.string.hash_tag) + pr.number);
            setTitle(pr);
        }

        private void setTitle(PullRequest pr) {
            StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
            Spannable name = new SpannableString(pr.name);
            name.setSpan(bss, 0, name.length(), Spanned.SPAN_COMPOSING);

            CharSequence titleSequence = name;
            if (pr.labels != null) {
                for (Label label : pr.labels) {
                    titleSequence = TextUtils.concat(titleSequence, " ", label.getSpanned());
                }
            }
            title.setText(titleSequence);
        }

        public PullRequestViewHolder(View itemView) {
            super(itemView);
            date = (AppCompatTextView) itemView.findViewById(R.id.pr_list_element_date);
            number = (AppCompatTextView) itemView.findViewById(R.id.pr_list_element_number);
            title = (AppCompatTextView) itemView.findViewById(R.id.pr_list_element_title);
        }
    }
}
