package com.example.android.popularmoviesapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmoviesapp.model.Movie;
import com.example.android.popularmoviesapp.persistence.MovieContract;
import com.example.android.popularmoviesapp.utils.Constants;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private Movie movie;
    private boolean ok = false;
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView imageView = findViewById(R.id.movie_image);
        TextView overview = findViewById(R.id.overview_tv);
        TextView release = findViewById(R.id.release_date_tv);
        RatingBar ratingBar = findViewById(R.id.rating_bar);
        imageButton = findViewById(R.id.image_btn);

        movie = getIntent().getParcelableExtra(Constants.MOVIE);
        setTitle(movie.getTitle());
        String release_date = getString(R.string.release_date) + "  " + movie.getReleaseDate();
        release.setText(release_date);
        ratingBar.setRating((float) movie.getVoteAverage());
        overview.setText(movie.getOverview());
        Picasso.with(this).load(Constants.BASE_IMG_URL + Constants.IMG_W500 + movie.getPoster()).into(imageView);
        init();
    }

    private void init() {
        if (findMovieByTitle(movie.getTitle()) != null) {
            ok = true;
            imageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_black_36dp));
        }
    }

    public void showTrailer(View view) {
        Intent i = new Intent(this, TrailerActivity.class);
        i.putExtra(Constants.TITLE, getTitle());
        i.putExtra(Constants.VIDEO_KEY, movie.getVideo_key());
        startActivity(i);
    }

    public void showReviews(View view) {
        Intent intent = new Intent(this, ReviewActivity.class);
        intent.putExtra(Constants.MOVIE, movie);
        startActivity(intent);
    }


    public void setAsFavorite(View view) {
        ok = !ok;
        imageButton.setImageDrawable(!ok ? ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_black_36dp) : ContextCompat.getDrawable(this, R.drawable.ic_favorite_black_36dp));
        if (ok) {
            if (findMovieByTitle(movie.getTitle()) == null) {
                ContentValues values = new ContentValues();
                values.put(MovieContract.MovieEntry.COLUMN_TITILE, movie.getTitle());
                getContentResolver().insert(MovieContract.CONTENT_URI, values);
            }
        } else {
            deleteMovieWithTitle(movie.getTitle());
        }

    }

    public Movie findMovieByTitle(String title) {
        String[] projection = {MovieContract.MovieEntry._ID,
                MovieContract.MovieEntry.COLUMN_TITILE};

        String selection = "title = \"" + title + "\"";

        Cursor cursor = getContentResolver().query(MovieContract.CONTENT_URI,
                projection, selection, null,
                null);

        Movie movie = null;

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            movie = new Movie(cursor.getString(0));
        }
        return movie;
    }

    public void deleteMovieWithTitle(String title) {
        Uri uri = MovieContract.CONTENT_URI;
        uri = uri.buildUpon().appendPath(title).build();
        getContentResolver().delete(uri,
                null, null);
    }

}
