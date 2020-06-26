/*
 * ChatViewModelTest.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/24/20 11:14 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.viewmodel;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.paulleclerc.go4lunch.model.ChatMessage;
import com.paulleclerc.go4lunch.model.Workmate;
import com.paulleclerc.go4lunch.ui.chat.ChatViewModel;
import com.paulleclerc.go4lunch.viewmodel.mock.ChatRepositoryFake;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class ChatViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    ChatViewModel viewModel;

    @Mock
    Application application;
    ChatRepositoryFake chatRepository;

    ChatMessage chatMessageForTests = new ChatMessage("message", "senderID", new Date());
    Workmate workmateForTests = new Workmate("uid", "username", "avatarUri", "documentID",
            "chosenRestaurantId", "chosenRestaurantName");

    @Before
    public void setUp() {
        chatRepository = new ChatRepositoryFake();
        viewModel = new ChatViewModel(application, chatRepository);
    }

    @Test
    public void testFetchMessagesShouldReturnChatMessagesSentToCallback() {
        List<ChatMessage> chatMessages = Collections.singletonList(chatMessageForTests);
        chatRepository.addChatMessagesListToQueue(chatMessages);

        assertEquals(chatMessages, viewModel.fetchMessages(workmateForTests).getValue());
    }

    @Test
    public void testSendMessageShouldSendMessageToRepository() {
        String message = "message";

        assertNull(chatRepository.sentMessage);

        viewModel.sendMessage(workmateForTests, message);

        assertEquals(message, chatRepository.sentMessage);
    }
}
