/*
 * ChatRepository.java
 *   Go4Lunch
 *
 *   Created by paulleclerc on 5/29/20 11:36 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.repository;

import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paulleclerc.go4lunch.model.ChatMessage;
import com.paulleclerc.go4lunch.model.Workmate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ChatRepository {
    private static final String TAG = ChatRepository.class.getSimpleName();
    private final Locale locale;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ChatRepository(Locale locale) {
        this.locale = locale;
    }

    public void fetchMessages(Workmate workmate, OnChatUpdateListener listener) {
        Calendar calendar = Calendar.getInstance(locale);
        int month = calendar.get(Calendar.MONTH - 1);
        int year = calendar.get(Calendar.YEAR);
        month--;
        if (month < 1) {
            month = 12;
            year--;
        }
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);

        db.collection("WorkmatesArray")
                .document(workmate.getDocumentID())
                .collection("Chat")
                //.whereGreaterThan("date", calendar.getTime())
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

    public interface OnChatUpdateListener {
        void onUpdate(List<ChatMessage> messages);
    }
}
