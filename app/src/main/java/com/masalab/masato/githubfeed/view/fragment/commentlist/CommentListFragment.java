package com.masalab.masato.githubfeed.view.fragment.commentlist;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.githubapi.GitHubUrls;
import com.masalab.masato.githubfeed.model.BaseModel;
import com.masalab.masato.githubfeed.model.Comment;
import com.masalab.masato.githubfeed.model.Issue;
import com.masalab.masato.githubfeed.model.Profile;
import com.masalab.masato.githubfeed.model.PullRequest;
import com.masalab.masato.githubfeed.model.event.Event;
import com.masalab.masato.githubfeed.navigator.Navigator;
import com.masalab.masato.githubfeed.util.DateUtil;
import com.masalab.masato.githubfeed.util.EventUtil;
import com.masalab.masato.githubfeed.view.fragment.paginatinglist.PaginatingListContract;
import com.masalab.masato.githubfeed.view.fragment.paginatinglist.PaginatingListFragment;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Masato on 2018/02/03.
 */

public class CommentListFragment extends PaginatingListFragment implements CommentListContract.View {

    @Override
    protected PaginatingListContract.Presenter onCreatePresenter() {
        CommentListContract.Presenter presenter = null;
        Issue issue = getArguments().getParcelable("issue");
        PullRequest pr = getArguments().getParcelable("pr");
        if (issue != null) {
            presenter = new CommentListPresenter(issue);
        } else if (pr != null) {
            presenter = new CommentListPresenter(pr);
        } else {
            throw new RuntimeException("no valid argument.");
        }
        presenter.setView(this);
        return presenter;
    }

    @Override
    public void showProfile(Profile profile) {
        Navigator.navigateToProfileView(getContext(), profile.url);
    }

    @Override
    protected PaginatingListViewHolder onCreatePaginatingViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case COMMENT_VIEW:
                view = getLayoutInflater().inflate(R.layout.comment, parent, false);
                return new CommentViewHolder(view);
            case EVENT_VIEW:
                view = getLayoutInflater().inflate(R.layout.issue_event, parent, false);
                return new EventViewHolder(view);
            default:
                Log.i("gh_feed", "null view holder type: " + viewType);
                return null;
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        if (holder instanceof CommentViewHolder) {
            CommentViewHolder commentViewHolder = (CommentViewHolder) holder;
            commentViewHolder.image.setImageBitmap(null);
        }
    }

    @Override
    protected void onBindViewHolder(PaginatingListViewHolder holder, BaseModel element, int viewType) {
        switch (viewType) {
            case COMMENT_VIEW:
                CommentViewHolder commentViewHolder = (CommentViewHolder) holder;
                Comment comment = (Comment) element;
                commentViewHolder.bindComment(comment);
                break;
            case EVENT_VIEW:
                EventViewHolder eventViewHolder = (EventViewHolder) holder;
                Event event = (Event) element;
                eventViewHolder.bindEvent(event);
                break;
        }
    }

    private class CommentViewHolder extends PaginatingListViewHolder {

        AppCompatTextView authorName;
        AppCompatTextView date;
        CircleImageView image;
        WebView commentBody;
        private Comment comment;
        private boolean everLoaded;

        void bindComment(Comment comment) {
            this.comment = comment;
            authorName.setText(comment.author.name);
            date.setText(DateUtil.getReadableDateForFeed(comment.createdAt, getContext()));
            Picasso.with(getContext()).load(comment.author.iconUrl).into(image);
            if (!everLoaded) {
                commentBody.loadDataWithBaseURL(GitHubUrls.BASE_HTML_URL, comment.bodyHtml, "text/html", "utf-8", null);
                everLoaded = true;
            }
        }

        CommentViewHolder(View itemView) {
            super(itemView);
            authorName = (AppCompatTextView) itemView.findViewById(R.id.comment_author_name);
            date = (AppCompatTextView) itemView.findViewById(R.id.comment_date);
            image = (CircleImageView) itemView.findViewById(R.id.comment_image);
            commentBody = (WebView) itemView.findViewById(R.id.comment_body);
        }
    }

    private class EventViewHolder extends PaginatingListViewHolder {

        AppCompatTextView content;
        ImageView imageView;

        void bindEvent(Event event) {
            String date = DateUtil.getReadableDateForFeed(event.createdAt, getContext());
            content.setText(EventUtil.parseEventContent(event.content + " " + date));
            Picasso.with(getContext()).load(event.iconUrl).into(imageView);
        }

        EventViewHolder(View itemView) {
            super(itemView);
            this.content = (AppCompatTextView) itemView.findViewById(R.id.issue_event_comment);
            imageView = (ImageView) itemView.findViewById(R.id.issue_event_actor_image);
        }
    }
}
