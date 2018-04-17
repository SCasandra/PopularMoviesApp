package com.example.android.popularmoviesapp.persistence;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Casi on 4/16/2018.
 */

public class MovieContract {
    public static final String AUTHORITY = "com.example.android.popularmoviesapp.persistence.MovieContentProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + MovieEntry.TABLE_NAME);

    public static final class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_TITILE = "title";
    }
}
