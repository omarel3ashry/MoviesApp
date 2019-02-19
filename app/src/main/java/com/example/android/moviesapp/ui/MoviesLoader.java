package com.example.android.moviesapp.ui;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.example.android.moviesapp.R;
import com.example.android.moviesapp.database.MovieDatabase;
import com.example.android.moviesapp.database.Movies;
import com.example.android.moviesapp.util.QueryUtil;

import java.util.List;

import static com.example.android.moviesapp.ui.MainActivity.URL;

public class MoviesLoader extends AsyncTaskLoader<List<Movies>> {

    public MoviesLoader(Context context) {
        super(context);
    }

    private String buildUrl() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortBy = sharedPref.getString(
                getContext().getString(R.string.sort_by_key),
                getContext().getString(R.string.sort_by_popular_value));
        Uri uri = Uri.parse(URL);
        Uri.Builder builder = uri.buildUpon();
        builder.appendPath(sortBy);
        builder.appendQueryParameter("api_key", "99da60832bacd6b5001049dc06c1442e");
        return builder.build().toString();
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Movies> loadInBackground() {
//
        return QueryUtil.extractMovies(QueryUtil.fetchingData(buildUrl()));
    }
}
