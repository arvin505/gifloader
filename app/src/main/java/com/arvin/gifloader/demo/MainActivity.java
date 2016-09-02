package com.arvin.gifloader.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.arvin.gifloader.cache.DiskCache;
import com.arvin.gifloader.cache.MemoryCache;
import com.arvin.gifloader.cache.WeakRefrenceCache;
import com.arvin.gifloader.config.GifLoaderConfig;
import com.arvin.gifloader.core.GifLoader;
import com.arvin.gifloader.widget.GifView;

public class MainActivity extends AppCompatActivity {
    GifView gifView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gifView = (GifView) findViewById(R.id.gif);

        //gifView.setMovieResource(R.raw.test);


        GifLoaderConfig config = new GifLoaderConfig();
        config.setCache(DiskCache.getDiskCache(this));
        config.setLoadingPlaceholder(R.mipmap.ic_launcher)
                .setNotFoundPlaceholder(R.mipmap.ic_launcher)
                .setThreadCount(5)
                ;
        GifLoader.getInstance().init(config);


    }

    public void click(View view) {
        Log.e("TAG","-------instance--onclick---- ");
        GifLoader.getInstance().displayGif(gifView,"http://f.hiphotos.baidu.com/zhidao/wh%3D600%2C800/sign=ca2fa5088882b9013df8cb3543bd854f/71cf3bc79f3df8dcd4d301becf11728b47102836.jpg");
        Log.e("TAG","-------instance--onclick- after--- ");
    }
}
