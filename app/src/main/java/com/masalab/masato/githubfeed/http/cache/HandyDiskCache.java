package com.masalab.masato.githubfeed.http.cache;

import android.content.Context;
import android.util.Log;

import com.masalab.masato.githubfeed.http.cache.db.CacheInfo;
import com.masalab.masato.githubfeed.http.cache.db.CacheInfoStorage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Masato on 2018/02/28.
 */

public class HandyDiskCache implements Cache {

    private static final long MAX_CACHE_SIZE = 1024 * 1024 * 20; // 20Mb
    private static final String CACHE_DIR_NAME = "github_cache";

    private static HandyDiskCache instance;

    private CacheInfoStorage storage;
    private Set<String> busyFilePath = new HashSet<>();
    private long totalCacheSize = -1;
    private MessageDigest md;
    private File cacheDir;

    public static void init(Context context) {
        instance = new HandyDiskCache(context);
    }

    public static HandyDiskCache getInstance() {
        if (instance == null) {
            throw new RuntimeException("init() must be called first.");
        }
        return instance;
    }

    private void addCacheSize(long size) {
        if (totalCacheSize == -1) {
            totalCacheSize = getSize(cacheDir);
        }
        totalCacheSize += size;
    }

    private long getTotalCacheSize() {
        if (totalCacheSize == -1) {
            totalCacheSize = getSize(cacheDir);
        }
        return totalCacheSize;
    }

    @Override
    public void cache(String url, String eTag, byte[] content) {
        File cacheFile = getCacheFile(url);
        writeFile(cacheFile, content);

        addCacheSize(getSize(cacheFile));

        CacheInfo cacheInfo = new CacheInfo();
        cacheInfo.fileName = getFileName(url);
        cacheInfo.eTag = eTag;
        cacheInfo.lastAccessed = new Date().getTime();

        synchronized (storage) {
            storage.insertCacheInfo(cacheInfo);
        }

        deleteCacheIfNeeded();
        Log.i("gh_feed", "total cache size: " + getTotalCacheSize()/1024 + "kb");
    }

    @Override
    public byte[] getCachedBytes(String url) {
        updateLastAccessed(url);
        File file = getCacheFile(url);
        return readFile(file);
    }

    public void updateLastAccessed(String url) {
        synchronized (storage) {
            CacheInfo cacheInfo = storage.selectCacheInfo(getFileName(url));
            if (cacheInfo != null) {
                cacheInfo.lastAccessed = new Date().getTime();
                cacheInfo.accessCount++;
                Log.i("gh_feed", "access count: " + cacheInfo.accessCount + "  url: " + url);
                storage.updateCacheInfo(cacheInfo);
            }
        }
    }

    @Override
    public boolean hasCache(String url) {
        File file = getCacheFile(url);
        synchronized (storage) {
            if (file.exists()) {
                CacheInfo cacheInfo = storage.selectCacheInfo(getFileName(url));
                if (cacheInfo != null) {
                    return true;
                } else {
                    file.delete();
                    return false;
                }
            } else {
                CacheInfo cacheInfo = storage.selectCacheInfo(getFileName(url));
                //Log.i("gh_feed", "file doesn't exist.");
                if (cacheInfo != null) {
                    storage.deleteCacheInfo(cacheInfo.fileName);
                }
                return false;
            }
        }
    }

    @Override
    public String getETag(String url) {
        String key = getFileName(url);
        CacheInfo cacheInfo = null;

        synchronized (storage) {
            cacheInfo = storage.selectCacheInfo(key);
        }

        if (cacheInfo == null) {
            return "";
        }
        return cacheInfo.eTag;
    }

    private File getCacheFile(String url) {
        String fileName = getFileName(url);
        return new File(cacheDir, fileName);
    }

    private byte[] readFile(File file) {
        while (isBusy(file)) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        setBusy(file);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b = fileInputStream.read();
            while (b != -1) {
                baos.write(b);
                b = fileInputStream.read();
            }
            return baos.toByteArray();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return new byte[0];
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            file.setLastModified(new Date().getTime());
            setUnBusy(file);
        }
    }

    private void writeFile(File file, byte[] bytes) {
        while (isBusy(file)) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        setBusy(file);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            file.setLastModified(new Date().getTime());
            setUnBusy(file);
        }
    }

    private void setBusy(File file) {
        if (file == null) {
            return;
        }
        busyFilePath.add(file.getPath());
    }

    private void setUnBusy(File file) {
        if (file == null) {
            return;
        }
        busyFilePath.remove(file.getPath());
    }

    private boolean isBusy(File file) {
        if (file == null) {
            return false;
        }
        return busyFilePath.contains(file.getPath());
    }

    private void deleteCacheIfNeeded() {
        while (MAX_CACHE_SIZE < getTotalCacheSize()) {
            deleteOldest();
            Log.i("gh_feed", "total cache size: " + getTotalCacheSize()/1024 + "kb");
        }
    }

    private void deleteOldest() {
        synchronized (storage) {
            CacheInfo cacheInfo = storage.selectOldest();
            File target = new File(cacheDir, cacheInfo.fileName);

            if (!target.exists()) {
                storage.deleteCacheInfo(cacheInfo.fileName);
                File[] cacheFiles = cacheDir.listFiles();
                if (0 < cacheFiles.length) {
                    target = cacheFiles[cacheFiles.length-1];
                }
            }

            long targetDirSize = getSize(target);
            if (target.delete()) {
                Log.i("gh_feed", "cache deleted. Access count was " + cacheInfo.accessCount);
                addCacheSize(-targetDirSize);
                storage.deleteCacheInfo(target.getName());
            }
        }
    }

    private long getSize(File file) {
        if (file == null) {
            return 0;
        }

        if (file.isFile()) {
            return file.length();
        }

        long size = 0;
        for (File child : file.listFiles()) {
            if (file.isDirectory()) {
                size += getSize(child);
            } else {
                size += child.length();
            }
        }
        return size;
    }

    private String getFileName(String url) {
        md.update(url.getBytes());
        byte[] bytes = md.digest();
        StringBuilder builder = new StringBuilder("cache");
        for (byte b : bytes) {
            builder.append(b);
        }
        return builder.toString();
    }

    private HandyDiskCache(Context context) {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        File dir = new File(context.getCacheDir(), CACHE_DIR_NAME);
        if (!dir.exists()) {
            dir.mkdir();
        }
        cacheDir = dir;

        storage = new CacheInfoStorage(context);
    }
}
