package com.example.android.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.popularmoviesapp.model.Movie;
import com.example.android.popularmoviesapp.model.Review;
import com.example.android.popularmoviesapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ListView listView = findViewById(R.id.lv);
        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra(Constants.MOVIE);
        setTitle(movie.getTitle());
        List<Review> reviews = intent.getParcelableArrayListExtra(Constants.REVIEW);

        List<String> str = new ArrayList<>();
        for (int i = 0; i < reviews.size(); i++) {
            if (reviews.get(i).getMovieId() == movie.getId()) {
                str.add(reviews.get(i).getAuthor() + ": " + reviews.get(i).getContent());
            }
        }
        listView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, str.toArray()));
    }

}
