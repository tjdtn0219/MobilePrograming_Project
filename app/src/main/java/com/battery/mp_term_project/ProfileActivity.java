package com.battery.mp_term_project;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    SearchView contentSearchView;
    ImageView profile_pt;
    TextView profile_name;
    TextView profile_text;
    Button btn_chat;
    Button btn_Edit;

    private List<ContentRecyclerViewItem> itemList;
    private ContentRecyclerViewAdapter contentRecyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference myRef;

    String uid;
    String user_pt;
    String user_name;
    String user_txt;

    String id_test;


    //프로필 사진 요청코드
    private static final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        id_test = ((GlobalVar) getApplication()).getCurrent_user().getUid();
        Log.d("profile", id_test);

        profile_pt = (ImageView) findViewById(R.id.profile_pt);
        profile_name = (TextView) findViewById(R.id.profile_name);
        profile_text = (TextView) findViewById(R.id.profile_text);





        //프로필 요소 추가
         getURIFromStorage(uid);



        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("Users").child(uid).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_name = dataSnapshot.getValue(String.class);

                profile_name.setText(user_name);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        myRef.child("Users").child(uid).child("profileText").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_txt = dataSnapshot.getValue(String.class);

                profile_text.setText(user_txt);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });





        //컨첸츠 리사이클러뷰 추가
        profilebindList();






        //프로필 이름, 글 수정하는 버튼
        btn_Edit = (Button) findViewById(R.id.btn_Edit);
        btn_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileEditActivity.class);
                startActivity(intent);
            }
        });

        btn_chat = findViewById(R.id.btn_chat);
        if( id_test.equals(uid)){
            btn_chat.setVisibility(View.GONE);
        }else{
            btn_chat = findViewById(R.id.btn_chat);
            btn_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),ChattingActivity.class);
                    startActivity(intent);
                }
            });
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
                        if(content != null && content.getUser().getUid().equals(uid)) {
                            content.setKey(snapshot.getKey());
                            Log.d("profile", "컨텐츠uid1 : " + content.getUser().getUid());
                            Log.d("profile", "컨텐츠uid2 : " + uid);
                            itemList.add(new ContentRecyclerViewItem(content));

                        }
                    }
                    RecyclerView mainRecyclerView = findViewById(R.id.profile_recycler_view);

                    contentRecyclerViewAdapter = new ContentRecyclerViewAdapter(itemList, ((GlobalVar) getApplication()).getCurrent_user());
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

    private void getURIFromStorage(String uid) {
        String get_path = "Profile_images/" + uid + "/" + "profile.jpg";
        StorageReference pathRef = FirebaseStorage.getInstance().getReference().child(get_path);
        pathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                    //Glide.with(getApplicationContext()).load(uri).into(profile_pt);
                Glide.with(getApplicationContext())
                        .load(uri)
                        .into(profile_pt);

                }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("profile", "프로필uri 실패 ");
            }
        });
    }



}
