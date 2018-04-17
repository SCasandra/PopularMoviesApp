package com.example.android.popularmoviesapp.utils;

import com.example.android.popularmoviesapp.model.Movie;
import com.example.android.popularmoviesapp.model.Review;
import com.example.android.popularmoviesapp.model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Casi on 4/14/2018.
 */

public final class MovieJsonUtils {

    public static List<Review> getMovieReviewKeyFromJson(String reviewJsonStr) throws JSONException {
        if (reviewJsonStr.isEmpty())
            return null;

        final String RESULTS = "results";
        final String ID = "id";
        final String CONTENT = "content";
        final String AUTHOR = "author";
        String author;
        String content;
        int id = 0;
        List<Review> reviews = null;
        JSONObject baseJsonResponse = new JSONObject(reviewJsonStr);
        if (baseJsonResponse.has(ID)) {
            id = baseJsonResponse.getInt(ID);
        }
        if (baseJsonResponse.has(RESULTS)) {
            JSONArray response = baseJsonResponse.getJSONArray(RESULTS);
            if (response.length() > 0) {
                reviews = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    JSONObject result = response.getJSONObject(i);
                    author = result.getString(AUTHOR);
                    content = result.getString(CONTENT);
                    reviews.add(new Review(author, content, id));
                }
            }
        }
        return reviews;
    }

    public static List<Video> getMovieVideoKeyFromJson(String videoJsonStr) throws JSONException {
        if (videoJsonStr.isEmpty())
            return null;

        final String RESULTS = "results";
        final String ID = "id";
        final String KEY = "key";
        List<Video> videos = null;
        int id = 0;

        JSONObject baseJsonResponse = new JSONObject(videoJsonStr);
        if (baseJsonResponse.has(ID)) {
            id = baseJsonResponse.getInt(ID);
        }
        if (baseJsonResponse.has(RESULTS)) {
            JSONArray response = baseJsonResponse.getJSONArray(RESULTS);
            if (response.length() > 0) {
                videos = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    JSONObject result = response.getJSONObject(i);
                    videos.add(new Video(id, result.getString(KEY)));
                }
            }
        }
        return videos;
    }

    public static List<Movie> getSimpleMovieListFromJson(String movieJsonStr)
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
        final String ID = "id";

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
                int id = 0;

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
                if (result.has(ID)) {
                    id = result.getInt(ID);
                }
                movieData.add(new Movie(title, releaseDate, poster, voteAverage, overview, id));
            }

        }

        return movieData;
    }

}
