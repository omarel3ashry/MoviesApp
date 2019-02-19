package com.example.android.moviesapp.ui.review;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.moviesapp.R;

import java.util.List;

public class ReviewAdapter extends ArrayAdapter {

    public ReviewAdapter(@NonNull Context context, List<Review> reviews) {
        super(context, 0, reviews);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_item, parent, false);
        }
        TextView authorName = convertView.findViewById(R.id.author_tv);
        TextView review = convertView.findViewById(R.id.review_tv);
        Review currentReviewObject = (Review) getItem(position);
        authorName.setText(currentReviewObject.getAuthorName());
        review.setText(currentReviewObject.getReview());
        return convertView;
    }
}
