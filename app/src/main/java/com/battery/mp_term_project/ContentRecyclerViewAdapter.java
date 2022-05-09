package com.battery.mp_term_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContentRecyclerViewAdapter extends RecyclerView.Adapter<ContentRecyclerViewAdapter.ViewHolder> {
    private List<ContentRecyclerViewItem> mItemList;

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
        ContentRecyclerViewItem item = mItemList.get(position);
        holder.user_img.setImageURI(item.getUser_img());
        holder.user_name.setText(item.getUser_name());
        holder.user_text.setText(item.getUser_text());
        holder.img1.setImageURI(item.getImg1());
        holder.img2.setImageURI(item.getImg2());
        holder.img3.setImageURI(item.getImg3());

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
       ImageView img1;
       ImageView img2;
       ImageView img3;

        public ViewHolder(View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조
            user_img = itemView.findViewById(R.id.user_img);
            user_name = itemView.findViewById(R.id.user_name);
            user_text = itemView.findViewById(R.id.user_text);
            img1 = itemView.findViewById(R.id.img1);
            img2 = itemView.findViewById(R.id.img2);
            img3 = itemView.findViewById(R.id.img3);
        }
    }
}