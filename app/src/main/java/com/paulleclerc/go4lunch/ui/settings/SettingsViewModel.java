/*
 * SettingsViewModel.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/5/20 11:24 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.ui.settings;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.paulleclerc.go4lunch.repository.UserRepository;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class SettingsViewModel extends AndroidViewModel {
    private UserRepository user = new UserRepository();

    private MutableLiveData<String> avatarUrl = new MutableLiveData<>();
    private MutableLiveData<Bitmap> newAvatar = new MutableLiveData<>();

    public SettingsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Bitmap> getAvatar() {
        return newAvatar;
    }

    public void setAvatar(Bitmap avatar) {
        newAvatar.setValue(avatar);
    }

    public LiveData<String> getUserAvatarUrl() {
        user.getUserAvatar(avatarUrl::setValue);
        return avatarUrl;
    }

    public void validateChanges() {
        Bitmap avatar = newAvatar.getValue();

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        if (avatar != null) {
            avatar.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getApplication().getApplicationContext().getContentResolver(),
                    avatar, UUID.randomUUID().toString(), null);

            user.setUserAvatar(Uri.parse(path));
        }
    }
}
