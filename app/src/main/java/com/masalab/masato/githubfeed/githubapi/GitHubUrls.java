package com.masalab.masato.githubfeed.githubapi;

import com.masalab.masato.githubfeed.model.Issue;
import com.masalab.masato.githubfeed.model.Profile;
import com.masalab.masato.githubfeed.model.PullRequest;
import com.masalab.masato.githubfeed.model.Repository;

/**
 * Created by Masato on 2018/02/16.
 */

public class GitHubUrls {
    public static final String BASE_HTML_URL = "https://github.com";
    public static final String BASE_API_URL = "https://api.github.com";
    public static final String LOGIN_URL = "https://github.com/login/oauth/access_token";
    public static final String OAUTH_URL = "https://github.com/login/oauth/authorize";
    public static final String PROFILE_URL = BASE_API_URL + "/user";
    public static final String SEARCH_URL = BASE_API_URL + "/search/repositories";

    private static final String BASE_AUTH_CHECK_URL = BASE_API_URL + "/applications";
    private static final String BASE_STAR_URL = BASE_API_URL + "/user/starred";
    private static final String BASE_REPO_URL = BASE_API_URL + "/repos";
    private static final String BASE_FOLLOW_ACTION_URL = BASE_API_URL + "/user/following";

    public static String getAuthCheckUrl(String clientId, String token) {
        return BASE_AUTH_CHECK_URL + "/" + clientId + "/tokens/" + token;
    }

    public static String getLoginUrl(String clientId) {
        return OAUTH_URL + "?client_id=" + clientId + "&scope=repo%20user";
    }

    public static String getStarUrl(Repository repository) {
        return BASE_STAR_URL + "/" + repository.owner + "/" + repository.name;
    }

    public static String getRepoUrl(Repository repository) {
        return BASE_REPO_URL + "/" + repository.owner + "/" + repository.name;
    }

    public static String getSubscriptionUrl(Repository repository) {
        return getRepoUrl(repository) + "/subscription";
    }

    public static String getIssueListUrl(Repository repository) {
        return getRepoUrl(repository) + "/issues";
    }

    public static String getPullListUrl(Repository repository) {
        return getRepoUrl(repository) + "/pulls";
    }

    public static String getCommitListUrl(Repository repository) {
        return getRepoUrl(repository) + "/commits";
    }

    public static String getReadMeUrl(Repository repository) {
        return getRepoUrl(repository) + "/readme";
    }

    public static String getUserUrl(Profile profile) {
        return BASE_API_URL + "/users" + "/" + profile.name;
    }

    public static String getEventUrl(Profile profile) {
        return getUserUrl(profile) + "/events";
    }

    public static String getReceivedEventUrl(Profile profile) {
        return getUserUrl(profile) + "/received_events";
    }

    public static String getRepoContentUrl(Repository repository) {
        return getRepoUrl(repository) + "/contents";
    }

    public static String getIssueEventUrl(Issue issue) {
        return issue.url + "/events";
    }

    public static String getPREventUrl(PullRequest pr) { return pr.repository.baseUrl + "/issues/" + pr.number + "/events"; }

    public static String getRepoListUrl(Profile profile) {
        return getUserUrl(profile) + "/repos";
    }

    public static String getStarredRepoListUrl(Profile profile) {
        return getUserUrl(profile) + "/starred";
    }

    public static String getFollowersUrl(Profile profile) {
        return getUserUrl(profile) + "/followers";
    }

    public static String getFollowingsUrl(Profile profile) {
        return getUserUrl(profile) + "/following";
    }

    public static String getOrgsUrl(Profile profile) {
        return BASE_API_URL + "/orgs/" + profile.name;
    }

    public static String getOrgsReposUrl(Profile profile) {
        return getOrgsUrl(profile) + "/repos";
    }

    public static String getOrgsEventUrl(Profile profile) {
        return getOrgsUrl(profile) + "/events";
    }

    public static String getOrgsMemberUrl(Profile profile) {
        return getOrgsUrl(profile) + "/members";
    }

    public static String getFollowActionUrl(Profile profile) {
        return BASE_FOLLOW_ACTION_URL + "/" + profile.name;
    }

}
