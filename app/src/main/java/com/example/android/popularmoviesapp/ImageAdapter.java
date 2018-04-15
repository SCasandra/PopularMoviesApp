package com.example.android.popularmoviesapp;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.android.popularmoviesapp.model.Movie;
import com.example.android.popularmoviesapp.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private List<Movie> movies;

    public ImageAdapter(Context c) {
        mContext = c;
        movies = new ArrayList<>();
    }


    public void setMovieData(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int i) {
        return movies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView;
        if (view == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            //  imageView.setLayoutParams(new ViewGroup.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //  imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) view;
        }

        // imageView.setImageResource(R.drawable.success);
        Movie m = (Movie) getItem(i);
        Picasso.with(mContext).load(Constants.BASE_IMG_URL + Constants.IMG_W500 + m.getPoster()).into(imageView);
        return imageView;
    }
}
