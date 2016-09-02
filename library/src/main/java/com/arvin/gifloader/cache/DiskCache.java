package com.arvin.gifloader.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Movie;
import android.os.Environment;
import android.util.Log;

import com.arvin.gifloader.request.GifRequest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import disklrucache.DiskLruCache;
import disklrucache.IOUtil;

/**
 * Created by arvin on 2016/9/2.
 */
public class DiskCache implements GifCache {

    private static final int MB = 1024 * 1024;
    private static final String GIF_DISK_CACHE = "gif";

    private DiskLruCache mDiskLruCache;

    private static DiskCache mDiskCache;

    private DiskCache(Context context) {
        initDiskCache(context);
    }

    public static DiskCache getDiskCache(Context context) {
        if (mDiskCache == null) {
            synchronized (DiskCache.class) {
                if (mDiskCache == null) {
                    mDiskCache = new DiskCache(context);
                }
            }

        }
        return mDiskCache;
    }

    /**
     * 初始化sdcard缓存
     */
    private void initDiskCache(Context context) {
        try {
            File cacheDir = getDiskCacheDir(context, GIF_DISK_CACHE);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            mDiskLruCache = DiskLruCache
                    .open(cacheDir, getAppVersion(context), 1, 50 * MB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param context
     * @return
     */
    private int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * @param context
     * @param uniqueName
     * @return
     */
    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        Log.e("tag","----cache----" + cachePath);
        return new File(cachePath + File.separator + uniqueName);
    }


    @Override
    public Movie get(GifRequest key) {
        Movie movie = null;
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key.gifUrlMd5);
            if (snapshot != null) {
                movie = Movie.decodeStream(snapshot.getInputStream(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movie;
    }

    @Override
    public void put(GifRequest key, byte[] value) {
        DiskLruCache.Editor editor = null;
        try {
            editor = mDiskLruCache.edit(key.gifUrlMd5);
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(0);
                if (writeGifToDisk(value, outputStream)) {
                    editor.commit();
                } else {
                    editor.abort();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(GifRequest key) {
        try {
            mDiskLruCache.remove(key.gifUrlMd5);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean writeGifToDisk(byte[] value, OutputStream outputStream) {
        BufferedOutputStream bos = new BufferedOutputStream(outputStream, 8 * 1024);
        boolean result = true;
        try {
            outputStream.write(value);
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } finally {
            IOUtil.closeQuietly(bos);
        }

        return result;
    }
}
