package com.paulleclerc.go4lunch.repository;

import android.util.Log;
import com.google.firebase.storage.FirebaseStorage;
import com.paulleclerc.go4lunch.closures.GetUserAvatarUriCompletion;

class FirStorageRepository {
    private static final String TAG = FirStorageRepository.class.getSimpleName();

    FirebaseStorage storage = FirebaseStorage.getInstance();

    public void getUserAvatar(String fileName, GetUserAvatarUriCompletion completion) {
        if (fileName == null) completion.onComplete(true, null);
        else {
            storage.getReference().child("Avatar").child(fileName).getDownloadUrl().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    completion.onComplete(true, task.getResult());
                } else {
                    Log.e(TAG, "getUserAvatar: ", task.getException());
                    completion.onComplete(false, null);
                }
            });
        }
    }
}
