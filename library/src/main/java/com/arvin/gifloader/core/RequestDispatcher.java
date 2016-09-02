package com.arvin.gifloader.core;

import android.util.Log;

import com.arvin.gifloader.loader.Loader;
import com.arvin.gifloader.loader.LoaderManager;
import com.arvin.gifloader.request.GifRequest;

import java.security.interfaces.ECPrivateKey;
import java.util.concurrent.BlockingQueue;

/**
 * Created by arvin on 2016/9/2.
 * 网络请求Dispatcher,继承自Thread,从网络请求队列中循环读取请求并且执行
 */
final class RequestDispatcher extends Thread {
    /**
     * 网络请求队列
     */
    private BlockingQueue<GifRequest> mRequestQueue;

    /**
     * @param queue
     */
    public RequestDispatcher(BlockingQueue<GifRequest> queue) {
        mRequestQueue = queue;
    }

    @Override
    public void run() {
        try {
            while (!this.isInterrupted()) {
                final GifRequest request = mRequestQueue.take();
                if (request.isCancel) {
                    continue;
                }

                final String schema = parseSchema(request.gifUrl);
                Loader imageLoader = LoaderManager.getInstance().getLoader(schema);
                imageLoader.loadGif(request);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String parseSchema(String uri) {
        if (uri.contains("://")) {
            return uri.split("://")[0];
        } else {
            Log.e(getName(), "### wrong scheme, image uri is : " + uri);
        }

        return "";
    }

}
