package com.example.android.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.popularmoviesapp.utils.Constants;

public class TrailerActivity extends AppCompatActivity {
    private static String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);
        Intent i = getIntent();
        setTitle(i.getStringExtra(Constants.TITLE));

    }
}