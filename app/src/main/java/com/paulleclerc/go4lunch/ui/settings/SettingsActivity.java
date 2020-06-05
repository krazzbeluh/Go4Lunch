/*
 * SettingsActivity.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/5/20 11:24 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.ui.settings;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.paulleclerc.go4lunch.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SettingsActivity extends AppCompatActivity {
    public static final String PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int GET_STORAGE_PERMS = 872;
    private static final int KEY_RESULT_LOAD_IMAGE = 659;

    @BindView(R.id.settings_avatar_layout)
    ConstraintLayout avatarLayout;
    @BindView(R.id.settings_avatar_image)
    ImageView avatarImage;
    private SettingsViewModel viewModel;
    private boolean isImageFromGallery = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        ButterKnife.bind(this);

        viewModel.getUserAvatarUrl().observe(this, avatarUrl -> {
            if (!isImageFromGallery)
                Glide.with(this).load(avatarUrl).placeholder(R.drawable.workmate).into(avatarImage);
        });

        viewModel.getAvatar().observe(this, avatar -> {
            if (isImageFromGallery) avatarImage.setImageBitmap(avatar);
        });
    }

    @AfterPermissionGranted(GET_STORAGE_PERMS)
    @OnClick(R.id.settings_avatar_layout)
    public void changeAvatar() {
        if (EasyPermissions.hasPermissions(this, PERM)) {
            Intent i = new Intent(
                    Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, KEY_RESULT_LOAD_IMAGE);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.permission_storage_message), GET_STORAGE_PERMS, PERM);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap avatar = null;
        if (requestCode == KEY_RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            if (selectedImage != null) {
                if (Build.VERSION.SDK_INT >= 29) {
                    ParcelFileDescriptor parcelFileDescriptor = null;
                    try {
                        parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedImage, "r", null);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    if (parcelFileDescriptor != null) {
                        FileInputStream inputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());

                        String fileName = UUID.randomUUID().toString();
                        File file = new File(getCacheDir(), fileName);

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
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();

                        avatar = BitmapFactory.decodeFile(picturePath);
                    }
                }
            }
        }

        if (avatar != null) {
            isImageFromGallery = true;
            viewModel.setAvatar(avatar);
        }
    }

    @OnClick(R.id.settings_validate_changes)
    public void validateChanges() {
        viewModel.validateChanges();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}