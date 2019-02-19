package com.example.android.moviesapp.ui.review;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;

import com.example.android.moviesapp.util.QueryUtil;

import java.util.List;

import static com.example.android.moviesapp.ui.MainActivity.API_KEY;
import static com.example.android.moviesapp.ui.MainActivity.URL;

public class ReviewLoader extends AsyncTaskLoader<List<Review>> {
    private String movieId;

    private String buildUrl() {
        Uri uri = Uri.parse(URL);
        Uri.Builder builder = uri.buildUpon();
        builder.appendPath(movieId);
        builder.appendPath("reviews");
        builder.appendQueryParameter("api_key", API_KEY);
        return builder.build().toString();
    }

    public ReviewLoader(Context context, String movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Override
    public List<Review> loadInBackground() {
        return QueryUtil.extractReviews(QueryUtil.fetchingData(buildUrl()));
    }
}
