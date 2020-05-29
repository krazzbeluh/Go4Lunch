/*
 * ChatViewModel.java
 *   Go4Lunch
 *
 *   Created by paulleclerc on 5/29/20 3:25 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.ui.chat;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.paulleclerc.go4lunch.model.ChatMessage;
import com.paulleclerc.go4lunch.model.Workmate;
import com.paulleclerc.go4lunch.repository.ChatRepository;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {
    private ChatRepository chatService = new ChatRepository(getApplication().getApplicationContext().getResources().getConfiguration().locale);
    private MutableLiveData<List<ChatMessage>> chatMessages = new MutableLiveData<>();

    public ChatViewModel(@NonNull Application application) {
        super(application);
    }

    LiveData<List<ChatMessage>> fetchMessages(Workmate workmate) {
        chatService.fetchMessages(workmate, messages -> this.chatMessages.setValue(messages));
        return chatMessages;
    }

    void sendMessage(Workmate workmate, String message) {
        chatService.sendChatMessage(workmate, message);
    }
}
