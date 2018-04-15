package com.example.android.popularmoviesapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.popularmoviesapp.utils.Constants;
import com.example.android.popularmoviesapp.utils.MovieJsonUtils;
import com.example.android.popularmoviesapp.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;
    private GridView mGridView;
    private String filter_type = NetworkUtils.TOP_RATED_FILTER;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorMessageDisplay = findViewById(R.id.errorTextView);
        mLoadingIndicator = findViewById(R.id.progressBar);
        mGridView = findViewById(R.id.gridView);
        imageAdapter = new ImageAdapter(this);
        mGridView.setAdapter(imageAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie m = (Movie) adapterView.getItemAtPosition(i);
                showDetailActivity(m);
            }
        });

        Spinner spinner = findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.filter_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).toString().equals("top rated")) {
                    filter_type = NetworkUtils.TOP_RATED_FILTER;
                } else {
                    filter_type = NetworkUtils.POPULAR_FILTER;
                }
                if (NetworkUtils.isOnline(getApplicationContext())) {
                    FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
                    fetchMoviesTask.execute();
                } else {
                    showErrorMessage(getString(R.string.no_net_error));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void showDetailActivity(Movie m) {
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra(Constants.TITLE, m.getTitle());
        i.putExtra(Constants.OVERVIEW, m.getOverview());
        i.putExtra(Constants.POSTER, m.getPoster());
        i.putExtra(Constants.RELEASE_DATE, m.getReleaseDate());
        i.putExtra(Constants.VOTE_AVG, m.getVoteAverage());
        startActivity(i);
    }

    /**
     * This method will make the View for the movie data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check movie each view is currently visible or gone.
     */
    private void showMovieDataView() {
        /* First, make sure the error is Gone */
        mErrorMessageDisplay.setVisibility(View.GONE);
        /* Then, make sure the movie data is visible */
        mGridView.setVisibility(View.VISIBLE);

    }

    /**
     * This method will make the error message visible and hide the movie
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check movie each view is currently visible or gone.
     */
    private void showErrorMessage(String msj) {
        /* First, hide the currently visible data */
        mGridView.setVisibility(View.GONE);
        /* Then, show the error */
        mErrorMessageDisplay.setText(msj);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private class FetchMoviesTask extends AsyncTask<Void, Void, List<Movie>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<Movie> movieData) {

            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData != null) {
                showMovieDataView();
                imageAdapter.setMovieData(movieData);
            } else {
                showErrorMessage(getString(R.string.no_data_error));
            }
        }

        @Override
        protected List<Movie> doInBackground(Void... voids) {
            URL movieRequestUrl = NetworkUtils.buildUrl(filter_type);

            try {
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);
                List<Movie> simpleJsonMovieDataList = MovieJsonUtils
                        .getSimpleMovieStringsFromJson(MainActivity.this, jsonMovieResponse);

                return simpleJsonMovieDataList;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
