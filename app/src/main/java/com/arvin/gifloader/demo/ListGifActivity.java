package com.arvin.gifloader.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.Arrays;

/**
 * Created by arvin on 2016/9/2.
 */
public class ListGifActivity extends Activity {
    private GifAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listView = (ListView) findViewById(R.id.list);
        adapter = new GifAdapter(this, Arrays.asList(Constants.urls));
        listView.setAdapter(adapter);
    }
}
