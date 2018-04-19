package com.masalab.masato.githubfeed.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.model.diff.DiffFile;
import com.masalab.masato.githubfeed.model.diff.DiffParser;
import com.masalab.masato.githubfeed.model.event.Event;
import com.masalab.masato.githubfeed.model.event.EventBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by Masato on 2018/01/19.
 */

public class GitHubObjectMapper {

    private static GitHubObjectMapper instance;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    private EventBuilder eventBuilder;
    private EventBuilder issueEventBuilder;

    public static void init(Context context) {
        instance = new GitHubObjectMapper(context);
    }

    public static GitHubObjectMapper getInstance() {
        if (instance == null) {
            throw new RuntimeException("init() must be called first before getting an instance.");
        }
        return instance;
    }

    public List<FeedEntry> mapFeedEntries(String feedString) {
        return XmlFeedParser.parse(feedString);
    }

    public Profile mapProfile(String profileString) {
        try {
            JSONObject jsonObject = new JSONObject(profileString);
            return mapProfile(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Profile mapProfile(JSONObject jsonObject) {
        try {
            Profile profile = new Profile();
            profile.name = jsonObject.getString("login");
            profile.iconUrl = jsonObject.getString("avatar_url");
            profile.url = jsonObject.getString("url");
            profile.htmlUrl = jsonObject.getString("html_url");
            profile.type = jsonObject.getString("type");
            profile.realName = jsonObject.optString("name");
            profile.company = jsonObject.optString("company");
            profile.blog = jsonObject.optString("blog");
            profile.location = jsonObject.optString("location");
            profile.email = jsonObject.optString("email");
            profile.bio = jsonObject.optString("bio");
            return profile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Profile> mapProfileList(String jsonString) {
        try {
            List<Profile> profiles = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Profile profile = mapProfile(jsonObject);
                if (profile != null) {
                    profiles.add(profile);
                }
            }
            return profiles;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String mapFeedUrl(String feedUrlJson) {
        try {
            JSONObject jsonObject = new JSONObject(feedUrlJson);
            return jsonObject.getString("current_user_public_url");
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Repository mapRepository(String repositoryString) {
        try {
            JSONObject jsonObject = new JSONObject(repositoryString);
            return mapRepository(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Repository mapRepository(JSONObject jsonObject) {
        try {
            Repository repository = new Repository();
            repository.fullName = jsonObject.getString("full_name");
            repository.htmlUrl = jsonObject.getString("html_url");
            repository.baseUrl = jsonObject.getString("url");
            repository.name = jsonObject.getString("name");
            repository.stars = jsonObject.getInt("stargazers_count");
            repository.watches = jsonObject.optInt("subscribers_count");
            repository.forks = jsonObject.getInt("forks_count");
            repository.lang = jsonObject.optString("language");
            repository.description = jsonObject.optString("description");
            repository.ownerProfile = mapProfile(jsonObject.optString("owner"));
            repository.parent = mapRepository(jsonObject.optJSONObject("parent"));
            JSONObject ownerJsonObject = jsonObject.getJSONObject("owner");
            repository.owner = ownerJsonObject.getString("login");
            return repository;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Repository> mapRepoList(String jsonString) {
        try {
            List<Repository> repositories = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Repository repository = mapRepository(jsonObject);
                if (repository != null) {
                    repositories.add(repository);
                }
            }
            return repositories;
        } catch (JSONException je) {
            je.printStackTrace();
            return null;
        }
    }

    public List<Repository> mapRepoList(JSONArray jsonArray) {
        try {
            List<Repository> repositories = new ArrayList<>();
            for (int i = 0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Repository repository = mapRepository(jsonObject);
                if (repository != null) {
                    repositories.add(repository);
                }
            }
            return repositories;
        } catch (JSONException je) {
            je.printStackTrace();
            return null;
        }
    }

    public RepoSearchResult mapRepoSearchResult(String jsonString, String q, String sort) {
        try {
            RepoSearchResult result = new RepoSearchResult();
            JSONObject root = new JSONObject(jsonString);
            JSONArray jsonArray = root.getJSONArray("items");
            result.repos = mapRepoList(jsonArray);
            result.q = q;
            result.sort = sort;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Comment mapComment(JSONObject jsonObject) {
        try {
            Comment comment = new Comment();
            comment.author = mapProfile(jsonObject.getJSONObject("user"));
            comment.bodyHtml = wrap(jsonObject.getString("body_html"));
            comment.createdAt = dateFormat.parse(jsonObject.getString("created_at"));
            return comment;
        } catch (Exception e) {
            e.printStackTrace();;
            return null;
        }
    }

    public List<Comment> mapCommentList(String jsonString) {
        try {
            List<Comment> comments = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                comments.add(mapComment(jsonObject));
            }
            return comments;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Issue mapIssue(JSONObject jsonObject) {
        try {
            Issue issue = new Issue();
            issue.url = jsonObject.getString("url");
            issue.htmlUrl = jsonObject.getString("html_url");
            issue.repoUrl = jsonObject.getString("repository_url");
            issue.name = jsonObject.getString("title");
            issue.number = jsonObject.getInt("number");
            issue.bodyHtml = wrap(jsonObject.optString("body_html"));
            issue.createdAt = dateFormat.parse(jsonObject.getString("created_at"));
            issue.commentsUrl = jsonObject.getString("comments_url");
            issue.author = mapProfile(jsonObject.getJSONObject("user"));
            issue.comments = jsonObject.getInt("comments");
            issue.state = jsonObject.getString("state");
            issue.labels = mapLabelList(jsonObject.optJSONArray("labels"));
            //issue.repository = mapRepository(jsonObject.optJSONObject("repository"));
            return issue;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Issue mapIssue(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return mapIssue(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Issue> mapIssueList(String jsonString) {
        try {
            List<Issue> issues = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (!jsonObject.has("pull_request")) {
                    issues.add(mapIssue(jsonObject));
                }
            }
            return issues;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public PullRequest mapPullRequest(JSONObject jsonObject) {
        try {
            PullRequest pr = new PullRequest();
            pr.url = jsonObject.getString("url");
            pr.htmlUrl = jsonObject.getString("html_url");
            pr.name = jsonObject.getString("title");
            pr.number = jsonObject.getInt("number");
            pr.bodyHtml = wrap(jsonObject.optString("body_html"));
            pr.createdAt = dateFormat.parse(jsonObject.getString("created_at"));
            pr.commentsUrl = jsonObject.getString("comments_url");
            pr.author = mapProfile(jsonObject.getJSONObject("user"));
            pr.state = jsonObject.getString("state");
            pr.diffUrl = jsonObject.getString("diff_url");
            pr.commitsUrl = jsonObject.getString("commits_url");
            pr.labels = mapLabelList(jsonObject.optJSONArray("labels"));
            pr.repository = mapRepository(jsonObject.getJSONObject("base").getJSONObject("repo"));
            return pr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public PullRequest mapPullRequest(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return mapPullRequest(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<PullRequest> mapPullRequestList(String jsonString) {
        try {
            List<PullRequest> prs = new ArrayList<>();

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                prs.add(mapPullRequest(jsonObject));
            }
            return prs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Commit mapCommit(JSONObject jsonObject) {
        try {
            Commit commit = new Commit();
            commit.sha = jsonObject.getString("sha");
            commit.url = jsonObject.getString("url");
            commit.htmlUrl = jsonObject.getString("html_url");
            commit.committer = mapProfile(jsonObject.optJSONObject("committer"));
            commit.author = mapProfile(jsonObject.optJSONObject("author"));
            JSONObject commitObject = jsonObject.getJSONObject("commit");
            commit.comment = commitObject.getString("message");
            JSONObject authorObject = commitObject.optJSONObject("author");
            if (authorObject != null) {
                commit.authorDate = dateFormat.parse(authorObject.getString("date"));
            }
            JSONObject committerObject = commitObject.optJSONObject("committer");
            if (committerObject != null) {
                commit.committerDate = dateFormat.parse(committerObject.getString("date"));
            }
            return commit;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Commit> mapCommitList(String jsonString) {
        try {
            List<Commit> commits = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                commits.add(mapCommit(jsonObject));
            }
            return commits;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<DiffFile> mapCommitDiffFileList(String jsonString) {
        try {
            List<DiffFile> diffFiles = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray filesArray = jsonObject.getJSONArray("files");
            for (int i = 0;i<filesArray.length();i++) {
                JSONObject fileObject = filesArray.getJSONObject(i);
                DiffFile diffFile = DiffParser.parseDiffFile(fileObject.optString("patch"), fileObject.optString("filename"));
                diffFiles.add(diffFile);
            }
            return diffFiles;
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    public List<DiffFile> mapDiffFileList(String diffString) {
        return DiffParser.parseDiffFiles(diffString);
    }

    public List<Event> mapEventList(String jsonString) {
        try {
            List<Event> events = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String type = jsonObject.optString("type");
                Event event = eventBuilder.buildEvent(type, jsonObject);
                events.add(event);
            }
            return events;
        } catch (JSONException je) {
            je.printStackTrace();
            return null;
        }
    }

    public List<ContentNode> mapContentNodeList(String jsonString) {
        try {
            List<ContentNode> list = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                list.add(mapContentNode(jsonObject));
            }
            Collections.sort(list);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ContentNode mapContentNode(JSONObject jsonObject) {
        try {
            ContentNode contentNode = new ContentNode();
            contentNode.type = jsonObject.getString("type");
            contentNode.url = jsonObject.getString("url");
            contentNode.name = jsonObject.getString("name");
            return contentNode;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String mapErrorMessage(String jsonString) {
        String string = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            string = jsonObject.getString("message");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    public String mapStringFromContent(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String encodedString = jsonObject.getString("content");
            return new String(Base64.decode(encodedString, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap mapBitmapFromContent(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String encodedString = jsonObject.getString("content");
            byte[] bytes = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Event> mapIssueEventList(String jsonString) {
        try {
            List<Event> events = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String type = jsonObject.optString("event");
                Event event = issueEventBuilder.buildEvent(type, jsonObject);
                if (event != null) {
                    events.add(event);
                }
            }
            return events;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Label> mapLabelList(JSONArray jsonArray) {
        try {
            List<Label> labels = new ArrayList<>();
            for (int i = 0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Label label = new Label();
                label.name = jsonObject.getString("name");
                label.colorString = jsonObject.getString("color");
                labels.add(label);
            }
            return labels;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String wrap(String htmlString) {
        return "<html><head><meta name=\"viewport\" content=\"width=device-width,initial-scale=1\"></head><body>" +
                "<div style=\"word-wrap: break-word;\">" +
                htmlString +
                "</div></body></html>";
    }

    private GitHubObjectMapper(Context context) {
        eventBuilder = EventBuilder.from(context, R.raw.event_blueprint);
        issueEventBuilder = EventBuilder.from(context, R.raw.issue_event_blueprint);
    }

}
