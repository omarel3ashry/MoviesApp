package com.example.android.moviesapp;

import android.net.Uri;

public class Movies {

    private String Title, Rate, Overview, posterUrl, releaseDate;
    private static final String imageBaseURL = "http://image.tmdb.org/t/p/";
    private static final String imageSize = "w185";

    public Movies(String posterUrl, String title, String rate, String overview, String releaseDate) {
        Title = title;
        Rate = rate;
        Overview = overview;
        this.posterUrl = posterUrl;
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return Title;
    }

    public String getRate() {
        return Rate;
    }

    public String getOverview() {
        return Overview;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
    String getFullImageUrl() {
        return Uri.parse(imageBaseURL).buildUpon()
                .appendEncodedPath(imageSize)
                .appendEncodedPath(this.getPosterUrl()).build().toString();
    }
}
