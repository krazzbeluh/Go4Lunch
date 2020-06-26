/*
 * ChatViewModel.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/24/20 11:14 AM.
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

import javax.annotation.Nonnull;

public class ChatViewModel extends AndroidViewModel {
    private final ChatRepository chatService;
    private MutableLiveData<List<ChatMessage>> chatMessages = new MutableLiveData<>();

    public ChatViewModel(@NonNull Application application) {
        super(application);
        this.chatService = new ChatRepository();
    }

    public ChatViewModel(@Nonnull Application application, ChatRepository chatRepository) {
        super(application);
        this.chatService = chatRepository;
    }

    public LiveData<List<ChatMessage>> fetchMessages(Workmate workmate) {
        chatService.fetchMessages(workmate, messages -> this.chatMessages.setValue(messages));
        return chatMessages;
    }

    public void sendMessage(Workmate workmate, String message) {
        chatService.sendChatMessage(workmate, message);
    }
}
