package com.example.android.moviesapp.ui;

import android.app.LoaderManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviesapp.R;
import com.example.android.moviesapp.database.AppExecutors;
import com.example.android.moviesapp.database.MovieDatabase;
import com.example.android.moviesapp.database.Movies;
import com.example.android.moviesapp.ui.review.Review;
import com.example.android.moviesapp.ui.review.ReviewAdapter;
import com.example.android.moviesapp.ui.review.ReviewLoader;
import com.example.android.moviesapp.ui.trailer.Trailer;
import com.example.android.moviesapp.ui.trailer.TrailerAdapter;
import com.example.android.moviesapp.ui.trailer.TrailerLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.movie_title)
    TextView mMovieTitle;
    @BindView(R.id.overview)
    TextView mOverview;
    @BindView(R.id.movie_rate)
    TextView mRateAverage;
    @BindView(R.id.release_date)
    TextView mReleaseDate;
    @BindView(R.id.movie_thumbnail)
    ImageView mThumbnail;
    @BindView(R.id.trailer_list_view)
    ListView trailerList;
    @BindView(R.id.review_list_view)
    ListView reviewList;
    @BindView(R.id.favorite_checkbox)
    CheckBox favoriteCheckBox;

    Intent intent;
    Movies movies;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private static final int TRAILER_LOADER_ID = 1;
    private static final int REVIEW_LOADER_ID = 2;
    private String movieId;
    MovieDatabase movieDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("movieDetails")) {
                Bundle bundle = intent.getExtras();
                assert bundle != null;
                movies = bundle.getParcelable("movieDetails");
            }
        }
        assert movies != null;
        mMovieTitle.setText(movies.getTitle());
        mOverview.setText(movies.getOverview());
        mRateAverage.setText(movies.getRate());
        mReleaseDate.setText(movies.getReleaseDate());
        String moviePosterUrl = movies.getFullImageUrl();
        movieId = movies.getMovieId();
        Picasso.with(this).load(moviePosterUrl).into(mThumbnail);

        List<Trailer> trailers = new ArrayList<>();
        trailerAdapter = new TrailerAdapter(DetailActivity.this, trailers);
        trailerList.setAdapter(trailerAdapter);

        List<Review> reviews = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(DetailActivity.this, reviews);
        reviewList.setAdapter(reviewAdapter);

        getLoaderManager().initLoader(TRAILER_LOADER_ID, null, trailersLoader).forceLoad();
        getLoaderManager().initLoader(REVIEW_LOADER_ID, null, reviewsLoader).forceLoad();

        trailerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trailer trailer = (Trailer) parent.getItemAtPosition(position);
                trailerPlayer(trailer.getKey());
            }
        });
        movieDb = MovieDatabase.getsInstance(getApplicationContext());

        favoriteCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favoriteCheckBox.isChecked()) {
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            movieDb.movieDao().insertMovie(movies);
                        }
                    });
                    Toast toast = Toast.makeText
                            (DetailActivity.this, "Movie has been added to fav", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            movieDb.movieDao().deleteMovie(movies);
                        }
                    });
                    Toast toast = Toast.makeText
                            (DetailActivity.this, "Movie has been removed from fav", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        checkIfFav(movieId);
    }

    private void checkIfFav(String id) {
        LiveData<Movies> movie = movieDb.movieDao().loadMovie(id);
        movie.observe(this, new Observer<Movies>() {
            @Override
            public void onChanged(@Nullable Movies movies) {
                if (movies == null)
                    favoriteCheckBox.setChecked(false);
                else favoriteCheckBox.setChecked(true);
            }
        });
    }

    private void trailerPlayer(String key) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + key));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    private LoaderManager.LoaderCallbacks<List<Trailer>> trailersLoader = new LoaderManager.LoaderCallbacks<List<Trailer>>() {
        @Override
        public Loader<List<Trailer>> onCreateLoader(int id, Bundle args) {
            return new TrailerLoader(DetailActivity.this, movieId);
        }

        @Override
        public void onLoadFinished(Loader<List<Trailer>> loader, List<Trailer> data) {
            if (data != null && !data.isEmpty()) {
                trailerAdapter.addAll(data);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Trailer>> loader) {

        }
    };
    private LoaderManager.LoaderCallbacks<List<Review>> reviewsLoader = new LoaderManager.LoaderCallbacks<List<Review>>() {
        @Override
        public Loader<List<Review>> onCreateLoader(int id, Bundle args) {
            return new ReviewLoader(DetailActivity.this, movieId);
        }

        @Override
        public void onLoadFinished(Loader<List<Review>> loader, List<Review> data) {
            if (data != null && !data.isEmpty()) {
                reviewAdapter.addAll(data);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Review>> loader) {

        }
    };
}
