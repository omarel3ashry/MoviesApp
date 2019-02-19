package com.example.android.moviesapp.ui.review;

public class Review {

    private String authorName;
    private String review;

    public Review(String authorName, String review) {
        this.authorName = authorName;
        this.review = review;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getReview() {
        return review;
    }
}
