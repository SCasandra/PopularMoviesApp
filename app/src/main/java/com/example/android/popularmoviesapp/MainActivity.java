package com.example.android.popularmoviesapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.popularmoviesapp.adapters.ImageAdapter;
import com.example.android.popularmoviesapp.interfaces.AsyncTaskCompleteListener;
import com.example.android.popularmoviesapp.model.Movie;
import com.example.android.popularmoviesapp.model.Review;
import com.example.android.popularmoviesapp.model.Video;
import com.example.android.popularmoviesapp.persistence.MovieContract;
import com.example.android.popularmoviesapp.tasks.FetchMovieReviewsTask;
import com.example.android.popularmoviesapp.tasks.FetchMovieVideosTask;
import com.example.android.popularmoviesapp.tasks.FetchMoviesTask;
import com.example.android.popularmoviesapp.utils.Constants;
import com.example.android.popularmoviesapp.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    public static final int TASK_LOADER_ID = 0;
    private static final String SCROLL_POSITION = "scroll_position";
    private static final String FILTER_TYPE_STATE = "filter_type_state";
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;
    private GridView mGridView;
    private String filter_type = NetworkUtils.TOP_RATED_FILTER;
    private static ImageAdapter imageAdapter;
    private List<Movie> topRatedMovies;
    private List<Movie> mostPopularMovies;
    private ArrayList<Video> videos;
    private ArrayList<Review> reviews;
    private final LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = this;
    private int mListInstanceState = 0;


    @Override
    protected void onResume() {
        super.onResume();
        if (filter_type.equals(NetworkUtils.FAVORITES_FILTER))
            getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            mListInstanceState = savedInstanceState.getInt(SCROLL_POSITION);
            filter_type = savedInstanceState.getString(FILTER_TYPE_STATE);
        }

        topRatedMovies = new ArrayList<>();
        mostPopularMovies = new ArrayList<>();
        videos = new ArrayList<>();
        reviews = new ArrayList<>();

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
        mGridView.smoothScrollToPosition(mListInstanceState);
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
                setFilterType(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (NetworkUtils.isOnline(getApplicationContext())) {
            new FetchMoviesTask(getApplicationContext(), new FetchMyMoviesTaskCompleteListener()).execute(NetworkUtils.POPULAR_FILTER);
            new FetchMoviesTask(getApplicationContext(), new FetchMyMoviesTaskCompleteListener()).execute(NetworkUtils.TOP_RATED_FILTER);
            for (int i = 0; i < mostPopularMovies.size(); i++) {
                new FetchMovieVideosTask(getApplicationContext(), new FetchMyVideosTaskCompleteListener()).execute(mostPopularMovies.get(i).getId());
                new FetchMovieReviewsTask(this, new FetchMyReviewsTaskCompleteListener()).execute(mostPopularMovies.get(i).getId());
            }
            for (int i = 0; i < topRatedMovies.size(); i++) {
                new FetchMovieVideosTask(getApplicationContext(), new FetchMyVideosTaskCompleteListener()).execute(topRatedMovies.get(i).getId());
                new FetchMovieReviewsTask(this, new FetchMyReviewsTaskCompleteListener()).execute(topRatedMovies.get(i).getId());
            }
        } else {
            showErrorMessage(getString(R.string.no_net_error));
        }
           /*
         Ensure a loader is initialized and active. If the loader doesn't already exist, one is
         created, otherwise the last created loader is re-used.
         */
        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SCROLL_POSITION, mGridView.getFirstVisiblePosition());
        outState.putString(FILTER_TYPE_STATE, filter_type);
        super.onSaveInstanceState(outState);
    }

    private void showDetailActivity(Movie m) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constants.MOVIE, m);
        ArrayList<Video> v = new ArrayList<>();
        for (int i = 0; i < videos.size(); i++) {
            if (m.getId() == videos.get(i).getMovie_id()) {
                v.add(videos.get(i));
            }
        }
        intent.putParcelableArrayListExtra(Constants.REVIEW, reviews);
        intent.putParcelableArrayListExtra(Constants.VIDEO, v);
        startActivity(intent);
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

    public void setFilterType(String filterType) {
        switch (filterType) {
            case "most popular":
                this.filter_type = NetworkUtils.POPULAR_FILTER;
                imageAdapter.setMovieData(mostPopularMovies);
                break;
            case "favorites":
                this.filter_type = NetworkUtils.FAVORITES_FILTER;
                getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, loaderCallbacks);
                break;
            default:
                this.filter_type = NetworkUtils.TOP_RATED_FILTER;
                imageAdapter.setMovieData(topRatedMovies);
        }
    }

    public class FetchMyMoviesTaskCompleteListener implements AsyncTaskCompleteListener<List<Movie>> {

        @Override
        public void onTaskComplete(List<Movie> movieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData != null) {
                showMovieDataView();
                imageAdapter.setMovieData(movieData);
                if (filter_type.equals("top rated")) {
                    topRatedMovies = movieData;
                    if (NetworkUtils.isOnline(getApplicationContext())) {
                        for (int i = 0; i < topRatedMovies.size(); i++) {
                            new FetchMovieVideosTask(getApplication(), new FetchMyVideosTaskCompleteListener()).execute(topRatedMovies.get(i).getId());
                            new FetchMovieReviewsTask(getApplication(), new FetchMyReviewsTaskCompleteListener()).execute(topRatedMovies.get(i).getId());
                        }
                    } else {
                        showErrorMessage(getString(R.string.no_net_error));
                    }
                } else {
                    mostPopularMovies = movieData;
                    if (NetworkUtils.isOnline(getApplicationContext())) {
                        for (int i = 0; i < mostPopularMovies.size(); i++) {
                            new FetchMovieVideosTask(getApplication(), new FetchMyVideosTaskCompleteListener()).execute(mostPopularMovies.get(i).getId());
                            new FetchMovieReviewsTask(getApplication(), new FetchMyReviewsTaskCompleteListener()).execute(mostPopularMovies.get(i).getId());
                        }
                    } else {
                        showErrorMessage(getString(R.string.no_net_error));
                    }
                }
            } else {
                showErrorMessage(getString(R.string.no_data_error));
            }
            filter_type = "top rated";
        }

        @Override
        public void onTaskPreExecute() {
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }
    }

    public class FetchMyVideosTaskCompleteListener implements AsyncTaskCompleteListener<List<Video>> {

        @Override
        public void onTaskComplete(List<Video> videoData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (videoData != null) {
                videos.addAll(videoData);
            }
        }

        @Override
        public void onTaskPreExecute() {
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }
    }

    public class FetchMyReviewsTaskCompleteListener implements AsyncTaskCompleteListener<List<Review>> {

        @Override
        public void onTaskComplete(List<Review> reviewData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (reviewData != null) {
                reviews.addAll(reviewData);
            }
            mGridView.smoothScrollToPosition(mListInstanceState);
        }

        @Override
        public void onTaskPreExecute() {
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }
    }


    /**
     * Instantiates and returns a new AsyncTaskLoader with the given ID.
     * This loader will return task data as a Cursor or null if an error occurs.
     * <p>
     * Implements the required callbacks to take care of loading data at all stages of loading.
     */
    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the movies data
            Cursor mMovieData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mMovieData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data

                // Query and load all favorite movies in the background;
                // [Hint] use a try/catch block to catch any errors in loading data

                try {
                    return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

                } catch (Exception e) {
                    Log.e("loader", "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };

    }


    /**
     * Called when a previously created loader has finished its load.
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        String[] t = new String[100];
        List<Movie> favMovies = new ArrayList<>();
        if (data != null) {
            int i = 0;
            while (data.moveToNext()) {
                int titleIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITILE);
                t[i] = data.getString(titleIndex);
                Log.d("fav", t[i]);
                for (int j = 0; j < mostPopularMovies.size(); j++) {
                    if (mostPopularMovies.get(j).getTitle().equals(t[i])) {
                        favMovies.add(mostPopularMovies.get(j));
                    }
                }
                for (int j = 0; j < topRatedMovies.size(); j++) {
                    if (topRatedMovies.get(j).getTitle().equals(t[i])) {
                        favMovies.add(topRatedMovies.get(j));
                    }
                }
            }
            showMovieDataView();
            imageAdapter.setMovieData(favMovies);
        } else {
            showErrorMessage(getString(R.string.no_data_error));
        }
    }


    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.
     * onLoaderReset removes any references this activity had to the loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
