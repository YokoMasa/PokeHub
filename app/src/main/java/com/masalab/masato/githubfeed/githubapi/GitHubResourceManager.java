package com.masalab.masato.githubfeed.githubapi;

import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

import com.masalab.masato.githubfeed.http.cache.Cache;
import com.masalab.masato.githubfeed.model.Commit;
import com.masalab.masato.githubfeed.model.ContentNode;
import com.masalab.masato.githubfeed.model.GitHubObjectMapper;
import com.masalab.masato.githubfeed.model.Profile;
import com.masalab.masato.githubfeed.model.Repository;
import com.masalab.masato.githubfeed.http.HandyHttpURLConnection;
import com.masalab.masato.githubfeed.http.HttpConnectionPool;

import java.util.concurrent.ExecutorService;

/**
 * Created by Masato on 2018/01/19.
 */

class GitHubResourceManager {

    private static final String MIME_TYPE_HTML = "application/vnd.github.v3.html+json";
    private static final String MIME_TYPE_DEFAULT = "application/vnd.github.v3+json";

    private GitHubObjectMapper mapper;
    private HttpConnectionPool connectionPool;

    void updateToken(String token) {
        connectionPool.setDefHeader("Authorization", "Token " + token);
    }

    void getProfile(final GitHubApiCallback callback) {
        final HandyHttpURLConnection connection = connectionPool.newConnection(GitHubUrls.PROFILE_URL);
        connection.get(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, successfulResult -> {
                return mapper.mapProfile(successfulResult.getBodyString());
            });
        });
    }

    void getProfile(String url, final GitHubApiCallback callback) {
        final HandyHttpURLConnection connection = connectionPool.newConnection(url);
        connection.get(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, successfulResult -> {
                return mapper.mapProfile(successfulResult.getBodyString());
            });
        });
    }

    void getFeedUrl(final GitHubApiCallback callback) {
        HandyHttpURLConnection connection = connectionPool.newConnection("https://api.github.com/feeds");
        connection.get(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, successfulResult -> {
                return mapper.mapFeedUrl(result.getBodyString());
            });
        });
    }

    void getFeedEntries(String url, int page, final GitHubApiCallback callback) {
        HandyHttpURLConnection connection = connectionPool.newConnection(url);
        connection.setHeader("Accept", "application/atom+xml");
        connection.addParams("page", Integer.toString(page));
        connection.get(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, successfulResult -> {
                return mapper.mapFeedEntries(successfulResult.getBodyString());
            });
        });
    }

    void getRepository(String url, final GitHubApiCallback callback) {
        HandyHttpURLConnection connection = connectionPool.newConnection(url);
        connection.get(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, successfulResult -> {
                return mapper.mapRepository(successfulResult.getBodyString());
            });
        });
    }

    void getRepos(String url, int page, GitHubApiCallback callback) {
        HandyHttpURLConnection connection = connectionPool.newConnection(url);
        connection.addParams("page", Integer.toString(page));
        connection.get(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, successfulResult -> {
                return mapper.mapRepoList(successfulResult.getBodyString());
            });
        });
    }

    void searchRepos(@Nullable String q, @Nullable String sort, int page, final GitHubApiCallback callback) {
        HandyHttpURLConnection connection = connectionPool.newConnection(GitHubUrls.SEARCH_URL);
        if (q != null) connection.addParams("q", q);
        if (sort != null) connection.addParams("sort", sort);
        connection.addParams("page", Integer.toString(page));
        connection.get(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, successfulResult -> {
                return mapper.mapRepoSearchResult(successfulResult.getBodyString(), q, sort);
            });
        });
    }

    void getIssue(String url, final GitHubApiCallback callback) {
        HandyHttpURLConnection connection = connectionPool.newConnection(url);
        connection.setHeader("Accept", MIME_TYPE_HTML);
        connection.get(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, successfulResult -> {
                return mapper.mapIssue(successfulResult.getBodyString());
            });
        });
    }

    void getPullRequest(String url, GitHubApiCallback callback) {
        HandyHttpURLConnection connection = connectionPool.newConnection(url);
        connection.setHeader("Accept", MIME_TYPE_HTML);
        connection.get(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, successfulResult -> {
                return mapper.mapPullRequest(successfulResult.getBodyString());
            });
        });
    }

    void getReadMeHtml(Repository repository, final GitHubApiCallback callback) {
        String url = GitHubUrls.getReadMeUrl(repository);
        HandyHttpURLConnection connection = connectionPool.newConnection(url);
        connection.setHeader("Accept", MIME_TYPE_HTML);
        connection.get(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, successfulResult -> {
                return successfulResult.getBodyString();
            });
        });
    }

    void isStarredByCurrentUser(Repository repository, final GitHubApiCallback callback) {
        String url = GitHubUrls.getStarUrl(repository);
        HandyHttpURLConnection connection = connectionPool.newConnection(url);
        connection.get(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, null);
        });
    }

    void starRepository(Repository repository, final GitHubApiCallback callback) {
        String url = GitHubUrls.getStarUrl(repository);
        HandyHttpURLConnection connection = connectionPool.newConnection(url);
        connection.setHeader("Content-Length", "0");
        connection.put(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, null);
        });
    }

    void unStarRepository(Repository repository, final GitHubApiCallback callback) {
        String url = GitHubUrls.getStarUrl(repository);
        HandyHttpURLConnection connection = connectionPool.newConnection(url);
        connection.delete(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, null);
        });
    }

    void isSubscribedByCurrentUser(Repository repository, final GitHubApiCallback callback) {
        String url = GitHubUrls.getSubscriptionUrl(repository);
        HandyHttpURLConnection connection = connectionPool.newConnection(url);
        connection.get(result -> {
            GitHubApiCallbackHandler. handleResult(result, callback, null);
        });
    }

    void subscribeRepository(Repository repository, final GitHubApiCallback callback) {
        String url = GitHubUrls.getSubscriptionUrl(repository);
        HandyHttpURLConnection connection = connectionPool.newConnection(url);
        connection.addParams("subscribed", "true");
        connection.put(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, null);
        });
    }

    void unSubscribeRepository(Repository repository, final GitHubApiCallback callback) {
        String url = GitHubUrls.getSubscriptionUrl(repository);
        HandyHttpURLConnection connection = connectionPool.newConnection(url);
        connection.delete(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, null);
        });
    }

    void getIssueListFromUrl(String url, int page, final GitHubApiCallback callback) {
        HandyHttpURLConnection connection = connectionPool.newConnection(url);
        connection.addParams("page", Integer.toString(page));
        connection.setHeader("Accept", MIME_TYPE_HTML);
        connection.get(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, successfulResult -> {
                return mapper.mapIssueList(successfulResult.getBodyString());
            });
        });
    }

    void getRepositoryIssueList(Repository repository, int page, final GitHubApiCallback callback) {
        String url = GitHubUrls.getIssueListUrl(repository);
        getIssueListFromUrl(url, page, callback);
    }

    void getRepositoryPullRequestList(Repository repository, int page, final GitHubApiCallback callback) {
        String url = GitHubUrls.getPullListUrl(repository);
        HandyHttpURLConnection connection = connectionPool.newConnection(url);
        connection.addParams("page", Integer.toString(page));
        connection.setHeader("Accept", MIME_TYPE_HTML);
        connection.get(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, successfulResult -> {
                return mapper.mapPullRequestList(successfulResult.getBodyString());
            });
        });
    }

    void getCommentListFromUrl(String url, int page, GitHubApiCallback callback) {
        HandyHttpURLConnection connection = connectionPool.newConnection(url);
        connection.addParams("page", Integer.toString(page));
        connection.setHeader("Accept", MIME_TYPE_HTML);
        connection.get(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, successfulResult -> {
                return mapper.mapCommentList(successfulResult.getBodyString());
            });
        });
    }

    void getRepositoryCommitList(Repository repository, int page, final GitHubApiCallback callback) {
        String url = GitHubUrls.getCommitListUrl(repository);
        getCommitListFromUrl(url, page, callback);
    }

    void getCommitListFromUrl(String url, int page, GitHubApiCallback callback) {
        HandyHttpURLConnection connection = connectionPool.newConnection(url);
        connection.addParams("page", Integer.toString(page));
        connection.get(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, successfulResult -> {
                return mapper.mapCommitList(successfulResult.getBodyString());
            });
        });
    }

    void getDiffFileList(String url, GitHubApiCallback callback) {
        HandyHttpURLConnection connection = connectionPool.newConnection(url);
        connection.get(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, successfulResult -> {
                return mapper.mapDiffFileList(successfulResult.getBodyString());
            });
        });
    }

    void getCommitDiffFileList(Commit commit, GitHubApiCallback callback) {
        HandyHttpURLConnection connection = connectionPool.newConnection(commit.url);
        connection.get(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, successfulResult -> {
                return mapper.mapCommitDiffFileList(successfulResult.getBodyString());
            });
        });
    }

    void getEventList(String url, int page, GitHubApiCallback callback) {
        HandyHttpURLConnection connection = connectionPool.newConnection(url);
        connection.addParams("page", Integer.toString(page));
        connection.setHeader("Accept", MIME_TYPE_HTML);
        connection.get(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, successfulResult -> {
                return mapper.mapEventList(successfulResult.getBodyString());
            });
        });
    }

    void getContentList(String url, GitHubApiCallback callback) {
        HandyHttpURLConnection connection = connectionPool.newConnection(url);
        connection.get(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, successfulResult -> {
                return mapper.mapContentNodeList(successfulResult.getBodyString());
            });
        });
    }

    void getContent(ContentNode contentNode, GitHubApiCallback callback) {
        HandyHttpURLConnection connection = connectionPool.newConnection(contentNode.url);
        connection.get(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, successfulResult -> {
                return mapper.mapStringFromContent(successfulResult.getBodyString());
            });
        });
    }

    void getContentHtml(ContentNode contentNode, GitHubApiCallback callback) {
        HandyHttpURLConnection connection = connectionPool.newConnection(contentNode.url);
        connection.setHeader("Accept", MIME_TYPE_HTML);
        connection.get(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, successfulResult -> {
                return successfulResult.getBodyString();
            });
        });
    }

    void getContentBitmap(ContentNode contentNode, GitHubApiCallback callback) {
        HandyHttpURLConnection connection = connectionPool.newConnection(contentNode.url);
        connection.get(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, successfulResult -> {
                return mapper.mapBitmapFromContent(successfulResult.getBodyString());
            });
        });
    }

    void getIssueEventList(String url, int page, GitHubApiCallback callback) {
        HandyHttpURLConnection connection = connectionPool.newConnection(url);
        connection.addParams("page", Integer.toString(page));
        connection.get(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, successfulResult -> {
                return mapper.mapIssueEventList(successfulResult.getBodyString());
            });
        });
    }

    void getProfileList(String url, int page, GitHubApiCallback callback) {
        HandyHttpURLConnection connection = connectionPool.newConnection(url);
        connection.addParams("page", Integer.toString(page));
        connection.get(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, successfulResult -> {
                return mapper.mapProfileList(successfulResult.getBodyString());
            });
        });
    }

    void getFollowers(Profile profile, int page, GitHubApiCallback callback) {
        getProfileList(GitHubUrls.getFollowersUrl(profile), page, callback);
    }

    void getFollowings(Profile profile, int page, GitHubApiCallback callback) {
        getProfileList(GitHubUrls.getFollowingsUrl(profile), page, callback);
    }

    void isFollowedByCurrentUser(Profile profile, GitHubApiCallback callback) {
        HandyHttpURLConnection connection = connectionPool.newConnection(GitHubUrls.getFollowActionUrl(profile));
        connection.get(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, null);
        });
    }

    void followUser(Profile profile, GitHubApiCallback callback) {
        HandyHttpURLConnection connection = connectionPool.newConnection(GitHubUrls.getFollowActionUrl(profile));
        connection.setHeader("Content-Length", "0");
        connection.put(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, null);
        });
    }

    void unFollowUser(Profile profile, GitHubApiCallback callback) {
        HandyHttpURLConnection connection = connectionPool.newConnection(GitHubUrls.getFollowActionUrl(profile));
        connection.delete(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, null);
        });
    }

    void getBitmapFromUrl(String url, final GitHubApiCallback callback) {
        HandyHttpURLConnection connection = connectionPool.newConnection(url);
        connection.get(result -> {
            GitHubApiCallbackHandler.handleResult(result, callback, successfulResult -> {
                byte[] bodyBytes = successfulResult.bodyBytes;
                return BitmapFactory.decodeByteArray(bodyBytes, 0, bodyBytes.length);
            });
        });
    }

    GitHubResourceManager(String token, ExecutorService service, Cache cache) {
        this.connectionPool = new HttpConnectionPool(service);
        this.connectionPool.setCache(cache);
        this.connectionPool.setDefHeader("User-Agent", "YokoMasa");
        this.connectionPool.setDefHeader("Authorization", "Token " + token);
        this.connectionPool.setDefHeader("Accept", MIME_TYPE_DEFAULT);
        mapper = GitHubObjectMapper.getInstance();
    }

}
