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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Casi on 4/15/2018.
 */


public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

    private Context context;
    private AsyncTaskCompleteListener<List<Movie>> listener;
    private List<Movie> movies;

    public FetchMoviesTask(Context ctx, AsyncTaskCompleteListener<List<Movie>> listener) {
        this.context = ctx;
        this.listener = listener;
        movies = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onTaskPreExecute();
    }

    @Override
    protected void onPostExecute(List<Movie> movieData) {
        listener.onTaskComplete(movieData);
    }

    @Override
    protected List<Movie> doInBackground(String... filters) {
        URL movieRequestUrl = NetworkUtils.buildUrl(filters[0], 0);

        try {
            String jsonMovieResponse = NetworkUtils
                    .getResponseFromHttpUrl(movieRequestUrl);
            List<Movie> simpleJsonMovieDataList = MovieJsonUtils
                    .getSimpleMovieListFromJson(jsonMovieResponse);
            if (simpleJsonMovieDataList == null)
                return null;
            for (int i = 0; i < simpleJsonMovieDataList.size(); i++) {
                new FetchMovieInfoTask(context, new FetchMyDataTaskCompleteListener()).execute(simpleJsonMovieDataList.get(i));
            }
            return simpleJsonMovieDataList;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return movies;
        }
    }

    private class FetchMyDataTaskCompleteListener implements AsyncTaskCompleteListener<Movie> {

        @Override
        public void onTaskComplete(Movie result) {
            movies.add(result);

        }

        @Override
        public void onTaskPreExecute() {

        }
    }
}