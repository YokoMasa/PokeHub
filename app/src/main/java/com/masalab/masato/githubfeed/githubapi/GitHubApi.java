package com.masalab.masato.githubfeed.githubapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.Nullable;

import com.masalab.masato.githubfeed.http.cache.HandyCache;
import com.masalab.masato.githubfeed.model.Commit;
import com.masalab.masato.githubfeed.model.ContentNode;
import com.masalab.masato.githubfeed.model.GitHubObjectMapper;
import com.masalab.masato.githubfeed.model.Profile;
import com.masalab.masato.githubfeed.model.Repository;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Masato on 2018/01/19.
 */

public class GitHubApi {

    public static final String GLOBAL_FEED_URL = "https://github.com/timeline";
    private GitHubTokenManager tokenManager;
    private GitHubResourceManager resourceManager;
    private ExecutorService executorService;
    private Profile currentUser;

    private static GitHubApi api;

    public static void init(Context appContext) {
        HandyCache.init(appContext);
        GitHubObjectMapper.init(appContext);
        api = new GitHubApi(appContext);
    }

    public static GitHubApi getApi() {
        if (api == null) {
            throw new RuntimeException("init() must be called before accessing to the api");
        }
        return api;
    }

    public Profile getCurrentUser() {
        return currentUser;
    }

    public void fetchFollowers(Profile profile, int page, GitHubApiCallback callback) {
        resourceManager.getFollowers(profile, page, callback);
    }

    public void fetchFollowings(Profile profile, int page, GitHubApiCallback callback) {
        resourceManager.getFollowings(profile, page, callback);
    }

    public void fetchProfileList(String url, int page, GitHubApiCallback callback) {
        resourceManager.getProfileList(url, page, callback);
    }

    public void fetchProfile(GitHubApiCallback callback) {
        resourceManager.getProfile(callback);
    }

    public void fetchProfile(String url, GitHubApiCallback callback) {
        resourceManager.getProfile(url, callback);
    }

    public void fetchFeedList(String url, int page, GitHubApiCallback callback) {
        resourceManager.getFeedEntries(url, page, callback);
    }

    public void fetchFeedUrl(GitHubApiCallback callback) {
        resourceManager.getFeedUrl(callback);
    }

    public void fetchBitmap(String url, GitHubApiCallback callback) {
        resourceManager.getBitmapFromUrl(url, callback);
    }

    public void fetchRepository(String url, GitHubApiCallback callback) {
        resourceManager.getRepository(url, callback);
    }

    public void searchRepos(@Nullable String q, @Nullable String sort, int page, GitHubApiCallback callback) {
        resourceManager.searchRepos(q, sort, page, callback);
    }

    public void fetchRepos(String url, int page, GitHubApiCallback callback) {
        resourceManager.getRepos(url, page, callback);
    }

    public void fetchReadMe(Repository repository, GitHubApiCallback callback) {
        resourceManager.getReadMeHtml(repository, callback);
    }

    public void isStarredByCurrentUser(Repository repository, GitHubApiCallback callback) {
        resourceManager.isStarredByCurrentUser(repository, callback);
    }

    public void isSubscribedByCurrentUser(Repository repository, GitHubApiCallback callback) {
        resourceManager.isSubscribedByCurrentUser(repository, callback);
    }

    public void starRepository(Repository repository, GitHubApiCallback callback) {
        resourceManager.starRepository(repository, callback);
    }

    public void subscribeRepository(Repository repository, GitHubApiCallback callback) {
        resourceManager.subscribeRepository(repository, callback);
    }

    public void unStarRepository(Repository repository, GitHubApiCallback callback) {
        resourceManager.unStarRepository(repository, callback);
    }

    public void unSubscribeRepository(Repository repository, GitHubApiCallback callback) {
        resourceManager.unSubscribeRepository(repository, callback);
    }

    public void fetchIssueList(String url, int page, GitHubApiCallback callback) {
        resourceManager.getIssueListFromUrl(url, page, callback);
    }

    public void fetchIssueList(Repository repository, int page,  GitHubApiCallback callback) {
        resourceManager.getRepositoryIssueList(repository, page, callback);
    }

    public void fetchIssue(String url, GitHubApiCallback callback) {
        resourceManager.getIssue(url, callback);
    }

    public void fetchPullRequest(String url, GitHubApiCallback callback) {
        resourceManager.getPullRequest(url, callback);
    }

