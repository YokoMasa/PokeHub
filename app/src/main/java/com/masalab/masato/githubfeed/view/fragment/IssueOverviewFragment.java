package com.masalab.masato.githubfeed.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.githubapi.GitHubUrls;
import com.masalab.masato.githubfeed.model.Issue;
import com.masalab.masato.githubfeed.navigator.Navigator;
import com.masalab.masato.githubfeed.util.DateUtil;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Masato on 2018/02/03.
 */

public class IssueOverviewFragment extends BaseFragment implements View.OnClickListener {

    private Issue issue;

    @Override
    public boolean isZombie() {
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onTryAgain() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_issue_overview, container, false);
        issue = getArguments().getParcelable("issue");
        initViews(view, issue);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (issue.author != null) {
            Navigator.navigateToProfileView(getContext(), issue.author.url);
        }
    }

    private void initViews(View view, Issue issue) {
        initState(view, issue);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.issue_overview_profile);
        linearLayout.setOnClickListener(this);

        AppCompatTextView authorName = (AppCompatTextView) view.findViewById(R.id.issue_overview_author_name);
        AppCompatTextView date = (AppCompatTextView) view.findViewById(R.id.issue_overview_date);
        AppCompatTextView title = (AppCompatTextView) view.findViewById(R.id.issue_overview_title);
        CircleImageView image = (CircleImageView) view.findViewById(R.id.issue_overview_image);
        WebView webView = (WebView) view.findViewById(R.id.issue_overview_comment_body);

        authorName.setText(issue.author.name);
        date.setText(DateUtil.getReadableDateForFeed(issue.createdAt, getContext()));
        title.setText(issue.name);
        webView.loadDataWithBaseURL(GitHubUrls.BASE_HTML_URL, issue.bodyHtml, "text/html", "utf-8", null);
        Picasso.with(getContext()).load(issue.author.iconUrl).into(image);
    }

    private void initState(View view, Issue issue) {
        AppCompatTextView stateTextView = view.findViewById(R.id.issue_overview_state);
        if (issue.state.equals(Issue.STATE_OPEN)) {
            stateTextView.setText(R.string.issue_state_open);
            stateTextView.setBackgroundColor(getResources().getColor(R.color.issue_open));
        } else {
            stateTextView.setText(R.string.issue_state_closed);
            stateTextView.setBackgroundColor(getResources().getColor(R.color.issue_closed));
        }
    }

}
