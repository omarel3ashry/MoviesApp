package com.example.android.moviesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

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
    Intent intent;
    Movies movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("movieDetails")) {
                movies = intent.getParcelableExtra("movieDetails");
            }
        }
        mMovieTitle.setText(movies.getTitle());
        mOverview.setText(movies.getOverview());
        mRateAverage.setText(movies.getRate());
        mReleaseDate.setText(movies.getReleaseDate());


    }
}
