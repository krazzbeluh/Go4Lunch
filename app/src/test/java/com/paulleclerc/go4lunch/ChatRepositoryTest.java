/*
 * ChatRepositoryTest.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/18/20 3:07 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.paulleclerc.go4lunch.repository.ChatRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChatRepositoryTest {
    ChatRepository chatRepository;

    @Mock
    FirebaseAuth auth;
    @Mock
    FirebaseFirestore db;

    @Mock
    QuerySnapshot querySnapshot;
    @Mock
    FirebaseFirestoreException exception;
    @Mock
    DocumentSnapshot document;

    @Before
    public void setUp() {
        chatRepository = new ChatRepository(db, auth);
    }

    @Test
    public void testGetChatMessagesShouldReturnNullIfFailure() {
        assertNull(chatRepository.getChatMessages(querySnapshot, exception));
    }

    @Test
    public void testGetChatMessagesShouldReturnNullIfSnapshotNull() {
        assertNull(chatRepository.getChatMessages(null, null));
    }

    @Test
    public void testGetChatMessagesShouldEmptyListIfEmptySnapshot() {
        when(querySnapshot.getDocuments()).thenReturn(new ArrayList<>());
        assertEquals(chatRepository.getChatMessages(querySnapshot, null), new ArrayList<>());
    }

    @Test
    public void testGetChatMessagesShouldReturnListIfEmptySnapshot() {
        String message = "message";
        String sender = "sender";
        String date = "date";

        when(querySnapshot.getDocuments()).thenReturn(Collections.singletonList(document));
        when(document.getString(message)).thenReturn(null);
        when(document.getString(sender)).thenReturn(null);
        when(document.get(date)).thenReturn(null);

        assertEquals(chatRepository.getChatMessages(querySnapshot, null), new ArrayList<>());
    }

    @Test
    public void testGetChatMessagesShouldReturnListIfValidSnapshot() {
        String message = "message";
        String sender = "sender";
        String date = "date";
        Date messageDate = new Date();

        when(querySnapshot.getDocuments()).thenReturn(Collections.singletonList(document));
        when(document.getString(message)).thenReturn(message);
        when(document.getString(sender)).thenReturn(sender);
        when(document.get(date)).thenReturn(new Timestamp(messageDate));

        assertEquals(chatRepository.getChatMessages(querySnapshot, null).size(), 1);
        assertEquals(chatRepository.getChatMessages(querySnapshot, null).get(0).message, message);
        assertEquals(chatRepository.getChatMessages(querySnapshot, null).get(0).senderID, sender);
        assertEquals(chatRepository.getChatMessages(querySnapshot, null).get(0).date, messageDate);
    }
}
