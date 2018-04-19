package com.masalab.masato.githubfeed.model.event;

import android.content.Context;

import com.masalab.masato.githubfeed.model.Action;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Masato on 2018/02/26.
 */

public class EventBuilder {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

    private JSONObject blueprints;

    public static EventBuilder from(Context context, int blueprintId) {
        try {
            InputStream is = context.getResources().openRawResource(blueprintId);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                builder.append(line);
                line = br.readLine();
            }
            JSONObject blueprints = new JSONObject(builder.toString());
            return new EventBuilder(blueprints);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EventBuilderError();
        }
    }

    public Event buildEvent(String type, JSONObject material) {
        JSONObject blueprint = blueprints.optJSONObject(type);
        if (blueprint == null) {
            return null;
        }

        Event event = new Event();
        event.content = buildComment(blueprint, material);
        event.action = buildAction(blueprint, material);
        event.iconUrl = buildIconUrl(blueprint, material);
        try {
            event.createdAt = dateFormat.parse(material.optString("created_at"));
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return event;
    }

    private String buildIconUrl(JSONObject blueprint, JSONObject material) {
        String raw = blueprint.optString("icon");
        return parse(raw, material);
    }

    private Action buildAction(JSONObject blueprint, JSONObject material) {
        String actionString = blueprint.optString("action");
        String rawTrigger = blueprint.optString("trigger");
        if (actionString.equals("") || rawTrigger.equals("")) {
            return null;
        }
        Action action = new Action();
        action.triggerUrl = parse(rawTrigger, material);
        switch (actionString) {
            case "REPO":
                action.type = Action.Type.REPO;
                break;
            case "ISSUE":
                action.type = Action.Type.ISSUE;
                break;
            case "PR":
                action.type = Action.Type.PR;
                break;
        }
        return action;
    }

    private String buildComment(JSONObject blueprint, JSONObject material) {
        JSONObject commentBlueprint = blueprint.optJSONObject("comment");
        if (commentBlueprint == null) {
            return "";
        }
        String template = getCommentTemplate(commentBlueprint, material);
        return parse(template, material);
    }

    private String getCommentTemplate(JSONObject commentBlueprint, JSONObject material) {
        if (commentBlueprint.has("switch")) {
            String switchKey = commentBlueprint.optString("switch");
            if (switchKey == null) {
                return "";
            }

            String switchWord = parse(switchKey, material);
            String template = commentBlueprint.optString(switchWord);
            if (template.equals("")) {
                return commentBlueprint.optString("default");
            } else {
                return template;
            }
        } else {
            return commentBlueprint.optString("default");
        }
    }

    private String parse(String raw, JSONObject material) {
        int varStartIndex = raw.indexOf("{");
        int varEndIndex = raw.indexOf("}");
        while (varStartIndex != -1) {
            String wholeCommand = raw.substring(varStartIndex, varEndIndex+1);
            String command = raw.substring(varStartIndex+1, varEndIndex);
            String commandResult = getStringFromCommand(command, material);
            raw = raw.replace(wholeCommand, commandResult);

            varStartIndex = raw.indexOf("{");
            varEndIndex = raw.indexOf("}");
        }
        return raw;
    }

    private String getStringFromCommand(String command, JSONObject material) {
        String[] dir = command.split("\\.");
        for (int i = 0;i<dir.length;i++) {
            if (i == dir.length -1) {
                Object o = material.opt(dir[i]);
                return o == null ? "" : o.toString();
            } else {
                material = material.optJSONObject(dir[i]);
            }
        }
        return "";
    }

    private EventBuilder(JSONObject blueprints) {
        this.blueprints = blueprints;
    }
}
