/*
 * WorkmatesRepository.java
 *   Go4Lunch
 *
 *   Created by paulleclerc on 5/29/20 11:36 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.repository;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.paulleclerc.go4lunch.closures.FetchWorkmatesCompletion;
import com.paulleclerc.go4lunch.model.Workmate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkmatesRepository {
    private static final String TAG = WorkmatesRepository.class.getSimpleName();
    private static final String WORKMATE_KEY = "workmates";
    private static final String WORKMATES_ARRAY_KEY = "WorkmatesArray";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final AuthRepository auth = new AuthRepository();
    private final FirStorageRepository storage = new FirStorageRepository();

    public void fetchWorkmates(FetchWorkmatesCompletion completion) {
        db.collection(WORKMATES_ARRAY_KEY)
                .whereArrayContains(WORKMATE_KEY, auth.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshot = task.getResult();
                        assert snapshot != null;
                        List<DocumentSnapshot> documents = snapshot.getDocuments();
                        getWorkmatesInfos(documents, completion::onComplete);
                        /*List<String[]> workmatesID = new ArrayList<>();
                        for (DocumentSnapshot document : documents) {
                            List<String> associatedUsers = (List<String>) document.get(context.getString(R.string.workmates_field));
                            if (associatedUsers == null) break;
                            workmatesID.add(new String[]{!associatedUsers.get(0).equals(auth.getUid()) ? associatedUsers.get(0) : associatedUsers.get(1), document.getId()});
                        }

                        getWorkmatesInfos(workmatesID, (success, workmates) -> {
                            if (success) completion.onComplete(true, workmates);
                            else completion.onComplete(false, null);
                        });*/
                    } else {
                        completion.onComplete(false, null);
                        Log.e(TAG, "fetchWorkmates: ", task.getException());
                    }
                });
    }

    @SuppressWarnings("unchecked")
    private void getWorkmatesInfos(List<DocumentSnapshot> documents, GetUserInfosCompletion completion) {
        //Map<workmateID, documentID>
        Map<String, String> documentIds = new HashMap<>();
        for (DocumentSnapshot document : documents) {
            List<String> associatedUsers = (List<String>) document.get(WORKMATE_KEY);
            if (associatedUsers == null) break;
            String workmateID = !associatedUsers.get(0).equals(auth.getUid()) ? associatedUsers.get(0) : associatedUsers.get(1);
            documentIds.put(workmateID, document.getId());
        }

        db.collection("User")
                .whereIn("userID", new ArrayList<>(documentIds.keySet())) // make list with workmatesIDs
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        assert querySnapshot != null;
                        List<DocumentSnapshot> UserDocuments = querySnapshot.getDocuments();
                        List<Workmate> workmates = new ArrayList<>();

                        AtomicInteger responses = new AtomicInteger();
                        for (DocumentSnapshot userDocument : UserDocuments) {
                            String userID = userDocument.getString("userID");
                            String username = userDocument.getString("username");
                            String avatarFileName = userDocument.getString("avatarName");

                            storage.getUserAvatar(avatarFileName, (success, uri) -> {
                                workmates.add(new Workmate(userID, username, (uri != null) ? uri.toString() : null, documentIds.get(userID)));
                                if (responses.incrementAndGet() == documents.size())
                                    completion.onComplete(true, workmates);
                            });
                        }
                    } else {
                        completion.onComplete(false, null);
                        Log.e(TAG, "getWorkmatesInfos: ", task.getException());
                    }
                });
        /*List<String> userIDs = new ArrayList<>();
        for (String[] ID: IDs) {
            userIDs.add(ID[0]);
        }

        db.collection("User")
                .whereIn("userID", userIDs)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot documentsSnapshot = task.getResult();
                        assert documentsSnapshot != null;
                        List<DocumentSnapshot> documents = documentsSnapshot.getDocuments();

                        List<Workmate> workmates = new ArrayList<>();
                        AtomicInteger responses = new AtomicInteger();
                        for (DocumentSnapshot document: documents) {
                            Log.d(TAG, "getWorkmatesInfos: " + document.getId());
                            String userID = document.getString("userID");
                            String username = document.getString("username");
                            String avatarFileName = document.getString("avatarName");

                            storage.getUserAvatar(avatarFileName, (success, uri) -> {
                                workmates.add(new Workmate(userID, username, (uri != null) ? uri.toString(): null, document.getId()));
                                if (responses.incrementAndGet() == documents.size()) completion.onComplete(true, workmates);
                            });
                        }

                        completion.onComplete(true, workmates);
                    } else {
                        completion.onComplete(false, null);
                        Log.e(TAG, "getUserInfos: ", task.getException());
                    }
                });*/
    }

    private interface GetUserInfosCompletion {
        void onComplete(boolean success, List<Workmate> workmates);
    }
}
