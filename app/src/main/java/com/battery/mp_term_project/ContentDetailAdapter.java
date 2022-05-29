package com.battery.mp_term_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ContentDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private final LayoutInflater inflater;
    private final ArrayList<ContentDetailData> dataArrayList = new ArrayList<>();

    public static class ContentDetailData
    {
        String contentId;
        String userName;
        String content;
        int imageCount;
        int likes;

        ContentDetailData(String contentId, String userName, String content)
        {
            this.contentId = contentId;
            this.userName = userName;
            this.content = content;
        }

        ContentDetailData(String contentId, String userName, String content, int imageCount, int likes)
        {
            this(contentId, userName, content);
            this.imageCount = imageCount;
            this.likes = likes;
        }
    }

    public enum ItemViewType
    {
        Content(0),
        Comment(1);

        private final int value;
        private ItemViewType(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        return Math.min(position, ItemViewType.Comment.getValue());
    }

    ContentDetailAdapter(Context context)
    {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (ItemViewType.values()[viewType])
        {
            case Content:
                return new ContentViewHolder(inflater.inflate(R.layout.contentdetail_content, parent, false));
            case Comment:
                return new CommentViewHolder(inflater.inflate(R.layout.contentdetail_comment, parent, false));
            default:
                return new NullViewHolder(inflater.inflate(R.layout.contentdetail_comment, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        switch (ItemViewType.values()[holder.getItemViewType()])
        {
            case Content:
                ContentViewHolder contentViewHolder = (ContentViewHolder)holder;
                contentViewHolder.setData(dataArrayList.get(position));
                break;
            case Comment:
                CommentViewHolder commentViewHolder = (CommentViewHolder)holder;
                commentViewHolder.setData(dataArrayList.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public void addData(ContentDetailData data)
    {
        dataArrayList.add(data);
        notifyItemInserted(dataArrayList.size() - 1);
    }

    public void setData(int index, ContentDetailData data)
    {
        boolean haveToAppend = index >= dataArrayList.size();
        while (index >= dataArrayList.size())
        {
            dataArrayList.add(null);
        }

        dataArrayList.set(index, data);
        if(haveToAppend) {
            notifyDataSetChanged();
        }
        else {
            notifyItemChanged(index);
        }
    }

    class ContentViewHolder extends RecyclerView.ViewHolder
    {
        TextView userNameText;
        ImageButton userProfileImageButton;
        LinearLayout imageLinearLayout;
        TextView contentText;
        TextView likeText;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameText = itemView.findViewById(R.id.contentUserNameText);
            userProfileImageButton = itemView.findViewById(R.id.userProfileImageButton);
            imageLinearLayout = itemView.findViewById(R.id.imageLinearLayout);
            contentText = itemView.findViewById(R.id.contentText);
            likeText = itemView.findViewById(R.id.likeText);
        }

        public void setData(ContentDetailData data)
        {
            if (data == null) {
                return;
            }

            userNameText.setText(data.userName);
            LoadImagesFromStorage(data);
            contentText.setText(data.content);
            likeText.setText(itemView.getResources().getString(R.string.content_likes, data.likes));
            likeText.setOnClickListener(view -> {
                //#todo : 좋아요 추가/제거
            });
        }

        private void LoadImagesFromStorage(ContentDetailData data) {
            for(int i = 0; i < data.imageCount; i++) {
                String[] ids = data.contentId.split("_");
                String imagePath = "Content_images/" + ids[0] + "/"
                        + ids[1] + "/" + i + ".jpg";

                final int currentIndex = i;
                StorageReference pathRef = FirebaseStorage.getInstance().getReference().child(imagePath);
                pathRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    while (currentIndex >= imageLinearLayout.getChildCount())
                    {
                        imageLinearLayout.addView(new ImageView(itemView.getContext()));
                    }

                    ImageView imageView = (ImageView) imageLinearLayout.getChildAt(currentIndex);
                    imageView.setMaxHeight(300);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(10, 0, 10, 0);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setAdjustViewBounds(true);

                    Glide.with(itemView.getContext()).load(uri).into(imageView);
                }).addOnFailureListener(e -> {
                });
            }
        }
    }

    class CommentViewHolder extends RecyclerView.ViewHolder
    {
        TextView userNameText;
        ImageButton userProfileImageButton;
        TextView contentText;

        public CommentViewHolder(@NonNull View itemView)
        {
            super(itemView);
            userNameText = itemView.findViewById(R.id.profileUserName);
            userProfileImageButton = itemView.findViewById(R.id.profileImageButton);
            contentText = itemView.findViewById(R.id.profileText);
        }

        public void setData(ContentDetailData data)
        {
            userNameText.setText(data.userName);
            //#todo : 댓글에도 이미지 추가가 필요하다면...
            contentText.setText(data.content);
        }
    }

    class NullViewHolder extends RecyclerView.ViewHolder
    {
        public NullViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
