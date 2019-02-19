package com.example.android.moviesapp.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "movie")
public class Movies implements Parcelable {
    @PrimaryKey
    @NonNull
    private String movieId;
    private String Title;
    private String Rate;
    private String Overview;
    private String posterUrl;
    private String releaseDate;
    @Ignore
    private static final String imageBaseURL = "http://image.tmdb.org/t/p/";
    @Ignore
    private static final String imageSize = "w185";


    public Movies(@NonNull String movieId, String Title, String Rate, String Overview, String posterUrl, String releaseDate) {
        this.movieId = movieId;
        this.Title = Title;
        this.Rate = Rate;
        this.Overview = Overview;
        this.posterUrl = posterUrl;
        this.releaseDate = releaseDate;
    }

//    public Movies(@NonNull String movieId, String Title) {
//        this.movieId = movieId;
//        this.Title = Title;
//    }

    public void setMovieId(@NonNull String movieId) {
        this.movieId = movieId;
    }

    @NonNull
    public String getMovieId() {
        return movieId;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getTitle() {
        return Title;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    public String getRate() {
        return Rate;
    }

    public void setOverview(String overview) {
        Overview = overview;
    }

    public String getOverview() {
        return Overview;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }


    @Ignore
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };


    public String getFullImageUrl() {
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
        movieId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterUrl);
        dest.writeString(Title);
        dest.writeString(Rate);
        dest.writeString(Overview);
        dest.writeString(releaseDate);
        dest.writeString(movieId);


    }


}
