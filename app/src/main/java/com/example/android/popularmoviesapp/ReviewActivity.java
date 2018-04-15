package com.example.android.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.popularmoviesapp.model.Movie;
import com.example.android.popularmoviesapp.utils.Constants;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ListView listView = findViewById(R.id.lv);
        Intent intent = getIntent();

        Movie movie = intent.getParcelableExtra(Constants.MOVIE);
        setTitle(movie.getTitle());

        //List of Items
        String[] listItems = new String[movie.getReview().size()];
        for (int i = 0; i < movie.getReview().size(); i++) {
            listItems[i] = movie.getReview().get(i).getAuthor() + ": " + movie.getReview().get(i).getContent();
        }

        listView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems));
    }

}
