package com.masalab.masato.githubfeed.http.cache.db;

/**
 * Created by Masato on 2018/03/01.
 */

public final class CacheInfoContract {

    static final String ID = "_id";
    static final String ETAG = "e_tag";
    static final String FILE_NAME = "file_name";
    static final String LAST_ACCESSED = "last_accessed";
    static final String ACCESS_COUNT = "access_count";
    static final String CACHE_INFO_TABLE = "cache_info";

    static final String CREATE_CACHE_INFO = "CREATE TABLE " + CACHE_INFO_TABLE + " ("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ETAG + " TEXT NOT NULL, "
            + FILE_NAME + " TEXT NOT NULL, "
            + ACCESS_COUNT + " INTEGER, "
            + LAST_ACCESSED + " INTEGER" + ")";

    static String getWhereClause(String urlHash) {
        return FILE_NAME + "='" + urlHash + "'";
    }

    private CacheInfoContract() {}
}
