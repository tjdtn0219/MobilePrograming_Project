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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ContentRecyclerViewAdapter extends RecyclerView.Adapter<ContentRecyclerViewAdapter.ViewHolder> {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private List<ContentRecyclerViewItem> mItemList = null;
    private List<Uri> image_list = null;
    private Context context;
    private User Current_User;

    public ContentRecyclerViewAdapter() {
        mItemList = new ArrayList<>();
    }

    public ContentRecyclerViewAdapter(User user) {
        mItemList = new ArrayList<>();
        Current_User = user;
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
        Log.e("TAGG", "뷰홀더 생성");

        return vh;
    }

    // onBindViewHolder : position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContentRecyclerViewItem item = mItemList.get(position);

        holder.user_img.setImageURI(item.getUser_img());
        holder.user_name.setText(item.getUser_name());
        holder.user_text.setText(item.getContent_text());
        holder.content_time.setText(longTimeToDatetimeAsString(item.getTime()));

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
            getContentURIFromStorage(holder, item, 0);
        }
        else if(item.getImage_list().size() == 2) {
            getContentURIFromStorage(holder, item, 0);
            getContentURIFromStorage(holder, item, 1);
            holder.img3.setVisibility(View.GONE);
        }
        else {
            getContentURIFromStorage(holder, item, 0);
            getContentURIFromStorage(holder, item, 1);
            getContentURIFromStorage(holder, item, 2);
        }

        AdaptLikesButtonState(holder, item);//메인화면 초기 likes버튼 상태 알맞게 초기화

        getProfileURIFromStorage(holder, item);//메인화면 프로필 이미지 출력

        holder.user_img.setOnClickListener(view -> openProfile(view, position, item.getUser_id()));

        holder.user_text.setOnClickListener(view -> openContentDetail(view, position));
        holder.img3.setOnClickListener(view -> openContentDetail(view, position));
        holder.comment_button.setOnClickListener(view -> openContentDetail(view, position));
        holder.like_button.setText(holder.itemView.getResources().getString(R.string.content_likes, item.getLikes()));
        holder.like_button.setOnClickListener(view -> changeLikes(holder, view, position, Current_User));
        holder.like_button2.setOnClickListener(view -> changeLikes(holder, view, position, Current_User));
    }

    private String longTimeToDatetimeAsString(long resultTime)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String formatTime = dateFormat.format(resultTime);
        return formatTime;
    }

    private void getContentURIFromStorage(@NonNull ViewHolder holder, ContentRecyclerViewItem item, int i) {
        String get_content_img_path = "Content_images/" + item.getUser_id() + "/"
                + item.getTime() + "/" + Integer.toString(i) + ".jpg";
        StorageReference pathRef = FirebaseStorage.getInstance().getReference().child(get_content_img_path);
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

    private void getProfileURIFromStorage(@NonNull ViewHolder holder, ContentRecyclerViewItem item) {
        String get_profile_img_path = "Profile_images/" + item.getUser_id() + "/"
                + "profile.jpg";
        StorageReference pathRef = FirebaseStorage.getInstance().getReference().child(get_profile_img_path);
        pathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                    Glide.with(context).load(uri).into(holder.user_img);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) { }
        });
    }

    void openProfile(View view, int position, String uid){
        Context context = view.getContext();
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra("uid", uid);
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
        ContentRecyclerViewItem data = mItemList.get(position);
        List<String> likes_list = user.getLikes_list();
        DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference();

        if (viewHolder.like_button.getVisibility() == View.VISIBLE) {
            //유저가 좋아요를 안누른 상태라면
            Log.e("TAGG", "좋아요 클릭");
            viewHolder.like_button.setVisibility(View.INVISIBLE);
            viewHolder.like_button2.setVisibility(View.VISIBLE);
            likes_list.add(data.getKey());
            likesRef.child("Contents").child(data.getKey()).child("likes").setValue(data.getLikes() + 1);
            likesRef.child("Users").child(user.getUid()).child("likes_list").setValue(likes_list);
            viewHolder.like_button2.setText(viewHolder.itemView.getResources().getString(R.string.content_likes, data.getLikes()+1));
        }
        else {
            //유저가 좋아요를 이미 누른상태라면
            Log.e("TAGG", "좋아요 해제");
            viewHolder.like_button.setVisibility(View.VISIBLE);
            viewHolder.like_button2.setVisibility(View.INVISIBLE);
            likes_list.remove(data.getKey());
            likesRef.child("Contents").child(data.getKey()).child("likes").setValue(data.getLikes() - 1);
            likesRef.child("Users").child(user.getUid()).child("likes_list").setValue(likes_list);
            viewHolder.like_button.setText(viewHolder.itemView.getResources().getString(R.string.content_likes, data.getLikes()));
        }

//        Log.e("TAGGG", Integer.toString(user.getLikes_list().size()));
    }

    void AdaptLikesButtonState(@NonNull ViewHolder holder, ContentRecyclerViewItem item) {
        if(Current_User.getLikes_list() != null) {
            if(Current_User.getLikes_list().size() > 0) {
                Log.e("TAGG", "좋아요 어댑터");
                List<String> likes_list = Current_User.getLikes_list();
                Boolean flag = likes_list.contains(item.getKey());
                if(flag) {
                    holder.like_button.setVisibility(View.INVISIBLE);
                    holder.like_button2.setVisibility(View.VISIBLE);
                    holder.like_button2.setText(holder.itemView.getResources().getString(R.string.content_likes, item.getLikes()));
                }
            }
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
        TextView content_time;
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
            content_time = itemView.findViewById(R.id.content_time);
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