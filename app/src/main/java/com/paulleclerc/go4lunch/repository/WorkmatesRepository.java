/*
 * WorkmatesRepository.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/18/20 12:47 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.repository;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
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
                .whereIn(FieldPath.documentId(), new ArrayList<>(documentIds.keySet())) // make list with workmatesIDs
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
                            String chosenRestaurantId = userDocument.getString("chosenPlaceId");

                            new PlacesRepository().getName(chosenRestaurantId, name -> storage.getUserAvatar(avatarFileName, (success, uri) -> {
                                workmates.add(new Workmate(userID, username, (uri != null) ? uri.toString() : null, documentIds.get(userID), chosenRestaurantId, name));
                                if (responses.incrementAndGet() == documents.size())
                                    completion.onComplete(true, workmates);
                            }));
                        }
                    } else {
                        completion.onComplete(false, null);
                        Log.e(TAG, "getWorkmatesInfos: ", task.getException());
                    }
                });
    }

    private interface GetUserInfosCompletion {
        void onComplete(boolean success, List<Workmate> workmates);
    }
}
