package com.arvin.gifloader.cache;

import android.graphics.Movie;

import com.arvin.gifloader.request.GifRequest;

import java.lang.ref.Reference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by arvin on 2016/9/2.
 */
public abstract class ReferenceCache implements GifCache {
    private final Map<String, Reference<byte[]>> referenceMap = Collections.synchronizedMap(new HashMap<String, Reference<byte[]>>());

    @Override
    public Movie get(GifRequest key) {
        Movie movie = null;
        Reference<byte[]> reference = referenceMap.get(key.gifUrlMd5);
        if (reference != null) {
            byte[] value = reference.get();
            movie = Movie.decodeByteArray(value, 0, value.length);
        }
        return movie;
    }

    @Override
    public void put(GifRequest key, byte[] value) {
        referenceMap.put(key.gifUrlMd5, createReference(value));
    }

    @Override
    public void remove(GifRequest key) {
        referenceMap.remove(key.gifUrlMd5);
    }

    protected abstract Reference<byte[]> createReference(byte[] value);
}
