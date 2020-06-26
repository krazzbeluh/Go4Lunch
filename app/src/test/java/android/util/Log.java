/*
 * Log.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/18/20 3:07 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package android.util;

public class Log {
    public static int d(String tag, String msg) {
        System.out.println("DEBUG: " + tag + ": " + msg);
        return 0;
    }

    public static int i(String tag, String msg) {
        System.out.println("INFO: " + tag + ": " + msg);
        return 0;
    }

    public static int w(String tag, String msg) {
        System.out.println("WARN: " + tag + ": " + msg);
        return 0;
    }

    public static int e(String tag, String msg, Throwable tr) {
        System.out.println("ERROR: " + tag + ": " + msg);
        return 0;
    }

}