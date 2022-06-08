package com.battery.mp_term_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private LayoutInflater inflater;
    private CategoryAdapter categoryAdapter;
    private DatabaseReference myRef;
    private List<ContentRecyclerViewItem> itemList;
    private ContentRecyclerViewAdapter contentRecyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar ac = getSupportActionBar();
        ac.setTitle("FFM");

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ImageButton addCategoryButton = findViewById(R.id.addCategoryButton);
        addCategoryButton.setOnClickListener(view -> {
            createAddCategoryDialog();
        });

        RecyclerView categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        categoryAdapter = new CategoryAdapter(this);
        getCategoriesFromFireBase(categoryAdapter);
        categoryRecyclerView.setAdapter(categoryAdapter);

        //컨첸츠 리사이클러뷰 추가
        mainbindList();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.actionbar_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int itemId = item.getItemId();
        if (itemId == R.id.item_search)
        {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        }
        else if (itemId == R.id.item_upload)
        {
            Intent intent = new Intent(this, UploadActivity.class);
            startActivity(intent);
        }
        else if (itemId == R.id.item_profile)
        {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("uid", ((GlobalVar) getApplication()).getCurrent_user().getUid());
            startActivity(intent);
        }

        return false;
    }

    private void createAddCategoryDialog()
    {
        View dialogView = inflater.inflate(R.layout.main_create_category_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();

        EditText categoryName = dialogView.findViewById(R.id.categoryName);
        Button confirmButton = dialogView.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(view -> {
            ((GlobalVar) getApplication()).getCurrent_user().addCategory(categoryName.getText().toString());
            DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference();
            categoryRef.child("Users")
                    .child(((GlobalVar) getApplication()).getCurrent_user().getUid())
                    .child("categories")
                    .setValue(((GlobalVar) getApplication()).getCurrent_user().getCategories());

            categoryAdapter.addCategory(categoryName.getText().toString());
            alertDialog.dismiss();
        });

        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(view -> {
            alertDialog.cancel();
        });

        alertDialog.show();
    }

    private void mainbindList(){
        layoutManager = new LinearLayoutManager(this);
        myRef = FirebaseDatabase.getInstance().getReference();
        Query myTopPostsQuery = myRef.child("Contents");
        RecyclerView mainRecyclerView = findViewById(R.id.main_recycler_view);

        mHandler.postDelayed(new Runnable() {
            public void run() {
                ValueEventListener postListener_content = new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        itemList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Content content = snapshot.getValue(Content.class);
                            if(content != null) {
                                content.setKey(snapshot.getKey());
                                itemList.add(new ContentRecyclerViewItem(content));
                            }
                        }

                        contentRecyclerViewAdapter = new ContentRecyclerViewAdapter(itemList, ((GlobalVar) getApplication()).getCurrent_user());
                        mainRecyclerView.setAdapter(contentRecyclerViewAdapter);
                        mainRecyclerView.setLayoutManager(layoutManager);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                    }
                };
                myTopPostsQuery.addListenerForSingleValueEvent(postListener_content);
            }
        }, 300); // 0.3초후



    }

    private void getCategoriesFromFireBase(CategoryAdapter categoryAdapter) {
        Intent getFromLogIn = getIntent();
        String uid = getFromLogIn.getStringExtra("userID");
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference();
        categoryRef.child("Users").child(uid)
                .child("categories").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(!task.isSuccessful()) {
                        Log.e("firebase", "Error getting categories", task.getException());
                    } else {
                        for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                            categoryAdapter.addCategory(dataSnapshot.getValue(String.class));
                        }
                    }
            }
        });

    }

    @Override
    protected  void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        mainbindList();

        setIntent(intent);
        String toastWords;
        String FromUpload = intent.getStringExtra("FromUpload");
        toastWords = FromUpload;
        Toast.makeText(getApplicationContext(), toastWords, Toast.LENGTH_SHORT).show();
    }

}