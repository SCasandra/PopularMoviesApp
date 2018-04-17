package com.example.android.popularmoviesapp.persistence;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Casi on 4/16/2018.
 */

public class MovieContract {
    public static final String AUTHORITY = "com.example.android.popularmoviesapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIE = "movie"; // table name

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_TITILE = "title";
       // public static final String COLUMN_ID = "movie_id";
    }
}
