/*
 * FileReader.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/26/20 10:42 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.utils;

import androidx.test.platform.app.InstrumentationRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FileReader {
    public static String readStringFromFile(String fileName) throws IOException {
        InputStream inputStream = InstrumentationRegistry.getInstrumentation().getTargetContext()
                .getApplicationContext().getAssets().open(fileName);
        StringBuilder builder = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        BufferedReader br = new BufferedReader(reader);

        String line;
        while ((line = br.readLine()) != null) {
            builder.append(line)
                    .append("\n");
        }

        return builder.toString();
    }
}
