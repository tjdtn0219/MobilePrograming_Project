package com.battery.mp_term_project;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class ContentDetailActivity extends AppCompatActivity {

    ContentDetailAdapter contentDetailAdapter;
    EditText commentEditText;
    String currentContentKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contentdetail);

        RecyclerView contentDetailRecyclerView = findViewById(R.id.contentDetailRecyclerView);
        contentDetailAdapter = new ContentDetailAdapter(this);
        contentDetailRecyclerView.setAdapter(contentDetailAdapter);

        loadContentData();

        commentEditText = findViewById(R.id.commentEditText);
        commentEditText.setOnFocusChangeListener((view, b) -> hideKeyboard());

        Button addCommentButton = findViewById(R.id.addCommentButton);
        addCommentButton.setOnClickListener((view) -> addComment());
    }

    private void loadContentData()
    {
        Intent intent = getIntent();
        if (intent != null) {
            String key = intent.getStringExtra("ContentKey");
            currentContentKey = key;

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();
            Query contentDetailQuery = myRef.child("Contents").equalTo(null, key);
            ValueEventListener contentDetailPostListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot querySnapShot) {

                    if (querySnapShot.getChildrenCount() > 1)
                    {
                        Log.w("Warning", "There is duplicate contents");
                    }

                    if (querySnapShot.hasChildren()) {
                        DataSnapshot contentSnapshot = querySnapShot.getChildren().iterator().next();
                        Content content = contentSnapshot.getValue(Content.class);
                        if (content != null) {
                            content.setKey(contentSnapshot.getKey());
                            contentDetailAdapter.setData(0, new ContentDetailAdapter.ContentDetailData(
                                    content.getKey(),
                                    content.getUser().getName(),
                                    content.getText(),
                                    content.getImages().size(),
                                    content.getLikes()
                            ));
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                }
            };
            contentDetailQuery.addValueEventListener(contentDetailPostListener);

            Query commentQuery = myRef.child("Comments").orderByChild("content_id").equalTo(key);
            ValueEventListener commentPostListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot querySnapshot) {

                    SortedMap<Long, Comment> comments = new TreeMap<>();
                    for (DataSnapshot commentSnapshot : querySnapshot.getChildren()) {
                        Comment comment = commentSnapshot.getValue(Comment.class);
                        if (comment != null) {
                            comments.put(comment.getTime(), comment);
                        }
                    }

                    int commentIndex = 0;
                    for (Map.Entry<Long, Comment> entry : comments.entrySet())
                    {
                        Comment comment = entry.getValue();
                        contentDetailAdapter.setData(commentIndex + 1, new ContentDetailAdapter.ContentDetailData( "", comment.getUser().getName(), comment.getText()));
                        commentIndex++;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            commentQuery.addValueEventListener(commentPostListener);
        }
    }

    private void addComment()
    {
        String commentText = commentEditText.getText().toString();
        if (commentText.isEmpty()) {
            return;
        }
        User user = ((GlobalVar)getApplication()).getCurrent_user();
        long now = System.currentTimeMillis();
        String commentKey = user.getUid() + "_" + now;
        Comment comment = new Comment(user, commentText, now, currentContentKey);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("Comments").child(commentKey).setValue(comment);

        commentEditText.setText("");
        commentEditText.clearFocus();
    }

    private void hideKeyboard() {
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(commentEditText.getWindowToken(), 0);
    }
}
