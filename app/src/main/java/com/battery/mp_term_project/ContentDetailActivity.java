package com.battery.mp_term_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class ContentDetailActivity extends AppCompatActivity {

    ContentDetailAdapter contentDetailAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contentdetail);

        RecyclerView contentDetailRecyclerView = findViewById(R.id.contentDetailRecyclerView);
        contentDetailAdapter = new ContentDetailAdapter(this);
        contentDetailRecyclerView.setAdapter(contentDetailAdapter);

        loadContentData();
    }

    private void loadContentData()
    {
        Intent intent = getIntent();
        if (intent != null) {
            String key = intent.getStringExtra("ContentKey");

        }
    }
}
