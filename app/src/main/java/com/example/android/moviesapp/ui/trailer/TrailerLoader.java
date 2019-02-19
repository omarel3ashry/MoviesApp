package com.example.android.moviesapp.ui.trailer;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;

import com.example.android.moviesapp.util.QueryUtil;

import java.util.List;

import static com.example.android.moviesapp.ui.MainActivity.API_KEY;
import static com.example.android.moviesapp.ui.MainActivity.URL;

public class TrailerLoader extends AsyncTaskLoader<List<Trailer>> {
    private String movieId;

    private String buildUrl() {
        Uri uri = Uri.parse(URL);
        Uri.Builder builder = uri.buildUpon();
        builder.appendPath(movieId);
        builder.appendPath("videos");
        builder.appendQueryParameter("api_key", API_KEY);
        return builder.build().toString();
    }


    public TrailerLoader(Context context, String movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Trailer> loadInBackground() {
        return QueryUtil.extractTrailer(QueryUtil.fetchingData(buildUrl()));
    }
}
