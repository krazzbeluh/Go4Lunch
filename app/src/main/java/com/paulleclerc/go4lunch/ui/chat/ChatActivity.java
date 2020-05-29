/*
 * ChatActivity.java
 *   Go4Lunch
 *
 *   Created by paulleclerc on 5/29/20 3:23 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.ui.chat;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.paulleclerc.go4lunch.R;
import com.paulleclerc.go4lunch.model.Workmate;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class ChatActivity extends AppCompatActivity {
    @BindView(R.id.chat_workmate_avatar)
    ImageView avatar;
    @BindView(R.id.chat_workmate_username)
    TextView username;
    @BindView(R.id.chat_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.chat_message_edittext)
    EditText messageEditText;
    @BindView(R.id.chat_send_button)
    ImageButton sendButton;

    private ChatViewModel viewModel;
    private Workmate workmate;
    private LinearLayoutManager linearLayoutManager;
    private ChatRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        workmate = (Workmate) getIntent().getSerializableExtra(getString(R.string.workmate_serializable_key));

        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        ButterKnife.bind(this);

        sendButton.setEnabled(false);

        Glide.with(this).load(workmate.avatarUri).into(avatar);
        username.setText(workmate.username);

        linearLayoutManager = new LinearLayoutManager(this);
        adapter = new ChatRecyclerViewAdapter(workmate);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        viewModel.fetchMessages(workmate).observe(this, chatMessages -> {
            if (chatMessages == null) return;
            adapter.setChatMessages(chatMessages);
            if (chatMessages.size() > 0)
                linearLayoutManager.scrollToPosition(chatMessages.size() - 1);
        });
    }

    @OnTextChanged(value = R.id.chat_message_edittext, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onTextChanged(CharSequence text) {
        if (text.length() > 0) sendButton.setEnabled(true);
        else sendButton.setEnabled(false);
    }

    @OnClick(R.id.chat_send_button)
    public void sendMessage() {
        String message = messageEditText.getText().toString();
        if (message.length() > 0) {
            viewModel.sendMessage(workmate, message);
            messageEditText.setText("");
        }
    }
}
