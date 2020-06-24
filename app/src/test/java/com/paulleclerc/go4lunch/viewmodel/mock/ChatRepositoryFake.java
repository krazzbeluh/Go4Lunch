/*
 * ChatRepositoryFake.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/24/20 11:14 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.viewmodel.mock;

import com.paulleclerc.go4lunch.model.ChatMessage;
import com.paulleclerc.go4lunch.model.Workmate;
import com.paulleclerc.go4lunch.repository.ChatRepository;

import java.util.ArrayList;
import java.util.List;

public class ChatRepositoryFake extends ChatRepository {
    public String sentMessage;
    List<List<ChatMessage>> chatMessagesListQueue = new ArrayList<>();

    public ChatRepositoryFake() {
        super(null, null);
    }

    public void addChatMessagesListToQueue(List<ChatMessage> chatMessages) {
        chatMessagesListQueue.add(chatMessages);
    }

    @Override
    public void fetchMessages(Workmate workmate, OnChatUpdateListener listener) {
        listener.onUpdate(chatMessagesListQueue.remove(0));
    }

    @Override
    public void sendChatMessage(Workmate workmate, String message) {
        sentMessage = message;
    }
}
