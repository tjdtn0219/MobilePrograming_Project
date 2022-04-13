package com.battery.mp_term_project;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    SearchView contentSearchView;
    ImageView profile_pt;
    Button btn_chat;

    //프로필 사진 요청코드
    private static final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        // 앨범으로 이동하는 버튼
        profile_pt = (ImageView) findViewById(R.id.profile_pt);
        profile_pt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        btn_chat = findViewById(R.id.btn_chat);
        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ChattingActivity.class);
                startActivity(intent);
            }
        });
        
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri uri = data.getData();
                    Glide.with(getApplicationContext()).load(uri).into(profile_pt); //다이얼로그 이미지사진에 넣기
                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {// 취소시 호출할 행동 쓰기
            }
        }
    }


}
