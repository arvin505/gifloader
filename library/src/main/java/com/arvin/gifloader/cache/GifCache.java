package com.arvin.gifloader.cache;

import android.graphics.Movie;

import com.arvin.gifloader.request.GifRequest;

/**
 * Created by arvin on 2016/9/2.
 */
public interface GifCache {

    public Movie get(GifRequest key);

    public void put(GifRequest key, byte[] value);

    public void remove(GifRequest key);
}
