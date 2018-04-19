package com.masalab.masato.githubfeed.githubapi;

import com.masalab.masato.githubfeed.R;

/**
 * Created by Masato on 2018/01/23.
 */

public enum Failure {

    INTERNET(R.string.failure_internet),
    INVALID_TOKEN(R.string.failure_invalid_token),
    NOT_FOUND(R.string.failure_not_found),
    UNEXPECTED(R.string.failure_unexpected),
    SERVER(R.string.failure_server),
    MAP(R.string.failure_map),
    CREATING_TOKEN(R.string.failure_creating_token);

    public int textId;

    private Failure(int textId) {
        this.textId = textId;
    }
}
