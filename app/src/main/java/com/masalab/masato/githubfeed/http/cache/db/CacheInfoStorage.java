package com.masalab.masato.githubfeed.http.cache.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Map;

import static com.masalab.masato.githubfeed.http.cache.db.CacheInfoContract.*;

/**
 * Created by Masato on 2018/03/01.
 */

public class CacheInfoStorage extends SQLiteOpenHelper {

    private static final String DB_NAME = "git_hub_feed";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CACHE_INFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }

    public boolean insertCacheInfo(CacheInfo cacheInfo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(FILE_NAME, cacheInfo.fileName);
        content.put(ETAG, cacheInfo.eTag);
        content.put(LAST_ACCESSED, cacheInfo.lastAccessed);
        content.put(ACCESS_COUNT, cacheInfo.accessCount);
        long result = db.insert(CACHE_INFO_TABLE, null, content);
        return result != -1;
    }

    public Map<String, CacheInfo> selectAll() {
        Map<String, CacheInfo> cacheInfoMap = new HashMap<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor =
                db.query(CACHE_INFO_TABLE, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            CacheInfo cacheInfo = new CacheInfo();
            cacheInfo = new CacheInfo();
            cacheInfo.eTag = cursor.getString(cursor.getColumnIndex(ETAG));
            cacheInfo.fileName = cursor.getString(cursor.getColumnIndex(FILE_NAME));
            cacheInfo.lastAccessed = cursor.getLong(cursor.getColumnIndex(LAST_ACCESSED));
            cacheInfo.accessCount = cursor.getInt(cursor.getColumnIndex(ACCESS_COUNT));
            cacheInfoMap.put(cacheInfo.fileName, cacheInfo);
        }
        cursor.close();
        return cacheInfoMap;
    }

    public CacheInfo selectCacheInfo(String fileName) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor =
                db.query(CACHE_INFO_TABLE, null, getWhereClause(fileName), null, null, null, null);
        CacheInfo cacheInfo = null;
        if (cursor.moveToFirst()) {
            cacheInfo = new CacheInfo();
            cacheInfo.eTag = cursor.getString(cursor.getColumnIndex(ETAG));
            cacheInfo.fileName = cursor.getString(cursor.getColumnIndex(FILE_NAME));
            cacheInfo.lastAccessed = cursor.getLong(cursor.getColumnIndex(LAST_ACCESSED));
            cacheInfo.accessCount = cursor.getInt(cursor.getColumnIndex(ACCESS_COUNT));
        }
        cursor.close();
        return cacheInfo;
    }

    public void deleteCacheInfo(String fileName) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(CACHE_INFO_TABLE, getWhereClause(fileName), null);
    }

    public void updateCacheInfo(CacheInfo cacheInfo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(FILE_NAME, cacheInfo.fileName);
        content.put(ETAG, cacheInfo.eTag);
        content.put(LAST_ACCESSED, cacheInfo.lastAccessed);
        content.put(ACCESS_COUNT, cacheInfo.accessCount);
        db.update(CACHE_INFO_TABLE, content, getWhereClause(cacheInfo.fileName), null);
    }

    public CacheInfo selectOldest() {
        SQLiteDatabase db = getReadableDatabase();
        String orderByClause = ACCESS_COUNT + " ASC";
        Cursor cursor =
                db.query(CACHE_INFO_TABLE, null, null, null, null, null, orderByClause, "1");
        CacheInfo cacheInfo = null;
        if (cursor.moveToFirst()) {
            cacheInfo = new CacheInfo();
            cacheInfo.eTag = cursor.getString(cursor.getColumnIndex(ETAG));
            cacheInfo.fileName = cursor.getString(cursor.getColumnIndex(FILE_NAME));
            cacheInfo.lastAccessed = cursor.getLong(cursor.getColumnIndex(LAST_ACCESSED));
            cacheInfo.accessCount = cursor.getInt(cursor.getColumnIndex(ACCESS_COUNT));
        }
        cursor.close();
        return cacheInfo;
    }

    public CacheInfoStorage(Context context) {
        super(context, DB_NAME, null, 1);
    }
}
