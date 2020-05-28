/*
 * GetUserAvatarUriCompletion.java
 *   Go4Lunch
 *
 *   Created by paulleclerc on 5/27/20 5:13 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.closures;

import android.net.Uri;

public interface GetUserAvatarUriCompletion {
    void onComplete(boolean success, Uri uri);
}
