package com.masalab.masato.githubfeed.navigator;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.masalab.masato.githubfeed.model.Action;
import com.masalab.masato.githubfeed.model.Commit;
import com.masalab.masato.githubfeed.model.ContentNode;
import com.masalab.masato.githubfeed.view.activity.commit.CommitActivity;
import com.masalab.masato.githubfeed.view.activity.content.ContentActivity;
import com.masalab.masato.githubfeed.view.activity.home.HomeActivity;
import com.masalab.masato.githubfeed.view.activity.GlobalFeedActivity;
import com.masalab.masato.githubfeed.view.activity.issue.IssueActivity;
import com.masalab.masato.githubfeed.view.activity.login.LogInActivity;
import com.masalab.masato.githubfeed.view.activity.profile.ProfileActivity;
import com.masalab.masato.githubfeed.view.activity.pr.PullRequestActivity;
import com.masalab.masato.githubfeed.view.activity.repo.RepoActivity;
import com.masalab.masato.githubfeed.view.activity.SearchActivity;
import com.masalab.masato.githubfeed.view.activity.TrendingActivity;

import java.util.List;

/**
 * Created by Masato on 2018/02/07.
 */

public class Navigator {

    public static void navigateToProfileView(Context context, String url) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    public static void navigateToSearchView(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    public static void navigateToTrending(Context context) {
        Intent intent = new Intent(context, TrendingActivity.class);
        context.startActivity(intent);
    }
    
    public static void navigateToLogInView(Context context) {
        Intent intent = new Intent(context, LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void navigateToHomeView(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void navigateToGlobalFeedView(Context context) {
        Intent intent = new Intent(context, GlobalFeedActivity.class);
        context.startActivity(intent);
    }

    public static void navigateToCommitView(Context context, Commit commit) {
        Intent intent = new Intent(context, CommitActivity.class);
        intent.putExtra("commit", commit);
        context.startActivity(intent);
    }

    public static void navigateToIssueView(Context context, String url) {
        Intent intent = new Intent(context, IssueActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    public static void navigateToRepoView(Context context, String url) {
        Intent intent = new Intent(context, RepoActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    public static void navigateToPullRequestView(Context context, String url) {
        Intent intent = new Intent(context, PullRequestActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    public static void navigateToContentView(Context context, ContentNode contentNode) {
        Intent intent = new Intent(context, ContentActivity.class);
        intent.putExtra("content_node", contentNode);
        context.startActivity(intent);
    }

    public static void navigateToExternalBrowser(Context context, String url) {
        if (url == null) {
            return;
        }
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        List<ResolveInfo> resolveInfoList =
                context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (0 < resolveInfoList.size()) {
            context.startActivity(intent);
        }
    }

    public static void navigateToEmail(Context context, String address) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + address));
        context.startActivity(intent);
    }

    public static void navigateFromAction(Context context, Action action) {
        if (action == null) {
            return;
        }
        switch (action.type) {
            case REPO:
                navigateToRepoView(context, action.triggerUrl);
                break;
            case PR:
                navigateToPullRequestView(context, action.triggerUrl);
                break;
            case ISSUE:
                navigateToIssueView(context, action.triggerUrl);
                break;
        }
    }
    
    private Navigator() {}
}
