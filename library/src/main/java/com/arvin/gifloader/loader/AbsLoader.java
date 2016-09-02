/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 bboyfeiyu@gmail.com ( Mr.Simple )
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.arvin.gifloader.loader;

import android.graphics.Bitmap;
import android.graphics.Movie;
import android.util.Log;

import com.arvin.gifloader.cache.GifCache;
import com.arvin.gifloader.config.DisplayConfig;
import com.arvin.gifloader.core.GifLoader;
import com.arvin.gifloader.request.GifRequest;
import com.arvin.gifloader.widget.GifView;


/**
 * @author xiaoyi
 */
public abstract class AbsLoader implements Loader {

    /**
     * 缓存
     */
    private static GifCache mCache = GifLoader.getInstance().getConfig().gifCache;

    @Override
    public final void loadGif(GifRequest request) {
        Movie movie = mCache.get(request);
        Log.e("TAG", "### 是否有缓存 : " + movie + ", uri = " + request.gifUrl);
        if (movie == null) {
            showLoading(request);
            byte[] bytes = onLoadGif(request);
            movie = Movie.decodeByteArray(bytes,0,bytes.length);
            cacheBitmap(request, bytes);
            Log.e("","-----e   null");
        } else {
            request.justCacheInMem = true;
           Log.e("","----e not null");
        }

        deliveryToUIThread(request, movie);
    }

    /**
     * @param result
     * @return
     */
    protected abstract byte[] onLoadGif(GifRequest result);

    /**
     * @param request
     * @param value
     */
    private void cacheBitmap(GifRequest request, byte[] value) {
        // 缓存新的图片
        if (value != null && mCache != null) {
            synchronized (mCache) {
                mCache.put(request, value);
            }
        }
    }

    /**
     * 显示加载中的视图,注意这里也要判断imageview的tag与image uri的相等性,否则逆序加载时出现问题
     * 
     * @param request
     */
    protected void showLoading(final GifRequest request) {
        final GifView gifView = request.getGifView();
        if (request.isGifViewTagValid()
                && hasLoadingPlaceholder(request.displayConfig)) {
            gifView.post(new Runnable() {

                @Override
                public void run() {
                    gifView.setImageResource(request.displayConfig.loadingResId);
                }
            });
        }
    }

    /**
     * 将结果投递到UI,更新ImageView
     * 
     * @param request
     * @param movie
     */
    protected void deliveryToUIThread(final GifRequest request,
            final Movie movie) {
        final GifView gifView = request.getGifView();
        if (gifView == null) {
            return;
        }
        gifView.post(new Runnable() {

            @Override
            public void run() {
                updateImageView(request, movie);
            }
        });
    }

    /**
     * 更新ImageView
     * 
     * @param request
     * @param result
     */
    private void updateImageView(GifRequest request, Movie result) {
        final GifView gifView = request.getGifView();
        final String uri = request.gifUrl;
        if (result != null && gifView.getTag().equals(uri)) {
            gifView.setMovie(result);
        }

        // 加载失败
        if (result == null && hasFaildPlaceholder(request.displayConfig)) {
            gifView.setImageResource(request.displayConfig.failedResId);
        }

        // 回调接口
        // FIXME: 2016/9/2
        if (request.mGifLister != null) {
            request.mGifLister.onComplete(gifView, result, uri);
        }
    }

    private boolean hasLoadingPlaceholder(DisplayConfig displayConfig) {
        return displayConfig != null && displayConfig.loadingResId > 0;
    }

    private boolean hasFaildPlaceholder(DisplayConfig displayConfig) {
        return displayConfig != null && displayConfig.failedResId > 0;
    }

}
