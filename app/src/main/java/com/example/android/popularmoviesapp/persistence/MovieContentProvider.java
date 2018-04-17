package com.example.android.popularmoviesapp.persistence;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.android.popularmoviesapp.persistence.MovieContract.MovieEntry.TABLE_NAME;

/**
 * Created by Casi on 4/16/2018.
 */

public class MovieContentProvider extends ContentProvider {

    private static final int MOVIE = 12;
    private static final int MOVIE_WITH_TITLE = 13;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper helper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        helper = new MovieDbHelper(context);
        return true;
    }

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.MovieEntry.TABLE_NAME, MOVIE);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.MovieEntry.TABLE_NAME + "/#", MOVIE_WITH_TITLE);
        return uriMatcher;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = helper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor = null;

        switch (match) {
            case MOVIE:
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOVIE_WITH_TITLE:
                String title = uri.getPathSegments().get(1);
                String mSelection = "title=?";
                String[] mSelectionArgs = new String[]{title};
                retCursor = db.query(TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = helper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        long id = 0;
        switch (match) {
            case MOVIE:
                db.insert(TABLE_NAME, null, contentValues);

                break;
            default:
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.parse(TABLE_NAME + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {

        final SQLiteDatabase db = helper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int mdelete = 0;

        switch (match) {
            case MOVIE_WITH_TITLE:
                String title = uri.getPathSegments().get(1);
                mdelete = db.delete(TABLE_NAME, "title=?", new String[]{title});
                break;
            default:
        }

        if (mdelete != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return mdelete;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int mUpdated = 0;

        int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE_WITH_TITLE:
                String title = uri.getPathSegments().get(1);
                mUpdated = helper.getWritableDatabase().update(TABLE_NAME, contentValues, "title=?", new String[]{title});
                break;
            default:
        }

        if (mUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return mUpdated;
    }
}
