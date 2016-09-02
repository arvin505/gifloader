package com.arvin.gifloader.cache;

import android.content.Context;
import android.graphics.Movie;

import com.arvin.gifloader.request.GifRequest;

/**
 * Created by arvin on 2016/9/2.
 */
public class DoubleCache implements GifCache {

    private DiskCache mDiskCache;
    private GifCache mMemoryCache;

    public DoubleCache(Context context, GifCache mMemoryCache) {
        this.mMemoryCache = mMemoryCache;
        mDiskCache = DiskCache.getDiskCache(context.getApplicationContext());
    }

    @Override
    public Movie get(GifRequest key) {
        Movie movie = null;
        movie = mMemoryCache.get(key);
        if (movie == null) {
            movie = mDiskCache.get(key);
        }
        return movie;
    }

    @Override
    public void put(GifRequest key, byte[] value) {
        mMemoryCache.put(key, value);
        mDiskCache.put(key, value);
    }

    @Override
    public void remove(GifRequest key) {
        mMemoryCache.remove(key);
        mDiskCache.remove(key);
    }
}
