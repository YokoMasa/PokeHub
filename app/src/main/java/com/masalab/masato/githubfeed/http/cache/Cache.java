package com.masalab.masato.githubfeed.http.cache;

/**
 * Created by Masato on 2018/02/28.
 */

public interface Cache {

    public void cache(String url, String eTag, byte[] content);

    public byte[] getCachedBytes(String url);

    public boolean hasCache(String url);

    public String getETag(String url);

}
