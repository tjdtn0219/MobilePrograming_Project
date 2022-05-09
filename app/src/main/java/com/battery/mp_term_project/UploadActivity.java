package com.battery.mp_term_project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
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
import android.widget.Toast;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class UploadActivity extends AppCompatActivity{


    private static final String TAG = "UploadActivity";
    ArrayList<String> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체
    private AppDatabase db = null;

    RecyclerView recyclerView;  // 이미지를 보여줄 리사이클러뷰
    MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터

    EditText mUploadText;
    SearchView contentSearchView;
    ImageButton btn_add_photo;
    Button btn_upload;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // 앨범으로 이동하는 버튼
        btn_add_photo = (ImageButton) findViewById(R.id.btn_add_photo);
        btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2222);
            }
        });

        db = AppDatabase.getInstance(this);

        //main 스레드에서는 db접근 불가, 새로운 thread 만들어서 접근
        class InsertRunnable implements Runnable {

            private Content content;

            public InsertRunnable(Content c) {
                content = c;
            }

            @Override
            public void run() {
                try{
                    db.contentDao().Insert(content);
                    Log.d("TAGGGG", content.getText());
                }
                catch (Exception e) {
                    Log.e("Insert Error in Upload", e.toString());
                }
            }
        }

        mUploadText = (EditText) findViewById(R.id.txt_upload);
        btn_upload = (Button) findViewById(R.id.button);
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Content content = new Content();
                content.setWriter_id(((GlobalVar) getApplication()).getUid());
                content.setText(mUploadText.getText().toString());
                content.setUri(uriList);
                long now = System.currentTimeMillis();
                content.setTime(now);

                InsertRunnable insertRunnable = new InsertRunnable(content);
                Thread uploadThread = new Thread(insertRunnable);
                uploadThread.start();

                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra("FromUpload", "게시글을 성공적으로 업로드 하였습니다.");
                startActivity(intent);

            }
        });

        recyclerView = findViewById(R.id.recyclerView);
    }

    // 앨범에서 액티비티로 돌아온 후 실행되는 메서드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null){   // 어떤 이미지도 선택하지 않은 경우
            Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
        }
        else{   // 이미지를 하나라도 선택한 경우
            if(data.getClipData() == null){     // 이미지를 하나만 선택한 경우
                Log.e("single choice: ", String.valueOf(data.getData()));
                Uri imageUri = data.getData();
                uriList.add(imageUri.toString());

                adapter = new MultiImageAdapter(uriList, getApplicationContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
            }
            else{      // 이미지를 여러장 선택한 경우
                ClipData clipData = data.getClipData();
                Log.e("clipData", String.valueOf(clipData.getItemCount()));

                if(clipData.getItemCount() > 10){   // 선택한 이미지가 11장 이상인 경우
                    Toast.makeText(getApplicationContext(), "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                }
                else{   // 선택한 이미지가 1장 이상 10장 이하인 경우
                    Log.e(TAG, "multiple choice");

                    for (int i = 0; i < clipData.getItemCount(); i++){
                        Uri imageUri = clipData.getItemAt(i).getUri();  // 선택한 이미지들의 uri를 가져온다.
                        try {
                            uriList.add(imageUri.toString());  //uri를 list에 담는다.

                        } catch (Exception e) {
                            Log.e(TAG, "File select error", e);
                        }
                    }

                    adapter = new MultiImageAdapter(uriList, getApplicationContext());
                    recyclerView.setAdapter(adapter);   // 리사이클러뷰에 어댑터 세팅
                    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));     // 리사이클러뷰 수평 스크롤 적용
                }
            }
        }
    }



}

