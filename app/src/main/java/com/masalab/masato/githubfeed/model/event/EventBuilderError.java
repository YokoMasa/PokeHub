package com.masalab.masato.githubfeed.model.event;

/**
 * Created by Masato on 2018/02/28.
 */

public class EventBuilderError extends Error {
    @Override
    public String getMessage() {
        return "could not init EventBuilder. The app cannot proceed anymore.";
    }
}
