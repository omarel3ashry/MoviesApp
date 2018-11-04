package com.example.android.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
        Picasso.with(this).load(moviePosterUrl).into(mThumbnail);
    }
}
