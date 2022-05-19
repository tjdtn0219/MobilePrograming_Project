package com.battery.mp_term_project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

public class ProfileEditActivity extends AppCompatActivity {

    SearchView contentSearchView;
    ImageView profile_pt;
    TextView profile_name;
    TextView profile_text;
    Button btn_Edit;

    //프로필 사진 요청코드
    private static final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

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
        profile_name = (EditText) findViewById(R.id.profile_name);
        profile_name.setText(((GlobalVar) getApplication()).getCurrent_user().getName());

        profile_text = (EditText) findViewById(R.id.profile_text);
        profile_text.setText(((GlobalVar) getApplication()).getCurrent_user().getProfileText());

        //프로필 이름, 글 수정하는 버튼
        btn_Edit = (Button) findViewById(R.id.btn_Edit);
        btn_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("name", profile_name.getText().toString());
                intent.putExtra("text", profile_text.getText().toString());

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
