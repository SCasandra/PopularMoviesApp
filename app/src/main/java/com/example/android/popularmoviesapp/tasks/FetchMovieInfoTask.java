package com.example.android.popularmoviesapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.android.popularmoviesapp.interfaces.AsyncTaskCompleteListener;
import com.example.android.popularmoviesapp.model.Movie;
import com.example.android.popularmoviesapp.utils.MovieJsonUtils;
import com.example.android.popularmoviesapp.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Casi on 4/15/2018.
 */

public class FetchMovieInfoTask extends AsyncTask<Movie, Void, Movie> {
    private Context context;
    private AsyncTaskCompleteListener<Movie> listener;

    public FetchMovieInfoTask(Context ctx, AsyncTaskCompleteListener<Movie> listener) {
        this.context = ctx;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Movie m) {
        listener.onTaskComplete(m);
    }

    @Override
    protected Movie doInBackground(Movie... movies) {

        URL movieVideoKeyRequestUrl = NetworkUtils.buildUrl(String.valueOf(movies[0].getId()), 1);
        URL movieReviewKeyRequestUrl = NetworkUtils.buildUrl(String.valueOf(movies[0].getId()), 2);

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