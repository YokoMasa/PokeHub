package com.masalab.masato.githubfeed.http.cache;

import android.util.LruCache;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Masato on 2018/02/18.
 */

public class HandyMemCache implements Cache {

    private static HandyMemCache instance;

    private LruCache<String, byte[]> cache;
    private Map<String, String> eTags = new HashMap<>();

    public static HandyMemCache getInstance() {
        if (instance == null) {
            instance = new HandyMemCache();
        }
        return instance;
    }

    @Override
    public boolean hasCache(String url) {
        return cache.get(url) != null;
    }

    @Override
    public byte[] getCachedBytes(String url) {
        return cache.get(url);
    }

    @Override
    public void cache(String url, String eTag,  byte[] bytes) {
        cache.put(url, bytes);
        eTags.put(url, eTag);
        int cacheSizeKb = cache.size() / 1024;
        int maxCacheSizeKb = cache.maxSize() / 1024;
        //Log.i("gh_feed", "cache size: " + cacheSizeKb + "/" + maxCacheSizeKb + "kb");
    }

    @Override
    public String getETag(String url) {
        return eTags.get(url);
    }

    private HandyMemCache() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        int cacheSize = (int) maxMemory / 8;
        cache = new LruCache<String, byte[]>(cacheSize) {
            @Override
            protected int sizeOf(String key, byte[] value) {
                return value.length;
            }
        };
    }
}
