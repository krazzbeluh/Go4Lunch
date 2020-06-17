/*
 * ChatRecyclerViewAdapter.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/17/20 10:09 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.ui.chat;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.paulleclerc.go4lunch.R;
import com.paulleclerc.go4lunch.model.ChatMessage;
import com.paulleclerc.go4lunch.model.Workmate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {
    private final Workmate workmate;
    private List<ChatMessage> chatMessages = new ArrayList<>();

    ChatRecyclerViewAdapter(Workmate workmate) {
        this.workmate = workmate;
    }

    void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recyclerview_row_gray, parent, false);
        return new ViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindChatMessage(chatMessages.get(position), workmate);
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chat_row_message)
        TextView message;
        @BindView(R.id.chat_row_username)
        TextView username;
        @BindView(R.id.chat_row_layout)
        ConstraintLayout chatLayout;
        @BindView(R.id.chat_row_bubble)
        ConstraintLayout chatBubble;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bindChatMessage(ChatMessage chatMessage, Workmate workmate) {
            ButterKnife.bind(this, itemView);

            message.setText(chatMessage.message);

            if (chatMessage.senderID.equals(workmate.uid)) username.setText(workmate.username);
            else {
                username.setText(itemView.getContext().getString(R.string.me));
                username.setTextColor(Color.RED);

                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(chatLayout);
                constraintSet.setGuidelinePercent(R.id.chat_row_guideline, 0.25f);
                constraintSet.connect(R.id.chat_row_bubble, ConstraintSet.START, R.id.guideline, ConstraintSet.END, 0);
                constraintSet.connect(R.id.chat_row_bubble, ConstraintSet.END, R.id.chat_row_layout, ConstraintSet.END, 0);
                constraintSet.applyTo(chatLayout);
            }
        }
    }
}
