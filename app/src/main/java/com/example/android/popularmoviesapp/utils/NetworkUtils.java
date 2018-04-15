package com.example.android.popularmoviesapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by Casi on 4/14/2018.
 */

public class NetworkUtils {
    private static String API_KEY = "";
    private static String MOVIES_BASE_URL = "api.themoviedb.org";
    public static String POPULAR_FILTER = "popular";
    public static String TOP_RATED_FILTER = "top_rated";
    private static String API_KEY_PARAM = "api_key";
    private static String SCHEMA = "http";
    public static String VIDEO = "/videos";
    public static String REWIEWS = "/reviews";

    /**
     * Builds the URL used to talk to the movie server using a filter. This filter is based
     * on the query capabilities of the movie provider that we are using.
     *
     * @param extraPath The filter that will be queried for.
     * @return The URL to use to query the movie server.
     */
    public static URL buildUrl(String extraPath) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEMA)
                .authority(MOVIES_BASE_URL)
                .appendPath("3")
                .appendPath("movie")
                .appendPath(extraPath)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        String jsonResponse = "";
        // If the URL is null, then return early
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            // TODO: Handle the exception
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static boolean isOnline(Context c) {
        ConnectivityManager cm =
                (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
