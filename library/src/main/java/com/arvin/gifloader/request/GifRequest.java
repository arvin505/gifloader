package com.arvin.gifloader.request;

import android.graphics.Movie;
import android.location.GpsStatus;

import com.arvin.gifloader.config.DisplayConfig;
import com.arvin.gifloader.core.GifLoader;
import com.arvin.gifloader.policy.LoadPolicy;
import com.arvin.gifloader.utils.GifViewHelper;
import com.arvin.gifloader.utils.MD5Helper;
import com.arvin.gifloader.widget.GifView;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by arvin on 2016/9/2.
 */
public class GifRequest implements Comparable<GifRequest> {
    private Reference<GifView> mGifViewRef;
    public DisplayConfig displayConfig;
    public String gifUrl = "";
    public String gifUrlMd5 = "";
    /**
     * 请求序列号
     */
    public int serialNum = 0;
    /**
     * 是否取消该请求
     */
    public boolean isCancel = false;

    /**
     *
     */
    public boolean justCacheInMem = false;

    /**
     * 加载策略
     */
    LoadPolicy mLoadPolicy = GifLoader.getInstance().getConfig().loadPolicy;
    public GifLoader.GifListener mGifLister;

    public GifRequest(GifView gifview, DisplayConfig displayConfig, String gifUrl, GifLoader.GifListener listener) {
        this.mGifViewRef = new WeakReference<GifView>(gifview);
        this.displayConfig = displayConfig;
        this.gifUrl = gifUrl;
        gifview.setTag(gifUrl);
        gifUrlMd5 = MD5Helper.generateMD5(gifUrl);
        this.mGifLister = listener;
    }

    /**
     * @param policy
     */
    public void setLoadPolicy(LoadPolicy policy) {
        if (policy != null) {
            mLoadPolicy = policy;
        }
    }

    /**
     * 判断imageview的tag与uri是否相等
     *
     * @return
     */
    public boolean isGifViewTagValid() {
        return mGifViewRef.get() != null ? mGifViewRef.get().getTag().equals(gifUrl) : false;
    }

    public GifView getGifView() {
        return mGifViewRef.get();
    }

    public int getGifViewWidth() {
        return GifViewHelper.getImageViewWidth(mGifViewRef.get());
    }

    public int getGifViewHeight() {
        return GifViewHelper.getImageViewHeight(mGifViewRef.get());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((gifUrl == null) ? 0 : gifUrl.hashCode());
        result = prime * result + ((mGifViewRef == null) ? 0 : mGifViewRef.get().hashCode());
        result = prime * result + serialNum;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GifRequest other = (GifRequest) obj;
        if (gifUrl == null) {
            if (other.gifUrl != null)
                return false;
        } else if (!gifUrl.equals(other.gifUrl))
            return false;
        if (mGifViewRef == null) {
            if (other.mGifViewRef != null)
                return false;
        } else if (!mGifViewRef.get().equals(other.mGifViewRef.get()))
            return false;
        if (serialNum != other.serialNum)
            return false;
        return true;
    }

    @Override
    public int compareTo(GifRequest another) {
        return mLoadPolicy.compare(this, another);
    }


}
