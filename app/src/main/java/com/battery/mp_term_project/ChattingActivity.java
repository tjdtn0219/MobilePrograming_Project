package com.battery.mp_term_project;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class ChattingActivity extends AppCompatActivity {

    LinearLayout chatLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        chatLinearLayout = findViewById(R.id.chatLinearLayout);

        addMyMessage("안녕하세요. 테스트 메시지입니다.");
        addOpponentMessage("안녕하세요.");
    }

    public void addMyMessage(String message)
    {
        ChatMessageView myMessageView = new ChatMessageView(this);
        myMessageView.setMyMessage(message);
        
        // 내 메시지는 오른쪽에 나타나도록
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.RIGHT;
        myMessageView.setLayoutParams(params);

        chatLinearLayout.addView(myMessageView);
    }

    public void addOpponentMessage(String message)
    {
        ChatMessageView opponentMessageView = new ChatMessageView(this);
        opponentMessageView.setOpponentMessage(message);

        // 상대 메시지는 왼쪽에 나타나도록
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.LEFT;
        opponentMessageView.setLayoutParams(params);

        chatLinearLayout.addView(opponentMessageView);
    }
}
