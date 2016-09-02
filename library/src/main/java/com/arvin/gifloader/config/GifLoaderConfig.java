package com.arvin.gifloader.config;

import com.arvin.gifloader.cache.GifCache;
import com.arvin.gifloader.cache.MemoryCache;
import com.arvin.gifloader.policy.LoadPolicy;
import com.arvin.gifloader.policy.SerialPolicy;

/**
 * Created by arvin on 2016/9/2.
 */
public class GifLoaderConfig {

    /**
     * 缓存对象
     */
    public GifCache gifCache = new MemoryCache();


    /**
     * 加载时的loading和加载失败的配置对象
     */
    public DisplayConfig displayConfig = new DisplayConfig();
    /**
     * 加载策略
     */
    public LoadPolicy loadPolicy = new SerialPolicy();

    /**
     *
     */
    public int threadCount = Runtime.getRuntime().availableProcessors() + 1;

    /**
     * @param count
     * @return
     */
    public GifLoaderConfig setThreadCount(int count) {
        threadCount = Math.max(1, count);
        return this;
    }

    public GifLoaderConfig setCache(GifCache cache) {
        gifCache = cache;
        return this;
    }

    private GifLoaderConfig setLoadingPlaceholder(int resId) {
        displayConfig.loadingResId = resId;
        return this;
    }


    private GifLoaderConfig setNotFoundPlaceholder(int resId) {
        displayConfig.failedResId = resId;
        return this;
    }

    public GifLoaderConfig setLoadPolicy(LoadPolicy policy) {
        if (policy != null) {
            loadPolicy = policy;
        }
        return this;
    }
}
