/*
 * SettingsActivity.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/17/20 4:36 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.ui.settings;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.paulleclerc.go4lunch.R;

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
    @BindView(R.id.settings_username_input)
    TextInputEditText usernameInput;
    @BindView(R.id.settings_allow_daily_checkbox)
    CheckBox allowNotificationsCheckBox;

    private SettingsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        ButterKnife.bind(this);

        viewModel.getAvatar().observe(this, avatarImage::setImageBitmap);
        viewModel.getUsername().observe(this, usernameInput::setText);
        viewModel.getAllowNotifications().observe(this, allowNotifications -> allowNotificationsCheckBox.setChecked(allowNotifications));
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
        if (requestCode == KEY_RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            if (selectedImage != null) {
                viewModel.getImageFromData(selectedImage);
            }
        }
    }

    @OnClick(R.id.settings_validate_changes)
    public void validateChanges() {
        Editable username = usernameInput.getText();
        viewModel.validateChanges(username == null ? null : username.toString(), allowNotificationsCheckBox.isChecked());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}