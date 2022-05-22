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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    SearchView contentSearchView;
    ImageView profile_pt;
    TextView profile_name;
    TextView profile_text;
    EditText profile_name_edit;
    EditText profile_text_edit;
    Button btn_chat;

    private List<ContentRecyclerViewItem> itemList;
    private ContentRecyclerViewAdapter contentRecyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference myRef;


    //프로필 사진 요청코드
    private static final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //컨첸츠 리사이클러뷰 추가
        profilebindList();

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
        profile_name = (TextView) findViewById(R.id.profile_name);
        profile_name.setText(((GlobalVar) getApplication()).getCurrent_user().getName());
        profile_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile_name.setVisibility(View.INVISIBLE);
                profile_name_edit = (EditText) findViewById(R.id.profile_name_edit);
                profile_name_edit.setVisibility(View.VISIBLE);
                profile_name_edit.setText(((GlobalVar) getApplication()).getCurrent_user().getName());
            }
        });

        profile_text = (TextView) findViewById(R.id.profile_text);
        profile_text.setText(((GlobalVar) getApplication()).getCurrent_user().getProfileText());
        profile_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile_text.setVisibility(View.INVISIBLE);
                profile_text_edit = (EditText) findViewById(R.id.profile_text_edit);
                profile_text_edit.setVisibility(View.VISIBLE);
                profile_text_edit.setText(((GlobalVar) getApplication()).getCurrent_user().getProfileText());

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

    private void profilebindList(){

        itemList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        myRef = FirebaseDatabase.getInstance().getReference();
        Query myTopPostsQuery = myRef.child("Contents");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Content content = snapshot.getValue(Content.class);
                    itemList.add(new ContentRecyclerViewItem(null,
                            content.getUser().getName(), content.getText(), content.getImages(), content.getLikes()));
                }
                RecyclerView mainRecyclerView = findViewById(R.id.profile_recycler_view);

                contentRecyclerViewAdapter = new ContentRecyclerViewAdapter(itemList);
                mainRecyclerView.setAdapter(contentRecyclerViewAdapter);

                mainRecyclerView.setLayoutManager(layoutManager);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };
        myTopPostsQuery.addValueEventListener(postListener);


    }


}
