/*
 * SettingsViewModel.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/24/20 4:55 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.ui.settings;

import android.app.Application;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.FileUtils;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.paulleclerc.go4lunch.repository.AvatarRepository;
import com.paulleclerc.go4lunch.repository.FirStorageRepository;
import com.paulleclerc.go4lunch.repository.FirestoreRepository;
import com.paulleclerc.go4lunch.repository.UserRepository;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class SettingsViewModel extends AndroidViewModel {
    private final UserRepository user;
    private final FirStorageRepository storage;
    private final FirestoreRepository firestore;
    private final AvatarRepository avatarRepository;

    private MutableLiveData<Bitmap> avatar = new MutableLiveData<>();
    private Bitmap avatarFromLibrary = null;
    private MutableLiveData<String> username = new MutableLiveData<>();
    private MutableLiveData<Boolean> allowNotifications = new MutableLiveData<>();

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        user = new UserRepository();
        storage = new FirStorageRepository();
        firestore = new FirestoreRepository();
        avatarRepository = new AvatarRepository();
    }

    public SettingsViewModel(@NonNull Application application, UserRepository userRepository,
                             FirStorageRepository storage,
                             FirestoreRepository firestoreRepository,
                             AvatarRepository avatarRepository) {
        super(application);
        this.user = userRepository;
        this.storage = storage;
        this.firestore = firestoreRepository;
        this.avatarRepository = avatarRepository;
    }

    public LiveData<Bitmap> getAvatar() {
        user.getUserAvatar(avatarUrl -> {
            if (avatar.getValue() == null)
                avatarRepository.getAvatarFromUrl(avatarUrl, getApplication(),
                        this.avatar::setValue);
        });
        return avatar;
    }

    public LiveData<String> getUsername() {
        firestore.getUsername(this.username::setValue);
        return username;
    }

    public LiveData<Boolean> getAllowNotifications() {
        firestore.getAllowNotifications(allowNotifications::setValue);
        return allowNotifications;
    }

    public void validateChanges(String username, boolean allowNotifications) {
        Bitmap avatar = this.avatar.getValue();

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        if (avatar == avatarFromLibrary && avatar != null) {
            avatar.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getApplication().getContentResolver(),
                    avatar, UUID.randomUUID().toString(), null);

            storage.saveUserAvatar(Uri.parse(path), firestore::setNewUserAvatar);
        }

        if (username != null && !username.equals(this.username.getValue())) {
            firestore.setUsername(username);
        }

        Boolean areNotificationsAllowed = this.allowNotifications.getValue();
        if (areNotificationsAllowed != null && allowNotifications != areNotificationsAllowed) {
            firestore.setAllowNotifications(allowNotifications);
        }
    }

    public void getImageFromData(Uri avatarUri) {
        Bitmap avatar = null;

        if (Build.VERSION.SDK_INT >= 29) {
            ParcelFileDescriptor parcelFileDescriptor = null;
            try {
                parcelFileDescriptor = getApplication().getContentResolver().openFileDescriptor(avatarUri, "r", null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            if (parcelFileDescriptor != null) {
                FileInputStream inputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());

                String fileName = UUID.randomUUID().toString();
                File file = new File(getApplication().getCacheDir(), fileName);

                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                if (outputStream != null) {
                    try {
                        FileUtils.copy(inputStream, outputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                avatar = BitmapFactory.decodeFile(file.getPath());
            }
        } else {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getApplication().getContentResolver().query(avatarUri, filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                avatar = BitmapFactory.decodeFile(picturePath);
            }
        }

        if (avatar != null) {
            this.avatar.setValue(avatar);
            avatarFromLibrary = avatar;
        }
    }
}
