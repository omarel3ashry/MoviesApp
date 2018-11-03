package com.example.android.moviesapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class MoviesLoader extends AsyncTaskLoader<List<Movies>> {
    private String Url;

    public MoviesLoader(Context context, String url) {
        super(context);
        Url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Movies> loadInBackground() {
        if (Url == null) {
            return null;
        }
        return QueryUtil.fetchingData(Url);
    }
}