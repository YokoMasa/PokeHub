package com.masalab.masato.githubfeed.http;

/**
 * Created by Masato on 2018/01/31.
 */

public interface ResultBodyConverter {
    public Object convert(ConnectionResult successfulResult);
}
