package com.arvin.gifloader.loader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by arvin on 2016/9/2.
 */
public class LoaderManager {
    public static final String HTTP = "http";
    public static final String HTTPS = "https";
    public static final String FILE = "file";

    /**
     *
     */
    private Map<String, Loader> mLoaderMap = new HashMap<String, Loader>();

    private Loader mNullLoader = new NullLoader();
    /**
     *
     */
    private static LoaderManager INSTANCE;

    /**
     *
     */
    private LoaderManager() {
        register(HTTP, new UrlLoader());
        register(HTTPS, new UrlLoader());
        register(FILE, new LocalLoader());
    }

    /**
     * @return
     */
    public static LoaderManager getInstance() {
        if (INSTANCE == null) {
            synchronized (LoaderManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LoaderManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * @param schema
     * @param loader
     */
    public final synchronized void register(String schema, Loader loader) {
        mLoaderMap.put(schema, loader);
    }

    public Loader getLoader(String schema) {
        if (mLoaderMap.containsKey(schema)) {
            return mLoaderMap.get(schema);
        }
        return mNullLoader;
    }

}
