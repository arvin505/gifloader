package com.arvin.gifloader.core;

import android.graphics.Movie;
import android.util.Log;

import com.arvin.gifloader.cache.GifCache;
import com.arvin.gifloader.cache.MemoryCache;
import com.arvin.gifloader.config.DisplayConfig;
import com.arvin.gifloader.config.GifLoaderConfig;
import com.arvin.gifloader.policy.SerialPolicy;
import com.arvin.gifloader.request.GifRequest;
import com.arvin.gifloader.widget.GifView;

/**
 * Created by arvin on 2016/9/2.
 * gif加载类，根据传入url显示gif图像
 */
public final class GifLoader {

    private GifLoader() {
    }

    private static volatile GifLoader sInstance;

    public static GifLoader getInstance() {
        Log.e("TAG", "--------instance------------");
       if (sInstance==null){
           synchronized (GifLoader.class){
               if (sInstance ==null){
                   sInstance = new GifLoader();
               }
           }
       }
        Log.e("TAG", "--------instance---return---------" + sInstance.hashCode());
        return sInstance;
    }


    private volatile GifCache mGifCache = new MemoryCache();

    public GifLoaderConfig getConfig() {
        return mConfig;
    }

    private RequestQueue mGifQueue;

    private GifLoaderConfig mConfig;


    public void init(GifLoaderConfig config) {
        mConfig = config;
        mGifCache = config.gifCache;
        checkConfig();
        mGifQueue = new RequestQueue();
        mGifQueue.start();
    }

    private void checkConfig() {
        if (mConfig == null) {
            throw new RuntimeException(
                    "The config of SimpleImageLoader is Null, please call the init(ImageLoaderConfig config) method to initialize");
        }

        if (mConfig.loadPolicy == null) {
            mConfig.loadPolicy = new SerialPolicy();
        }
        if (mGifCache == null) {
            mGifCache = new MemoryCache();
        }
    }

    public void displayGif(GifView gifView, String uri) {
        displayGif(gifView, uri, null, null);
    }

    public void displayGif(GifView gifView, String uri, DisplayConfig config) {
        displayGif(gifView, uri, config, null);
    }

    public void displayGif(GifView gifView, String uri, GifListener listener) {
        displayGif(gifView, uri, null, listener);
    }

    public void displayGif(final GifView gifView, final String uri,
                           final DisplayConfig config, final GifListener listener) {
        GifRequest request = new GifRequest(gifView, config, uri, listener);
        // 加载的配置对象,如果没有设置则使用ImageLoader的配置
        request.displayConfig = request.displayConfig != null ? request.displayConfig
                : mConfig.displayConfig;
        // 添加对队列中
        mGifQueue.addRequest(request);
    }

    public void stop() {
        mGifQueue.stop();
    }


    /**
     * gif加载Listener
     *
     * @author xiaoyi
     */
    public static interface GifListener {
        public void onComplete(GifView gifView, Movie movie, String uri);
    }
}
