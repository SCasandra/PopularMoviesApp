package com.example.android.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmoviesapp.utils.Constants;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView imageView = findViewById(R.id.movie_image);
        TextView overview = findViewById(R.id.overview_tv);
        TextView release = findViewById(R.id.release_date_tv);
        RatingBar ratingBar = findViewById(R.id.rating_bar);

        Intent i = getIntent();
        setTitle(i.getStringExtra(Constants.TITLE));
        overview.setText(i.getStringExtra(Constants.OVERVIEW));
        String release_date = getString(R.string.release_date) + "  " + i.getStringExtra(Constants.RELEASE_DATE);
        release.setText(release_date);
        ratingBar.setRating((float) i.getDoubleExtra(Constants.VOTE_AVG, 0));
        Picasso.with(this).load(Constants.BASE_IMG_URL + Constants.IMG_W780 + i.getStringExtra(Constants.POSTER)).into(imageView);
    }
}
