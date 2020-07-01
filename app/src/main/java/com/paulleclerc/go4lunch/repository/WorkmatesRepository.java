/*
 * WorkmatesRepository.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/29/20 3:34 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.repository;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.paulleclerc.go4lunch.model.Workmate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;

public class WorkmatesRepository {
    private static final String TAG = WorkmatesRepository.class.getSimpleName();
    public static final String WORKMATE_KEY = "workmates";
    private static final String WORKMATES_ARRAY_KEY = "WorkmatesArray";

    private final FirebaseFirestore db;
    private final AuthRepository auth;
    private final FirStorageRepository storage;

    public WorkmatesRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.auth = new AuthRepository();
        this.storage = new FirStorageRepository();
    }

    public WorkmatesRepository(FirebaseFirestore db, AuthRepository auth,
                               FirStorageRepository storage) {
        this.db = db;
        this.auth = auth;
        this.storage = storage;
    }

    public void fetchWorkmates(FetchWorkmatesCompletion completion) {
        db.collection(WORKMATES_ARRAY_KEY)
                .whereArrayContains(WORKMATE_KEY, auth.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    List<DocumentSnapshot> documents = getDocumentsFromTask(task);
                    if (documents != null && !documents.isEmpty())
                        getWorkmatesInfos(documents, completion::onComplete);
                    else completion.onComplete(null);
                });
    }

    @Nullable
    public List<DocumentSnapshot> getDocumentsFromTask(Task<QuerySnapshot> task) {
        QuerySnapshot snapshot = task.getResult();
        if (task.isSuccessful() && snapshot != null) {
            return snapshot.getDocuments();
        } else {
            Log.e(TAG, "fetchWorkmates: ", task.getException());
            return null;
        }
    }

    private void getWorkmatesInfos(@Nonnull List<DocumentSnapshot> documents, GetUserInfosCompletion completion) {
        //Map<workmateID, documentID>
        Map<String, String> documentIds = getWorkmatesDocumentsIds(documents);
        db.collection("User")
                .whereIn(FieldPath.documentId(), new ArrayList<>(documentIds.keySet())) // make list with workmatesIDs
                .get().addOnCompleteListener(task -> {
            List<DocumentSnapshot> UserDocuments = getDocumentsFromTask(task);

            if (UserDocuments == null) completion.onComplete(null);
            else {
                List<Workmate> workmates = new ArrayList<>();

                AtomicInteger responses = new AtomicInteger();
                for (DocumentSnapshot userDocument : UserDocuments) {
                    String userID = userDocument.getId();
                    String username = userDocument.getString("username");
                    String avatarFileName = userDocument.getString("avatarName");
                    String chosenRestaurantId = userDocument.getString("chosenPlaceId");

                    storage.getUserAvatar(avatarFileName, ((success, uri) -> {
                        if (chosenRestaurantId != null) {
                            new PlacesRepository().getName(chosenRestaurantId, name -> {
                                workmates.add(new Workmate(userID, username, (uri != null) ?
                                        uri.toString() : null, documentIds.get(userID),
                                        chosenRestaurantId, name));
                                if (responses.incrementAndGet() == documents.size())
                                    completion.onComplete(workmates);
                            });
                        } else {
                            workmates.add(new Workmate(userID, username, (uri != null) ?
                                    uri.toString() : null, documentIds.get(userID),
                                    chosenRestaurantId, null));
                            if (responses.incrementAndGet() == documents.size())
                                completion.onComplete(workmates);
                        }
                    }));
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getWorkmatesDocumentsIds(@Nonnull List<DocumentSnapshot> documents) {
        Map<String, String> documentIds = new HashMap<>();
        for (DocumentSnapshot document : documents) {
            List<String> associatedUsers = (List<String>) document.get(WORKMATE_KEY);
            if (associatedUsers == null) break;
            String workmateID = !associatedUsers.get(0).equals(auth.getUid()) ? associatedUsers.get(0) : associatedUsers.get(1);
            documentIds.put(workmateID, document.getId());
        }

        return documentIds;
    }

    private interface GetUserInfosCompletion {
        void onComplete(List<Workmate> workmates);
    }

    public interface FetchWorkmatesCompletion {
        void onComplete(List<Workmate> workmates);
    }
}
