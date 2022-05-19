package com.battery.mp_term_project;

import android.content.Context;
import android.net.Uri;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContentRecyclerViewAdapter extends RecyclerView.Adapter<ContentRecyclerViewAdapter.ViewHolder> {
    private List<ContentRecyclerViewItem> mItemList = null;

    public ContentRecyclerViewAdapter(List<ContentRecyclerViewItem> mItemList) {
        this.mItemList = mItemList;
    }

    // onCreateViewHolder : 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.content, parent, false);
        ContentRecyclerViewAdapter.ViewHolder vh = new ContentRecyclerViewAdapter.ViewHolder(view);

        return vh;
    }

    // onBindViewHolder : position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        ContentRecyclerViewItem item = mItemList.get(getItemCount()-1-position);
        ContentRecyclerViewItem item = mItemList.get(position);

        holder.user_img.setImageURI(item.getUser_img());
        holder.user_name.setText(item.getUser_name());
        holder.user_text.setText(item.getUser_text());
        if(item.getImage_list() == null) {
            holder.img_layout1.setVisibility(View.GONE);
        }
        else if(item.getImage_list().size() == 1) {
            holder.img1.setImageURI(item.getImage_list().get(0));
            holder.img_layout2.setVisibility(View.GONE);
        }
        else if(item.getImage_list().size() == 2) {
            holder.img1.setImageURI(item.getImage_list().get(0));
            holder.img2.setImageURI(item.getImage_list().get(1));
            holder.img_layout2.setVisibility(View.GONE);
        }
        else {
            holder.img1.setImageURI(item.getImage_list().get(0));
            holder.img2.setImageURI(item.getImage_list().get(1));
            holder.img3.setImageURI(item.getImage_list().get(2));
        }

        holder.user_text.setOnClickListener(view -> openContentDetail(view, position));
        holder.img3.setOnClickListener(view -> openContentDetail(view, position));
        holder.comment_button.setOnClickListener(view -> openContentDetail(view, position));
    }

    void openContentDetail(View view, int position)
    {
        Context context = view.getContext();
        Intent intent = new Intent(context, ContentDetailActivity.class);
        //#todo : 추후에는 database에서 데이터를 받아오게 하는 것으로 충분할듯
        ContentRecyclerViewItem data = mItemList.get(position);
        intent.putExtra("userName", data.getUser_name());
        intent.putExtra("userText", data.getUser_text());
        context.startActivity(intent);
    }

    // getItemCount : 전체 데이터의 개수를 리턴
    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
       ImageView user_img;
       TextView user_name;
       TextView user_text;
       LinearLayout img_layout1;
       LinearLayout img_layout2;
       ImageView img1;
       ImageView img2;
       ImageView img3;
       Button comment_button;

        public ViewHolder(View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조
            user_img = itemView.findViewById(R.id.user_img);
            user_name = itemView.findViewById(R.id.user_name);
            user_text = itemView.findViewById(R.id.user_text);
            img_layout1 = itemView.findViewById(R.id.img_layout1);
            img_layout2 = itemView.findViewById(R.id.img_layout2);
            img1 = itemView.findViewById(R.id.img1);
            img2 = itemView.findViewById(R.id.img2);
            img3 = itemView.findViewById(R.id.img3);
            comment_button = itemView.findViewById(R.id.comment_button);
        }
    }
}