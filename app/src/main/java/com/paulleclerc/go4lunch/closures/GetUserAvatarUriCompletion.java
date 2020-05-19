package com.paulleclerc.go4lunch.closures;

import android.net.Uri;

public interface GetUserAvatarUriCompletion {
    void onComplete(boolean success, Uri uri);
}
