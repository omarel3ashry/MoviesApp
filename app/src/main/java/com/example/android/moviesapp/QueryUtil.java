package com.example.android.moviesapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

final class QueryUtil {

    private static final String LOG = QueryUtil.class.getSimpleName();

    private static URL createUrl(String requestedURL) {
        URL url = null;
        try {
            url = new URL(requestedURL);
        } catch (MalformedURLException exception) {
            Log.e(LOG, "Error with creating URL ", exception);
        }
        return url;
    }

    static List<Movies> fetchingData(String url) {

        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(createUrl(url));
        } catch (IOException e) {
            Log.e(LOG, "Error closing input stream", e);
        }

        return extractMovies(jsonResponse);
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG, "Error response code: " + urlConnection.getResponseCode());
                Log.i("URL", url.toString());
            }
        } catch (IOException e) {
            Log.e(LOG, "Problem retrieving the news JSON results", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            Log.i("URL", url.toString());
            return jsonResponse;
        }
    }

    private static String readFromStream(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    private static List<Movies> extractMovies(String jsonRes) {

        List<Movies> movies = new ArrayList<>();

        if (TextUtils.isEmpty(jsonRes)) {
            return null;
        }
        try {

            JSONObject root = new JSONObject(jsonRes);
            JSONArray results = root.optJSONArray("results");

            for (int i = 0; i < results.length(); i++) {

                JSONObject object = results.getJSONObject(i);
                String posterPath = object.getString("poster_path");
                String movieTitle = object.getString("title");
                String voteAverage = object.getString("vote_average");
                String overview = object.getString("overview");
                String releaseDate = object.getString("release_date");
                movies.add(
                        new Movies(posterPath, movieTitle, voteAverage, overview, releaseDate));
            }
        } catch (JSONException e) {
            Log.e(LOG, "Problem parsing the movies JSON results", e);
        }
        return movies;
    }
}
