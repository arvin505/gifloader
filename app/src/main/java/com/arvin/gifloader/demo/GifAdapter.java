package com.arvin.gifloader.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.arvin.gifloader.core.GifLoader;
import com.arvin.gifloader.widget.GifView;

import java.util.List;

/**
 * Created by arvin on 2016/9/2.
 */
public class GifAdapter extends BaseAdapter {
    List<String> mDatas ;
    Context mContext;
    LayoutInflater mInflater;

    public GifAdapter(Context mContext, List<String> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.list_item,parent,false);
        }
        GifView gifView = (GifView) convertView.findViewById(R.id.gif_item);
        GifLoader.getInstance().displayGif(gifView,mDatas.get(position));
        return convertView;
    }
}
