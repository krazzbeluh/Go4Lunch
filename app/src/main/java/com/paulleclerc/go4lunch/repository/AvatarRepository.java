/*
 * AvatarRepository.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/8/20 10:44 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

public class AvatarRepository {
    public void getAvatarFromUrl(String avatarUrl, Context context, GetAvatarFromUrlCompletion completion) {
        Glide.with(context)
                .asBitmap()
                .load(avatarUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        completion.onComplete(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    public interface GetAvatarFromUrlCompletion {
        void onComplete(Bitmap avatar);
    }
}