    public void fetchPullRequestList(Repository repository, int page, GitHubApiCallback callback) {
        resourceManager.getRepositoryPullRequestList(repository, page, callback);
    }

    public void fetchCommentList(String url, int page, GitHubApiCallback callback) {
        resourceManager.getCommentListFromUrl(url, page, callback);
    }

    public void fetchCommitList(String url, int page, GitHubApiCallback callback) {
        resourceManager.getCommitListFromUrl(url, page, callback);
    }

    public void fetchCommitList(Repository repository, int page, GitHubApiCallback callback) {
        resourceManager.getRepositoryCommitList(repository, page, callback);
    }

    public void fetchDiffFileList(String url, GitHubApiCallback callback) {
        resourceManager.getDiffFileList(url, callback);
    }

    public void fetchCommitDiffFileList(Commit commit, GitHubApiCallback callback) {
        resourceManager.getCommitDiffFileList(commit, callback);
    }

    public void fetchEventList(String url, int page, GitHubApiCallback callback) {
        resourceManager.getEventList(url, page, callback);
    }

    public void fetchContentList(String url, GitHubApiCallback callback) {
        resourceManager.getContentList(url, callback);
    }

    public void fetchContent(ContentNode contentNode, GitHubApiCallback callback) {
        resourceManager.getContent(contentNode, callback);
    }

    public void fetchContentHtml(ContentNode contentNode, GitHubApiCallback callback) {
        resourceManager.getContentHtml(contentNode, callback);
    }

    public void fetchContentBitmap(ContentNode contentNode, GitHubApiCallback callback) {
        resourceManager.getContentBitmap(contentNode, callback);
    }

    public void fetchIssueEventList(String url, int page, GitHubApiCallback callback) {
        resourceManager.getIssueEventList(url, page, callback);
    }

    public void fetchOrgsMemberList(Profile profile, int page, GitHubApiCallback callback) {
        resourceManager.getProfileList(GitHubUrls.getOrgsMemberUrl(profile), page, callback);
    }

    public void fetchOrgsRepoList(Profile profile, int page, GitHubApiCallback callback) {
        resourceManager.getRepos(GitHubUrls.getOrgsReposUrl(profile), page, callback);
    }

    public void fetchOrgsEventList(Profile profile, int page, GitHubApiCallback callback) {
        resourceManager.getEventList(GitHubUrls.getOrgsEventUrl(profile), page, callback);
    }

    public void isFollowedByCurrentUser(Profile profile, GitHubApiCallback callback) {
        resourceManager.isFollowedByCurrentUser(profile, callback);
    }

    public void followUser(Profile profile, GitHubApiCallback callback) {
        resourceManager.followUser(profile, callback);
    }

    public void unFollowUser(Profile profile, GitHubApiCallback callback) {
        resourceManager.unFollowUser(profile, callback);
    }

    public void deleteToken() {
        tokenManager.deleteToken();
    }

    public void checkIfTokenValid(final GitHubApiCallback callback) {
        tokenManager.checkIfTokenValid(callback);
    }

    public void requestToken(String code, final GitHubApiCallback callback) {
        tokenManager.fetchToken(code, result -> {
            if (result.isSuccessful) {
                String token = (String) result.resultObject;
                resourceManager.updateToken(token);
            }
            callback.onApiResult(result);
        });
    }

    private void installProvider(Context appContext) {
        executorService.submit(() ->{
            try {
                ProviderInstaller.installIfNeeded(appContext);
            } catch (GooglePlayServicesRepairableException re) {
                GooglePlayServicesUtil.showErrorNotification(
                        re.getConnectionStatusCode(), appContext);
            } catch (GooglePlayServicesNotAvailableException ne) {
                ne.printStackTrace();
            }
        });
    }

    private void setCurrentUser() {
        resourceManager.getProfile(result -> {
            if (result.isSuccessful) {
                currentUser = (Profile) result.resultObject;
            }
        });
    }

    private GitHubApi(Context appContext) {
        SharedPreferences preferences = appContext.getSharedPreferences("token", Context.MODE_PRIVATE);
        Resources resources = appContext.getResources();
        this.executorService = Executors.newSingleThreadExecutor();
        installProvider(appContext);
        this.tokenManager = new GitHubTokenManager(resources, preferences, executorService);
        this.resourceManager = new GitHubResourceManager(tokenManager.getToken(), executorService, HandyCache.getInstance());
        setCurrentUser();
    }

}
