package com.example.android.moviesapp.ui;

import android.app.LoaderManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviesapp.R;
import com.example.android.moviesapp.database.MovieDatabase;
import com.example.android.moviesapp.database.Movies;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Movies>>, MovieAdapter.MovieAdapterOnClickHandler {

    @BindView(R.id.recview)
    RecyclerView moviesRecyclerView;
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
    private MovieDatabase movieDb;
    private List<Movies> moviesList;
    public static String API_KEY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            moviesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset_portrait);
            moviesRecyclerView.setPadding
                    (getDimens(R.dimen.item_offset_portrait), getDimens(R.dimen.item_offset_portrait),
                            getDimens(R.dimen.item_offset_portrait), getDimens(R.dimen.item_offset_portrait));
        } else {
            moviesRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
            itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset_landscape);
            moviesRecyclerView.setPadding
                    (getDimens(R.dimen.item_offset_landscape), getDimens(R.dimen.item_offset_landscape),
                            getDimens(R.dimen.item_offset_landscape), getDimens(R.dimen.item_offset_landscape));
        }
        moviesRecyclerView.addItemDecoration(itemDecoration);

        movieAdapter = new MovieAdapter(new ArrayList<Movies>(), this);
        moviesRecyclerView.setAdapter(movieAdapter);

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
        movieDb = MovieDatabase.getsInstance(getApplicationContext());


    }

    private List<Movies> checkCurrentList(boolean isChecked) {

        if (isChecked) {
            LiveData<List<Movies>> currentMovies = movieDb.movieDao().loadAllMovies();
            return currentMovies.getValue();

        } else {
            return new ArrayList<>();
        }
    }

    private void showFavorites() {

        final LiveData<List<Movies>> movie = movieDb.movieDao().loadAllMovies();
        movie.observe(this, new Observer<List<Movies>>() {
            @Override
            public void onChanged(@Nullable List<Movies> movies) {
                Log.d("DB", "DB CHANGED");
                if (movies != null && movies.size() != 0) {
                    movieAdapter.updateMovies(movies);
                    moviesRecyclerView.setAdapter(movieAdapter);
                } else {
                    Toast toast = Toast.makeText
                            (MainActivity.this, "There is no favorite movies to show!", Toast.LENGTH_LONG);
                    toast.show();
                }

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
            case R.id.favorite_action:
                if (item.isChecked()) {
                    getLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this).forceLoad();
                    errLoad.setVisibility(View.GONE);
                    retryBtn.setVisibility(View.GONE);
                    item.setChecked(false);
                    item.setIcon(R.drawable.ic_star_border_white_24dp);
                } else {
                    showFavorites();
                    item.setChecked(true);
                    item.setIcon(R.drawable.ic_star_white_24dp);
                }

        }
        return super.onOptionsItemSelected(item);
    }

    private int getDimens(int dimenSize) {
        return getResources().getDimensionPixelSize(dimenSize);
    }

    @Override
    public Loader<List<Movies>> onCreateLoader(int id, Bundle args) {

        return new MoviesLoader(MainActivity.this);
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
