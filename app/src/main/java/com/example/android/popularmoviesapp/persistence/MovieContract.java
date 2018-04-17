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
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_VOTE_AVG = "vote_average";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_ID_REVIEW = "id_review";
    }

    public static final class ReviewEntry implements BaseColumns {
        public static final String TABLE_NAME = "review";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
    }
}
