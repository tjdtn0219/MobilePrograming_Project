package com.battery.mp_term_project;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ChattingActivity extends AppCompatActivity {

    private ChatMessageAdapter chatMessageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        //#todo : 상대방의 이름 추가
        setTitle(R.string.chatting_titlePlaceholder);

        RecyclerView chattingRecyclerView = findViewById(R.id.chattingRecyclerView);
        chatMessageAdapter = new ChatMessageAdapter(this);
        chattingRecyclerView.setAdapter(chatMessageAdapter);

        //#todo : 채팅 데이터 가져와서 적용
        for (int i = 0; i < 25; i++)
        {
            chatMessageAdapter.addMyMessage("안녕하세요.\n테스트 메시지입니다." + i);
            chatMessageAdapter.addOpponentMessage("안녕하세요." + i);
            chatMessageAdapter.addMyMessage("이것은 긴 테스트 메시지입니다. 길게 좀 쳐보겠습니다~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            chatMessageAdapter.addOpponentMessage("상대방에서도 긴 테스트 메시지를 쳐보겠습니다~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
    }
}
