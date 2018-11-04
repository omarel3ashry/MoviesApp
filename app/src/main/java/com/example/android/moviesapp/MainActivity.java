package com.example.android.moviesapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Movies>>, MovieAdapter.MovieAdapterOnClickHandler {

    @BindView(R.id.recview)
    RecyclerView MoviesList;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.load_tv)
    TextView loadData;
    @BindView(R.id.err_load)
    TextView errLoad;
    @BindView(R.id.retry_btn)
    Button retryBtn;
    private MovieAdapter movieAdapter;
    ItemOffsetDecoration itemDecoration;
    NetworkInfo networkInfo;
    private static final int LOADER_ID = 0;
    public static String URL = "https://api.themoviedb.org/3/movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            MoviesList.setLayoutManager(new GridLayoutManager(this, 3));
            itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset_portrait);
            MoviesList.setPadding
                    (getDimens(R.dimen.item_offset_portrait), getDimens(R.dimen.item_offset_portrait),
                            getDimens(R.dimen.item_offset_portrait), getDimens(R.dimen.item_offset_portrait));
        } else {
            MoviesList.setLayoutManager(new GridLayoutManager(this, 4));
            itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset_landscape);
            MoviesList.setPadding
                    (getDimens(R.dimen.item_offset_landscape), getDimens(R.dimen.item_offset_landscape),
                            getDimens(R.dimen.item_offset_landscape), getDimens(R.dimen.item_offset_landscape));
        }

        MoviesList.addItemDecoration(itemDecoration);
        movieAdapter = new MovieAdapter(new ArrayList<Movies>(), this);
        MoviesList.setAdapter(movieAdapter);
        if (networkInfo != null && networkInfo.isConnected()) {
            errLoad.setVisibility(View.GONE);
            retryBtn.setVisibility(View.GONE);
            getLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
        } else {
            progressBar.setVisibility(View.GONE);
            loadData.setVisibility(View.GONE);
            errLoad.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.VISIBLE);
        }
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this).forceLoad();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.settings_action:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private int getDimens(int dimenSize) {
        return getResources().getDimensionPixelSize(dimenSize);
    }

    @Override
    public Loader<List<Movies>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String sortBy = sharedPref.getString(
                getString(R.string.sort_by_key),
                getString(R.string.sort_by_popular_value));
        Uri uri = Uri.parse(URL);
        Uri.Builder builder = uri.buildUpon();
        builder.appendPath(sortBy);
        builder.appendQueryParameter("api_key", "your_key");

        return new MoviesLoader(MainActivity.this, builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Movies>> loader, List<Movies> data) {

        if (data != null && !data.isEmpty()) {
            movieAdapter.updateMovies(data);
            progressBar.setVisibility(View.GONE);
            loadData.setVisibility(View.GONE);
            errLoad.setVisibility(View.GONE);
            retryBtn.setVisibility(View.GONE);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Movies>> loader) {

    }

    @Override
    public void OnMovieClick(Movies m) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("movieDetails", m);
        startActivity(intent);
    }
}
