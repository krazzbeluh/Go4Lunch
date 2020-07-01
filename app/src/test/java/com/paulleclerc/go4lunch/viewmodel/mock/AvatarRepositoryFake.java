/*
 * AvatarRepositoryFake.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/24/20 4:55 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.viewmodel.mock;

import android.content.Context;
import android.graphics.Bitmap;

import com.paulleclerc.go4lunch.repository.AvatarRepository;

public class AvatarRepositoryFake extends AvatarRepository {
    Bitmap bmp;

    public void setBitmapOutput(Bitmap bmp) {
        this.bmp = bmp;
    }

    @Override
    public void getAvatarFromUrl(String avatarUrl, Context context,
                                 GetAvatarFromUrlCompletion completion) {
        completion.onComplete(bmp);
    }
}
