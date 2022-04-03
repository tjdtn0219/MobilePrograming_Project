package com.battery.mp_term_project;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {

    SearchView contentSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        contentSearchView = findViewById(R.id.contentSearchView);
        contentSearchView.requestFocus();
    }
}
