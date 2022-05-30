package com.battery.mp_term_project;

import android.content.Context;
import android.net.Uri;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ContentRecyclerViewAdapter extends RecyclerView.Adapter<ContentRecyclerViewAdapter.ViewHolder> {
    private List<ContentRecyclerViewItem> mItemList = null;
    private List<Uri> image_list = null;
    private Context context;
    private User Current_User;

    public ContentRecyclerViewAdapter() {
        mItemList = new ArrayList<>();
    }

    public ContentRecyclerViewAdapter(List<ContentRecyclerViewItem> mItemList, User user) {
        setItemList(mItemList);
        Current_User = user;
//        Log.e("TAG" ,Current_User.getUid());
    }

    public void setItemList(List<ContentRecyclerViewItem> mItemList) {
        this.mItemList = mItemList;
        notifyDataSetChanged();
    }


    // onCreateViewHolder : 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
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
        holder.user_text.setText(item.getContent_text());

        holder.img_layout1.setVisibility(View.VISIBLE);
        holder.img_layout2.setVisibility(View.VISIBLE);

        holder.img1.setVisibility(View.INVISIBLE);
        holder.img2.setVisibility(View.INVISIBLE);
        holder.img3.setVisibility(View.INVISIBLE);

        if(item.getImage_list() == null) {
            holder.img_layout1.setVisibility(View.GONE);
        }
        else if(item.getImage_list().size() == 1) {
            holder.img_layout2.setVisibility(View.GONE);
            getURIFromStorage(holder, item, 0);
        }
        else if(item.getImage_list().size() == 2) {
            getURIFromStorage(holder, item, 0);
            getURIFromStorage(holder, item, 1);
            holder.img3.setVisibility(View.GONE);
        }
        else {
            getURIFromStorage(holder, item, 0);
            getURIFromStorage(holder, item, 1);
            getURIFromStorage(holder, item, 2);
        }

        AdaptLikesButtonState(holder, item);//메인화면 초기 likes버튼 상태 알맞게 초기화

        holder.user_img.setOnClickListener(view -> openProfile(view, position));

        holder.user_text.setOnClickListener(view -> openContentDetail(view, position));
        holder.img3.setOnClickListener(view -> openContentDetail(view, position));
        holder.comment_button.setOnClickListener(view -> openContentDetail(view, position));
        holder.like_button.setText(holder.itemView.getResources().getString(R.string.content_likes, item.getLikes()));
        holder.like_button.setOnClickListener(view -> changeLikes(holder, view, position, Current_User));
    }

    private void getURIFromStorage(@NonNull ViewHolder holder, ContentRecyclerViewItem item, int i) {
        String get_path = "Content_images/" + item.getUser_id() + "/"
                + item.getTime() + "/" + Integer.toString(i) + ".jpg";
        StorageReference pathRef = FirebaseStorage.getInstance().getReference().child(get_path);
        pathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (i == 0) {
                    Glide.with(context).load(uri).into(holder.img1);
                    holder.img1.setVisibility(View.VISIBLE);
                }
                else if (i == 1) {
                    Glide.with(context).load(uri).into(holder.img2);
                    holder.img2.setVisibility(View.VISIBLE);
                }
                else if (i == 2) {
                    Glide.with(context).load(uri).into(holder.img3);
                    holder.img3.setVisibility(View.VISIBLE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) { }
        });
    }

    void openProfile(View view, int position){
        Context context = view.getContext();
        Intent intent = new Intent(context, ProfileActivity.class);
        context.startActivity(intent);

    }

    void openContentDetail(View view, int position)
    {
        Context context = view.getContext();
        Intent intent = new Intent(context, ContentDetailActivity.class);
        ContentRecyclerViewItem data = mItemList.get(position);
        intent.putExtra("ContentKey", data.getKey());
        context.startActivity(intent);
    }

    void changeLikes(@NonNull ViewHolder viewHolder, View view, int position, User user)
    {
        //#todo : 유저의 좋아요 상황에 따라 다르게...
        viewHolder.like_button.setVisibility(View.INVISIBLE);
        viewHolder.like_button2.setVisibility(View.VISIBLE);
        ContentRecyclerViewItem data = mItemList.get(position);
        List<String> likes_list = user.getLikes_list();
        likes_list.add(data.getKey());
        DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference();
        likesRef.child("Contents").child(data.getKey()).child("likes").setValue(data.getLikes() + 1);
        likesRef.child("Users").child(user.getUid()).child("likes_list").setValue(likes_list);
        viewHolder.like_button2.setText(viewHolder.itemView.getResources().getString(R.string.content_likes, data.getLikes() + 1));
//        Log.e("TAGGG", Integer.toString(user.getLikes_list().size()));
    }

    void AdaptLikesButtonState(@NonNull ViewHolder holder, ContentRecyclerViewItem item) {
        List<String> likes_list = Current_User.getLikes_list();
        Boolean flag = likes_list.contains(item.getKey());
        if(flag) {
            holder.like_button.setVisibility(View.INVISIBLE);
            holder.like_button2.setVisibility(View.VISIBLE);
            holder.like_button2.setText(holder.itemView.getResources().getString(R.string.content_likes, item.getLikes()));
        }
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
        Button like_button;
        Button like_button2;

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
            like_button = itemView.findViewById(R.id.like_button);
            like_button2 = itemView.findViewById(R.id.like_button2);
        }
    }
}