package com.battery.mp_term_project;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.ViewHolder> {

    private List<UserSearchResult> userSearchResultList;

    public static class UserSearchResult
    {
        Uri profileImageUri;
        String userName;
        String profileText;
        String uid;
        UserSearchResult(Uri profileImageUri, String userName, String profileText, String uid)
        {
            this.profileImageUri = profileImageUri;
            this.userName = userName;
            this.profileText = profileText;
            this.uid = uid;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.profile_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserSearchResult result = userSearchResultList.get(position);
        holder.profileImage.setImageURI(result.profileImageUri);
        holder.profileUserName.setText(result.userName);
        holder.profileText.setText(result.profileText);

        holder.itemView.setOnClickListener(view -> openProfile(view, position, result.uid));
            //#todo : 프로필 이동 추가
    }

     void openProfile(View view, int position, String uid){
        Context context = view.getContext();
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra("uid", uid);
        context.startActivity(intent);

    }

    @Override
    public int getItemCount() {
        return userSearchResultList.size();
    }

    public void setUserSearchResultList(List<UserSearchResult> userSearchResultList)
    {
        this.userSearchResultList = userSearchResultList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView profileUserName;
        TextView profileText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            profileUserName = itemView.findViewById(R.id.profileUserName);
            profileText = itemView.findViewById(R.id.profileText);
        }
    }
}
