/*
 * SettingsActivity.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/5/20 11:58 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.ui.settings;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.paulleclerc.go4lunch.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {
    private static final int KEY_RESULT_LOAD_IMAGE = 42;
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
    }

    @OnClick(R.id.settings_avatar_layout)
    public void changeAvatar() {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, KEY_RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KEY_RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            if (selectedImage != null) {
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    avatarImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));

                    isImageFromGallery = true;
                }
            }
        }
    }
}