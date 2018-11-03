package com.example.android.moviesapp;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Movies implements Parcelable {

    private String Title, Rate, Overview, posterUrl, releaseDate;
    private static final String imageBaseURL = "http://image.tmdb.org/t/p/";
    private static final String imageSize = "w185";

    Movies(String posterUrl, String title, String rate, String overview, String releaseDate) {
        this.Title = title;
        this.Rate = rate;
        this.Overview = overview;
        this.posterUrl = posterUrl;
        this.releaseDate = releaseDate;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };



    public String getTitle() {
        return Title;
    }

    public String getRate() {
        return Rate;
    }

    public String getOverview() {
        return Overview;
    }

    private String getPosterUrl() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    private Movies(Parcel in) {
        posterUrl = in.readString();
        Title = in.readString();
        Rate = in.readString();
        Overview = in.readString();
        releaseDate = in.readString();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterUrl);
        dest.writeString(Title);
        dest.writeString(Rate);
        dest.writeString(Overview);
        dest.writeString(releaseDate);


    }
}
