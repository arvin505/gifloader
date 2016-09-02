package com.arvin.gifloader.cache;

import android.graphics.Movie;
import android.util.LruCache;

import com.arvin.gifloader.request.GifRequest;

/**
 * Created by arvin on 2016/9/2.
 */
public class MemoryCache implements GifCache {
    private LruCache<String, byte[]> mMemoryCache;

    public MemoryCache() {
        final int maxMemory = (int) Runtime.getRuntime().maxMemory() / 1024;
        final int cacheSize = maxMemory / 4;
        mMemoryCache = new LruCache<String, byte[]>(cacheSize) {
            @Override
            protected int sizeOf(String key, byte[] value) {
                return value.length/1024;
            }
        };
    }

    @Override
    public Movie get(GifRequest key) {
        byte[] value = mMemoryCache.get(key.gifUrlMd5);
        if (value==null)return null;
        return Movie.decodeByteArray(value, 0, value.length);
    }

    @Override
    public void put(GifRequest key, byte[] value) {
        mMemoryCache.put(key.gifUrlMd5, value);
    }

    @Override
    public void remove(GifRequest key) {
        mMemoryCache.remove(key.gifUrlMd5);
    }
}
