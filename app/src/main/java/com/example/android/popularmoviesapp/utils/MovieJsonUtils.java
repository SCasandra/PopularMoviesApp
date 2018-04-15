package com.example.android.popularmoviesapp.utils;

import android.content.Context;

import com.example.android.popularmoviesapp.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Casi on 4/14/2018.
 */

public final class MovieJsonUtils {

    public static List<Movie> getSimpleMovieStringsFromJson(Context context, String movieJsonStr)
            throws JSONException {

        if (movieJsonStr.isEmpty())
            return null;

        /* All movies are children of the "results" object */
        final String RESULTS = "results";
        final String TITLE = "title";
        final String RELEASE_DATE = "release_date";
        final String POSTER = "poster_path";
        final String VOTE_AVERAGE = "vote_average";
        final String PLOT_SYNOPSIS = "overview";

        /* Movie list to hold movies */
        List<Movie> movieData = null;

        JSONObject baseJsonResponse = new JSONObject(movieJsonStr);
        JSONArray response = baseJsonResponse.getJSONArray(RESULTS);
        // If there are results in the results array
        if (response.length() > 0) {
            movieData = new ArrayList<>();
            for (int i = 0; i < response.length(); i++) {
                JSONObject result = response.getJSONObject(i);
                String title = "";
                double voteAverage = 0;
                String poster = "";
                String releaseDate = "";
                String overview = "";

                if (result.has(TITLE)) {
                    title = result.getString(TITLE);
                }
                if (result.has(POSTER)) {
                    poster = result.getString(POSTER);
                }
                if (result.has(RELEASE_DATE)) {
                    releaseDate = result.getString(RELEASE_DATE);
                }
                if (result.has(PLOT_SYNOPSIS)) {
                    overview = result.getString(PLOT_SYNOPSIS);
                }
                if (result.has(VOTE_AVERAGE)) {
                    voteAverage = result.getDouble(VOTE_AVERAGE);
                }
                movieData.add(new Movie(title, releaseDate, poster, voteAverage, overview));
            }

        }

        return movieData;
    }

}
