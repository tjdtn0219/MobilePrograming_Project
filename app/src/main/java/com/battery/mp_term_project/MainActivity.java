package com.battery.mp_term_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private LayoutInflater inflater;

    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        Intent intent = getIntent();LoginActivity에서 startActivityForResult, putExtra 한 거 받기
//        String nickName = intent.getStringExtra("nickname");LoginActivity에서 가져옴
//        String photoURL = intent.getStringExtra("photoURL");LoginActivity에서 가져옴

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ImageButton addCategoryButton = findViewById(R.id.addCategoryButton);
        addCategoryButton.setOnClickListener(view -> {
            createAddCategoryDialog();
        });

        RecyclerView categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        categoryAdapter = new CategoryAdapter(this);
        categoryRecyclerView.setAdapter(categoryAdapter);

        addContent();
        addContent();
        addContent();
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
            startActivity(intent);
        }

        return false;
    }

    private void addContent()
    {
        LinearLayout container = findViewById(R.id.contentscontainer);

        inflater.inflate(R.layout.content, container, true);
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
            categoryAdapter.addCategory(categoryName.getText().toString());
            alertDialog.dismiss();
        });

        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(view -> {
            alertDialog.cancel();
        });

        alertDialog.show();
    }
}