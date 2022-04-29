package com.battery.mp_term_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {

    public class ChatData
    {
        private final boolean isMyMessage;
        private final String message;
        private final String name;

        public ChatData(String name, String message, boolean isMyMessage)
        {
            this.name = name;
            this.message = message;
            this.isMyMessage = isMyMessage;
        }
    }

    private final LayoutInflater inflater;
    private final ArrayList<ChatData> chatDataArrayList = new ArrayList<>();

    ChatMessageAdapter(Context context)
    {
        inflater = LayoutInflater.from(context);
    }

    public void addMyMessage(String myMessage)
    {
        //#todo : 나의 이름 필요
        ChatData chatData = new ChatData("", myMessage, true);
        chatDataArrayList.add(chatData);
        notifyItemInserted(chatDataArrayList.size() - 1);
    }

    public void addOpponentMessage(String opponentMessage)
    {
        //#todo : 상대방의 이름 필요
        ChatData chatData = new ChatData("", opponentMessage, false);
        chatDataArrayList.add(chatData);
        notifyItemInserted(chatDataArrayList.size() - 1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.chat_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(chatDataArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return chatDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View myMessageView;
        View opponentMessageView;

        ViewHolder(View itemView)
        {
            super(itemView);
            myMessageView = itemView.findViewById(R.id.myMessageLayout);
            opponentMessageView = itemView.findViewById(R.id.opponentMessageLayout);
        }

        public void setData(ChatData data)
        {
            TextView messageText;
            if (data.isMyMessage)
            {
                myMessageView.setVisibility(View.VISIBLE);
                opponentMessageView.setVisibility(View.GONE);
                messageText = myMessageView.findViewById(R.id.myMessageText);
            }
            else
            {
                myMessageView.setVisibility(View.GONE);
                opponentMessageView.setVisibility(View.VISIBLE);
                messageText = opponentMessageView.findViewById(R.id.opponentMessageText);

                TextView opponentNameText = opponentMessageView.findViewById(R.id.opponentNameText);
                opponentNameText.setText(data.name);
            }

            messageText.setText(data.message);
        }
    }
}
