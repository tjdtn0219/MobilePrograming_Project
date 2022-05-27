package com.battery.mp_term_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final ArrayList<String> categoryStrings = new ArrayList<String>();

    CategoryAdapter(Context context)
    {
        inflater = LayoutInflater.from(context);
    }

    public void addCategory()
    {
        addCategory("Test" + categoryStrings.size());
    }

    public void addCategory(String categoryString)
    {
        categoryStrings.add(categoryString);
        notifyItemInserted(categoryStrings.size() - 1);
    }

    public void editCategory(String newCategoryString, int position)
    {
        categoryStrings.set(position, newCategoryString);
        notifyItemChanged(position);
    }

    public void removeCategory(int position)
    {
        categoryStrings.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.main_category_button, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.categoryButton.setText(categoryStrings.get(position));
    }

    @Override
    public int getItemCount() {
        return categoryStrings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        Button categoryButton;

        ViewHolder(View itemView)
        {
            super(itemView);
            categoryButton = itemView.findViewById(R.id.categoryButton);
            categoryButton.setOnClickListener((view) -> {
                Context context = itemView.getContext();
                Intent intent = new Intent(context, SearchActivity.class);
                intent.putExtra("Keyword", categoryButton.getText());
                context.startActivity(intent);
            });
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
        {
            MenuInflater menuInflater = ((Activity)v.getContext()).getMenuInflater();
            menuInflater.inflate(R.menu.context_category, menu);
            for (int i = 0; i < menu.size(); i++)
            {
                MenuItem item = menu.getItem(i);

                // recyclerView에서 contextMenu를 이용하여 삭제하기 위한 Hack
                item.setOnMenuItemClickListener((MenuItem menuItem) -> {
                    int itemId = menuItem.getItemId();
                    if (itemId == R.id.item_category_edit)
                    {
                        createEditCategoryDialog(v, getAdapterPosition());
                        return true;
                    }
                    else if (itemId == R.id.item_category_delete)
                    {
                        removeCategory(getAdapterPosition());
                        return true;
                    }

                    return false;
                });
            }
        }

        private void createEditCategoryDialog(View v, int adapterPosition)
        {
            View dialogView = inflater.inflate(R.layout.main_create_category_dialog, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setView(dialogView);
            AlertDialog alertDialog = builder.create();

            EditText categoryName = dialogView.findViewById(R.id.categoryName);
            Button confirmButton = dialogView.findViewById(R.id.confirmButton);
            confirmButton.setOnClickListener(view -> {
                editCategory(categoryName.getText().toString(), adapterPosition);
                alertDialog.dismiss();
            });

            Button cancelButton = dialogView.findViewById(R.id.cancelButton);
            cancelButton.setOnClickListener(view -> {
                alertDialog.cancel();
            });

            alertDialog.show();
        }
    }
}
