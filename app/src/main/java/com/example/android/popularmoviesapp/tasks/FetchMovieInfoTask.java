package com.example.android.popularmoviesapp.tasks;

import android.os.AsyncTask;

import com.example.android.popularmoviesapp.model.Movie;
import com.example.android.popularmoviesapp.utils.MovieJsonUtils;
import com.example.android.popularmoviesapp.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Casi on 4/15/2018.
 */

public class FetchMovieInfoTask extends AsyncTask<Movie, Void, Void> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void v) {

    }

    @Override
    protected Void doInBackground(Movie... movies) {
        URL movieVideoKeyRequestUrl = NetworkUtils.buildUrl(movies[0].getId() + NetworkUtils.VIDEO);
        URL movieReviewKeyRequestUrl = NetworkUtils.buildUrl(movies[0].getId() + NetworkUtils.REWIEWS);

        try {
            String jsonVideoResponse = NetworkUtils
                    .getResponseFromHttpUrl(movieVideoKeyRequestUrl);
            String jsonReviewResponse = NetworkUtils
                    .getResponseFromHttpUrl(movieReviewKeyRequestUrl);
            movies[0].setVideo_key(MovieJsonUtils.getMovieVideoKeyFromJson(jsonVideoResponse));
            movies[0].setReview(MovieJsonUtils.getMovieReviewKeyFromJson(jsonReviewResponse));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}