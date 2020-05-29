/*
 * ChatRepository.java
 *   Go4Lunch
 *
 *   Created by paulleclerc on 5/29/20 3:23 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.repository;

import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paulleclerc.go4lunch.model.ChatMessage;
import com.paulleclerc.go4lunch.model.Workmate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ChatRepository {
    private static final String TAG = ChatRepository.class.getSimpleName();
    private static final String WORKMATES_ARRAY_KEY = "WorkmatesArray";
    private static final String CHAT_KEY = "Chat";
    private static final String MESSAGE_KEY = "message";
    private static final String SENDER_KEY = "sender";
    private static final String DATE_KEY = "date";

    private final Locale locale;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public ChatRepository(Locale locale) {
        this.locale = locale;
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
                .addSnapshotListener((querySnapshot, e) -> {
                    if (querySnapshot == null || e != null) {
                        Log.e(TAG, "fetchMessages: ", e);
                        return;
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

                    listener.onUpdate(chatMessages);
                });
    }

    public void sendChatMessage(Workmate workmate, String message) {
        String userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();

        Map<String, Object> formattedMessage = new HashMap<>();
        formattedMessage.put(MESSAGE_KEY, message);
        formattedMessage.put(SENDER_KEY, userID);
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
