package com.example.android.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.popularmoviesapp.utils.Constants;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Intent i = getIntent();
        setTitle(i.getStringExtra(Constants.TITLE));
    }

}
