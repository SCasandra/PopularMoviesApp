package com.example.android.popularmoviesapp.adapters;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.android.popularmoviesapp.model.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends BaseAdapter {

    private static final Object YOUTUBE_URL = "http://img.youtube.com/vi/";
    private Context mContext;
    private List<Video> videos;

    public VideoAdapter(Context c) {
        mContext = c;
        videos = new ArrayList<>();
    }


    public void setVideoData(List<Video> videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return videos.size();
    }

    @Override
    public Object getItem(int i) {
        return videos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView;
        if (view == null) {
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) view;
        }

        Video v = (Video) getItem(i);
        Picasso.with(mContext).load(YOUTUBE_URL + v.getKey() + "/0.jpg").into(imageView);
        return imageView;
    }
}
