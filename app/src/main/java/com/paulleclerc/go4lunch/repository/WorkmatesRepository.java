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

public class WorkmatesRepository {
    private static final String TAG = WorkmatesRepository.class.getSimpleName();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final AuthRepository auth = new AuthRepository();

    public void fetchWorkmates(Context context, FetchWorkmatesCompletion completion) {
        db.collection(context.getString(R.string.workmates_array))
                .whereArrayContains(context.getString(R.string.workmates_field), auth.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "fetchWorkmates: " + task.getResult().getDocuments().size());
                        QuerySnapshot snapshot = task.getResult();
                        assert snapshot != null;
                        List<DocumentSnapshot> documents = snapshot.getDocuments();

                        List<Workmate> workmates = new ArrayList<>();
                        for (DocumentSnapshot document: documents) {
                            List<String> array = (List<String>) document.get(context.getString(R.string.workmates_field));
                            if (array == null) break;
                            String userID  = (!array.get(0).equals(auth.getUid())) ? array.get(0) : array.get(1);
                            workmates.add(new Workmate(userID));
                        }

                        completion.onComplete(true, workmates);
                    } else {
                        completion.onComplete(false, null);
                        Log.e(TAG, "fetchWorkmates: ", task.getException());
                    }
                });
    }
}
