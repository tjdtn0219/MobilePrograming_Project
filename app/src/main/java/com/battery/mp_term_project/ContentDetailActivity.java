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
        String userName = intent.getStringExtra("userName");
        String userText = intent.getStringExtra("userText");
        contentDetailAdapter.addData(new ContentDetailAdapter.ContentDetailData(userName, userText));

        for (int i = 0; i < 50; i++)
        {
            contentDetailAdapter.addData(new ContentDetailAdapter.ContentDetailData("Test" + i, "테스트 댓글입니다!"));
        }
    }
}
