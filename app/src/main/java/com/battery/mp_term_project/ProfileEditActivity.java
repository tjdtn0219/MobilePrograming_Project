package com.battery.mp_term_project;

import android.content.ClipData;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class ProfileEditActivity extends AppCompatActivity {

    ImageView profile_pt;
    TextView profile_name;
    TextView profile_text;
    Button btn_Edit;
    String pt_uri;

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
        getURIFromStorage(((GlobalVar) getApplication()).getCurrent_user().getUid());

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

        //프로필 이름, 글 수정 완료 버튼
        btn_Edit = (Button) findViewById(R.id.btn_Edit);
        btn_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UploadToFirebaseStorage();

                User userdata = ((GlobalVar) getApplication()).getCurrent_user();
                userdata.setName(profile_name.getText().toString());
                userdata.setProfileText(profile_text.getText().toString());
                userdata.setProfileImage(pt_uri);

                ((GlobalVar) getApplication()).setCurrent_user(userdata);

                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
                myRef.child("Users")
                        .child(((GlobalVar) getApplication()).getCurrent_user().getUid())
                        .setValue(userdata);//Push to RDB



                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("uid", ((GlobalVar) getApplication()).getCurrent_user().getUid());
                startActivity(intent);
                finish();
                Toast.makeText(getApplicationContext(), "프로필 수정이 완료되었습니다.", Toast.LENGTH_LONG).show();
                /*Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(getApplicationContext(), "프로필 수정이 완료되었습니다.", Toast.LENGTH_LONG).show();*/

            }
        });
        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
        if(data == null){   // 어떤 이미지도 선택하지 않은 경우
            Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
        }
        else{

            Uri imageUri = data.getData();
            Glide.with(getApplicationContext()).load(imageUri).into(profile_pt);
            pt_uri = imageUri.toString();
        }}
    }
}
    private void UploadToFirebaseStorage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        String uid = ((GlobalVar) getApplication()).getCurrent_user().getUid();
        String fileName = "Profile_images/" + uid + "/" + "profile.jpg";
        StorageReference uploadRef = storageRef.child(fileName);
        if (pt_uri != null) {
            UploadTask uploadTask = uploadRef.putFile(Uri.parse(pt_uri));

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.e("Image Upload FireStorage", "SUCCESS");
                }
            });

        }

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
