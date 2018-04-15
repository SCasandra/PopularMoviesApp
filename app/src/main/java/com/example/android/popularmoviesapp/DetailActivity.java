package com.example.android.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmoviesapp.model.Movie;
import com.example.android.popularmoviesapp.utils.Constants;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView imageView = findViewById(R.id.movie_image);
        TextView overview = findViewById(R.id.overview_tv);
        TextView release = findViewById(R.id.release_date_tv);
        RatingBar ratingBar = findViewById(R.id.rating_bar);

        Movie movie = getIntent().getParcelableExtra(Constants.MOVIE);
        id = movie.getId();
        setTitle(movie.getTitle());
        String release_date = getString(R.string.release_date) + "  " + movie.getReleaseDate();
        release.setText(release_date);
        ratingBar.setRating((float) movie.getVoteAverage());
        overview.setText(movie.getOverview());
        Picasso.with(this).load(Constants.BASE_IMG_URL + Constants.IMG_W500 + movie.getPoster()).into(imageView);
    }

    public void showTrailer(View view) {
        Intent i = new Intent(this, TrailerActivity.class);
        i.putExtra(Constants.TITLE, getTitle());
        startActivity(i);
    }

    public void showReviews(View view) {
        Intent i = new Intent(this, ReviewActivity.class);
        i.putExtra(Constants.TITLE, getTitle());
        startActivity(i);
    }
}
