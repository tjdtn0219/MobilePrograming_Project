package com.battery.mp_term_project;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class ChatMessageView extends ConstraintLayout {

    LayoutInflater inflater;
    View opponentChatView;
    View myChatView;

    public ChatMessageView(Context context) {
        super(context);
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setOpponentMessage(String message)
    {
        opponentChatView = inflater.inflate(R.layout.chat_opponent_message, null);
        addView(opponentChatView);
        TextView messageText = findViewById(R.id.opponentMessageText);
        messageText.setText(message);
    }

    public void setMyMessage(String message)
    {
        myChatView = inflater.inflate(R.layout.chat_my_message, null);
        addView(myChatView);
        TextView messageText = findViewById(R.id.myMessageText);
        messageText.setText(message);
    }
}
