package com.battery.mp_term_project;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView searchResultRecyclerView;
    private ContentRecyclerViewAdapter contentSearchAdapter;
    private UserSearchAdapter userSearchAdapter;

    private RadioButton profilesButton;
    private RadioButton contentsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchView contentSearchView = findViewById(R.id.contentSearchView);
        contentSearchView.setIconified(false);
        contentSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (profilesButton.isChecked())
                {
                    searchUsers(s);
                }
                else if (contentsButton.isChecked())
                {
                    searchContents(s);
                }
                contentSearchView.clearFocus();
                searchResultRecyclerView.requestFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        profilesButton = findViewById(R.id.profilesButton);
        profilesButton.setChecked(true);
        contentsButton = findViewById(R.id.contentsButton);

        searchResultRecyclerView = findViewById(R.id.searchResultRecyclerView);
        searchResultRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contentSearchAdapter = new ContentRecyclerViewAdapter();
        userSearchAdapter = new UserSearchAdapter();

        String keyword = getIntent().getStringExtra("Keyword");
        if (keyword != null)
        {
            contentSearchView.setQuery(keyword, true);
        }
    }

    public void searchContents(String key)
    {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        Query contentsQuery = databaseRef.child("Contents");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<ContentRecyclerViewItem> contentItemList = new ArrayList<>();
                for (DataSnapshot contentSnapshot : snapshot.getChildren())
                {
                    Content content = contentSnapshot.getValue(Content.class);
                    User user = content.getUser();
                    if (user.getName().toLowerCase(Locale.ROOT).contains(key.toLowerCase(Locale.ROOT))
                    || content.getText().toLowerCase(Locale.ROOT).contains(key.toLowerCase(Locale.ROOT)))
                    {
                        content.setKey(contentSnapshot.getKey());
                        contentItemList.add(new ContentRecyclerViewItem(content));
                    }
                }
                searchResultRecyclerView.setAdapter(contentSearchAdapter);
                contentSearchAdapter.setItemList(contentItemList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        contentsQuery.addValueEventListener(postListener);
    }

    public void searchUsers(String key)
    {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        Query contentsQuery = databaseRef.child("Users");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<UserSearchAdapter.UserSearchResult> userSearchResultList = new ArrayList<>();
                for (DataSnapshot contentSnapshot : snapshot.getChildren())
                {
                    User user = contentSnapshot.getValue(User.class);
                    if (user.getName().toLowerCase(Locale.ROOT).contains(key.toLowerCase(Locale.ROOT))
                            || user.getProfileText().toLowerCase(Locale.ROOT).contains(key.toLowerCase(Locale.ROOT)))
                    {
                        userSearchResultList.add(new UserSearchAdapter.UserSearchResult(null, user.getName(), user.getProfileText(),user.getUid()));
                    }
                }
                searchResultRecyclerView.setAdapter(userSearchAdapter);
                userSearchAdapter.setUserSearchResultList(userSearchResultList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        contentsQuery.addValueEventListener(postListener);
    }
}