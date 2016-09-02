package com.arvin.gifloader.cache;

import android.graphics.Movie;

import com.arvin.gifloader.request.GifRequest;

/**
 * Created by arvin on 2016/9/2.
 */
public class DoubleCache implements GifCache{
    @Override
    public Movie get(GifRequest key) {
        return null;
    }

    @Override
    public void put(GifRequest key, byte[] value) {

    }

    @Override
    public void remove(GifRequest key) {

    }
}
