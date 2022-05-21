package com.battery.mp_term_project;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContentDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private final LayoutInflater inflater;
    private final ArrayList<ContentDetailData> dataArrayList = new ArrayList<>();

    public static class ContentDetailData
    {
        String userName;
        String content;
        ArrayList<Uri> imageUris;

        ContentDetailData(String userName, String content)
        {
            this.userName = userName;
            this.content = content;
            imageUris = new ArrayList<>();
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

    class ContentViewHolder extends RecyclerView.ViewHolder
    {
        TextView userNameText;
        ImageButton userProfileImageButton;
        ScrollView imageScrollView;
        TextView contentText;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameText = itemView.findViewById(R.id.contentUserNameText);
            userProfileImageButton = itemView.findViewById(R.id.userProfileImageButton);
            imageScrollView = itemView.findViewById(R.id.imageScrollView);
            contentText = itemView.findViewById(R.id.contentText);
        }

        public void setData(ContentDetailData data)
        {
            userNameText.setText(data.userName);
            for (Uri imageUri : data.imageUris)
            {
                ImageView imageView = new ImageView(itemView.getContext());
                imageView.setImageURI(imageUri);
                imageScrollView.addView(imageView);
            }
            contentText.setText(data.content);
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
