package com.battery.mp_term_project;

import android.app.Activity;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<String> categoryStrings = new ArrayList<String>();

    CategoryAdapter(Context context)
    {
        inflater = LayoutInflater.from(context);
    }

    public void addCategory()
    {
        categoryStrings.add("Test" + categoryStrings.size());
        notifyDataSetChanged();
    }

    public void addCategory(String categoryString)
    {
        categoryStrings.add(categoryString);
        notifyDataSetChanged();
    }

    public void removeCategory(int position)
    {
        categoryStrings.remove(position);
        notifyDataSetChanged();
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
                        //#todo : 편집 기능 추가
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
    }
}
