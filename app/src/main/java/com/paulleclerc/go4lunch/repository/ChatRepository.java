/*
 * ChatRepository.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/18/20 3:07 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.repository;

import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.paulleclerc.go4lunch.model.ChatMessage;
import com.paulleclerc.go4lunch.model.Workmate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRepository {
    private static final String TAG = ChatRepository.class.getSimpleName();
    public static final String WORKMATES_ARRAY_KEY = "WorkmatesArray";
    public static final String CHAT_KEY = "Chat";
    private static final String MESSAGE_KEY = "message";
    private static final String SENDER_KEY = "sender";
    private static final String DATE_KEY = "date";

    private final FirebaseFirestore db;
    private final FirebaseAuth auth;

    public ChatRepository() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public ChatRepository(FirebaseFirestore db, FirebaseAuth auth) {
        this.db = db;
        this.auth = auth;
    }

    public void fetchMessages(Workmate workmate, OnChatUpdateListener listener) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        Date lastMonth = c.getTime();

        db.collection(WORKMATES_ARRAY_KEY)
                .document(workmate.getDocumentID())
                .collection(CHAT_KEY)
                .whereGreaterThan("date", lastMonth)
                .addSnapshotListener((querySnapshot, e) -> listener.onUpdate(getChatMessages(querySnapshot, e)));
    }

    public List<ChatMessage> getChatMessages(QuerySnapshot querySnapshot, Exception e) {
        if (querySnapshot == null || e != null) {
            Log.e(TAG, "fetchMessages: ", e);
            return null;
        }

        List<ChatMessage> chatMessages = new ArrayList<>();
        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
            String message = document.getString("message");
            String sender = document.getString("sender");
            Timestamp date = (Timestamp) document.get("date");

            if (message != null && sender != null && date != null) {
                chatMessages.add(new ChatMessage(message, sender, date.toDate()));
            }
        }

        return chatMessages;
    }

    public void sendChatMessage(Workmate workmate, String message) {
        Map<String, Object> formattedMessage = new HashMap<>();
        formattedMessage.put(MESSAGE_KEY, message);
        formattedMessage.put(SENDER_KEY, auth.getUid());
        formattedMessage.put(DATE_KEY, FieldValue.serverTimestamp());

        db.collection(WORKMATES_ARRAY_KEY)
                .document(workmate.getDocumentID())
                .collection(CHAT_KEY)
                .document()
                .set(formattedMessage)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.e(TAG, "sendChatMessage: ", task.getException());
                    }
                });
    }

    public interface OnChatUpdateListener {
        void onUpdate(List<ChatMessage> messages);
    }
}
