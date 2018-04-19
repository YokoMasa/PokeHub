package com.masalab.masato.githubfeed.view.fragment.issuelist;

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
import com.masalab.masato.githubfeed.model.Issue;
import com.masalab.masato.githubfeed.model.Label;
import com.masalab.masato.githubfeed.navigator.Navigator;
import com.masalab.masato.githubfeed.util.DateUtil;

/**
 * Created by Masato on 2018/02/03.
 */

public class IssueListFragment extends PaginatingListFragment implements IssueListContract.View {

    @Override
    protected PaginatingListContract.Presenter onCreatePresenter() {
        IssueListContract.Presenter presenter = null;
        Repository repository = getArguments().getParcelable("repo");
        String url = getArguments().getString("url");
        if (repository != null) {
            presenter = new IssueListPresenter(repository);
        } else if (url != null) {
            presenter = new IssueListPresenter(url);
        } else {
            throw new RuntimeException("no valid argument");
        }
        presenter.setView(this);
        return presenter;
    }

    @Override
    public void showIssue(Issue issue) {
        Navigator.navigateToIssueView(getContext(), issue.url);
    }

    @Override
    protected PaginatingListViewHolder onCreatePaginatingViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.issue_list_element, parent, false);
        return new IssueViewHolder(view);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {

    }

    @Override
    protected void onBindViewHolder(PaginatingListViewHolder holder, BaseModel element, int viewType) {
        IssueViewHolder viewHolder = (IssueViewHolder) holder;
        Issue issue = (Issue) element;
        viewHolder.bindIssue(issue);
    }

    private class IssueViewHolder extends PaginatingListViewHolder {

        public AppCompatTextView number;
        public AppCompatTextView date;
        public AppCompatTextView title;
        public AppCompatTextView comments;

        void bindIssue(Issue issue) {
            number.setText("#" + Integer.toString(issue.number));
            date.setText(DateUtil.getReadableDateForFeed(issue.createdAt, getContext()));
            comments.setText(Integer.toString(issue.comments));
            setTitle(issue);
        }

        private void setTitle(Issue issue) {
            StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
            Spannable name = new SpannableString(issue.name);
            name.setSpan(bss, 0, name.length(), Spanned.SPAN_COMPOSING);

            CharSequence titleSequence = name;
            if (issue.labels != null) {
                for (Label label : issue.labels) {
                    titleSequence = TextUtils.concat(titleSequence, " ", label.getSpanned());
                }
            }
            title.setText(titleSequence);
        }

        public IssueViewHolder(View itemView) {
            super(itemView);
            number = (AppCompatTextView) itemView.findViewById(R.id.issue_list_element_number);
            title = (AppCompatTextView) itemView.findViewById(R.id.issue_list_element_title);
            date = (AppCompatTextView) itemView.findViewById(R.id.issue_list_element_date);
            comments = (AppCompatTextView) itemView.findViewById(R.id.issue_list_element_comment);
        }
    }
}
