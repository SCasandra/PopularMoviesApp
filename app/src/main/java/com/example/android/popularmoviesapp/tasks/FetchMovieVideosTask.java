package com.example.android.popularmoviesapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.android.popularmoviesapp.interfaces.AsyncTaskCompleteListener;
import com.example.android.popularmoviesapp.model.Video;
import com.example.android.popularmoviesapp.utils.MovieJsonUtils;
import com.example.android.popularmoviesapp.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by Casi on 4/15/2018.
 */

public class FetchMovieVideosTask extends AsyncTask<Integer, Void, List<Video>> {
    private Context context;
    private AsyncTaskCompleteListener<List<Video>> listener;
    public static String VIDEO = "videos";

    public FetchMovieVideosTask(Context ctx, AsyncTaskCompleteListener<List<Video>> listener) {
        this.context = ctx;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<Video> videos) {
        listener.onTaskComplete(videos);
    }

    @Override
    protected List<Video> doInBackground(Integer... movie_id) {

        URL movieVideoKeyRequestUrl = NetworkUtils.buildUrl(new String[]{movie_id[0].toString(), VIDEO});
        try {
            String jsonVideoResponse = NetworkUtils
                    .getResponseFromHttpUrl(movieVideoKeyRequestUrl);
            return MovieJsonUtils.getMovieVideoKeyFromJson(jsonVideoResponse);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}