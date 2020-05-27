package com.paulleclerc.go4lunch.repository;

import android.content.Context;
import android.util.Log;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.paulleclerc.go4lunch.R;
import com.paulleclerc.go4lunch.closures.FetchWorkmatesCompletion;
import com.paulleclerc.go4lunch.model.Workmate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkmatesRepository {
    private static final String TAG = WorkmatesRepository.class.getSimpleName();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final AuthRepository auth = new AuthRepository();
    private final FirStorageRepository storage = new FirStorageRepository();

    @SuppressWarnings("unchecked")
    public void fetchWorkmates(Context context, FetchWorkmatesCompletion completion) {
        db.collection(context.getString(R.string.workmates_array))
                .whereArrayContains(context.getString(R.string.workmates_field), auth.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "fetchWorkmates: " + Objects.requireNonNull(task.getResult()).getDocuments().size());
                        QuerySnapshot snapshot = task.getResult();
                        assert snapshot != null;
                        List<DocumentSnapshot> documents = snapshot.getDocuments();

                        List<String> workmatesID = new ArrayList<>();
                        for (DocumentSnapshot document : documents) {
                            List<String> associatedUsers = (List<String>) document.get(context.getString(R.string.workmates_field));
                            if (associatedUsers == null) break;
                            workmatesID.add(!associatedUsers.get(0).equals(auth.getUid()) ? associatedUsers.get(0) : associatedUsers.get(1));
                        }

                        getWorkmatesInfos(workmatesID, (success, workmates) -> {
                            if (success) completion.onComplete(true, workmates);
                            else completion.onComplete(false, null);
                        });
                    } else {
                        completion.onComplete(false, null);
                        Log.e(TAG, "fetchWorkmates: ", task.getException());
                    }
                });
    }

    private void getWorkmatesInfos(List<String> uids, GetUserInfosCompletion completion) {
        for (String id: uids) {
            Log.d(TAG, "getWorkmatesInfos: " + id);
        }

        db.collection("User")
                .whereIn("userID", uids)
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
                                workmates.add(new Workmate(userID, username, (uri != null) ? uri.toString(): null));
                                if (responses.incrementAndGet() == documents.size()) completion.onComplete(true, workmates);
                            });
                        }

                        completion.onComplete(true, workmates);
                    } else {
                        completion.onComplete(false, null);
                        Log.e(TAG, "getUserInfos: ", task.getException());
                    }
                });
    }

    private interface GetUserInfosCompletion {
        void onComplete(boolean success, List<Workmate> workmates);
    }
}
