package com.arvin.gifloader.cache;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by arvin on 2016/9/2.
 */
public class WeakRefrenceCache extends ReferenceCache {
    @Override
    protected Reference<byte[]> createReference(byte[] value) {
        return new WeakReference<>(value);
    }
}
