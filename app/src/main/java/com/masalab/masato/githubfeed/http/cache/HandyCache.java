package com.masalab.masato.githubfeed.http.cache;

import android.content.Context;
import android.util.Log;

/**
 * Created by Masato on 2018/02/28.
 */

public class HandyCache implements Cache {

    private static HandyCache instance;

    private HandyMemCache memCache;
    private HandyDiskCache diskCache;

    public static HandyCache getInstance() {
        if (instance == null) {
            throw new RuntimeException("init() must be called first");
        }
        return instance;
    }

    public static void init(Context context) {
        instance = new HandyCache(context);
    }

    @Override
    public void cache(String url, String eTag, byte[] content) {
        memCache.cache(url, eTag, content);
        diskCache.cache(url, eTag, content);
    }

    @Override
    public byte[] getCachedBytes(String url) {
        byte[] bytes = memCache.getCachedBytes(url);
        if (bytes != null) {
            Log.i("gh_feed", "FROM MEM CACHE url: " + url);
            diskCache.updateLastAccessed(url);
            return bytes;
        }

        bytes = diskCache.getCachedBytes(url);
        if (bytes != null) {
            Log.i("gh_feed", "FROM DISK CACHE url: " + url);
            String eTag = diskCache.getETag(url);
            memCache.cache(url, eTag, bytes);
            return bytes;
        }
        return new byte[0];
    }

    @Override
    public String getETag(String url) {
        String eTag = memCache.getETag(url);
        if (eTag != null) {
            return eTag;
        }

        eTag = diskCache.getETag(url);
        if (eTag != null) {
            return eTag;
        }
        return "";
    }

    @Override
    public boolean hasCache(String url) {
        if (memCache.hasCache(url)) {
            return true;
        }
        if (diskCache.hasCache(url)) {
            return true;
        }
        return false;
    }

    private HandyCache(Context context) {
        memCache = HandyMemCache.getInstance();
        HandyDiskCache.init(context);
        diskCache = HandyDiskCache.getInstance();
    }
}
