/*
 * ChatRecyclerViewAdapter.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 7/1/20 7:18 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.ui.chat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
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
    private final Context context;

    ChatRecyclerViewAdapter(Workmate workmate, Context context) {
        this.workmate = workmate;
        this.context = context;
    }

    void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflatedView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recyclerview_row_gray, parent, false);
        return new ViewHolder(inflatedView, context);
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
        private final Context context;

        @BindView(R.id.chat_row_message)
        TextView message;
        @BindView(R.id.chat_row_username)
        TextView username;
        @BindView(R.id.chat_row_layout)
        ConstraintLayout chatLayout;
        @BindView(R.id.chat_row_bubble)
        ConstraintLayout chatBubble;

        ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
        }

        void bindChatMessage(ChatMessage chatMessage, Workmate workmate) {
            ButterKnife.bind(this, itemView);

            message.setText(chatMessage.message);

            boolean isUserSender = !chatMessage.senderID.equals(workmate.uid);

            username.setText(isUserSender ? itemView.getContext().getString(R.string.me) :
                    workmate.username);
            username.setTextColor(isUserSender ? Color.RED : ContextCompat.getColor(context,
                    R.color.chat_sender_name));

            Drawable background = chatBubble.getBackground();
            background.setColorFilter(ContextCompat.getColor(context, isUserSender ?
                    R.color.chat_sender : R.color.chat_bubble_gray), PorterDuff.Mode.SRC_IN);

            chatBubble.setBackground(background);

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(chatLayout);
            constraintSet.setGuidelinePercent(R.id.chat_row_guideline_right, isUserSender ? 1f :
                    0.75f);
            constraintSet.setGuidelinePercent(R.id.chat_row_guideline_left, isUserSender ? 0.25f
                    : 0f);
            constraintSet.applyTo(chatLayout);
        }
    }
}
