package com.example.android.popularmoviesapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.android.popularmoviesapp.interfaces.AsyncTaskCompleteListener;
import com.example.android.popularmoviesapp.model.Review;
import com.example.android.popularmoviesapp.utils.MovieJsonUtils;
import com.example.android.popularmoviesapp.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by Casi on 4/17/2018.
 */

public class FetchMovieReviewsTask extends AsyncTask<Integer, Void, List<Review>> {
    private Context context;
    private AsyncTaskCompleteListener<List<Review>> listener;
    private static String REVIEWS = "reviews";

    public FetchMovieReviewsTask(Context ctx, AsyncTaskCompleteListener<List<Review>> listener) {
        this.context = ctx;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<Review> reviews) {
        listener.onTaskComplete(reviews);
    }

    @Override
    protected List<Review> doInBackground(Integer... movie_id) {

        URL movieReviewKeyRequestUrl = NetworkUtils.buildUrl(new String[]{movie_id[0].toString(), REVIEWS});

        try {
            String jsonReviewResponse = NetworkUtils
                    .getResponseFromHttpUrl(movieReviewKeyRequestUrl);
            return MovieJsonUtils.getMovieReviewKeyFromJson(jsonReviewResponse);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
