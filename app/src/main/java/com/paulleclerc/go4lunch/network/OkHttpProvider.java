/*
 * OkHttpProvider.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/26/20 10:42 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class OkHttpProvider {
    // Timeout for the network requests
    private static long REQUEST_TIMEOUT = 10L;

    private static OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .build();

    public static OkHttpClient getOkHttpClient() {
        return client;
    }
}
