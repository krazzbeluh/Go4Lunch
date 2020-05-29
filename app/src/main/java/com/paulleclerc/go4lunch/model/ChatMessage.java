/*
 * ChatMessage.java
 *   Go4Lunch
 *
 *   Created by paulleclerc on 5/29/20 11:36 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.model;

import java.util.Date;

public class ChatMessage {
    public final String message;
    public final String serderID;
    public final Date date;

    public ChatMessage(String message, String serderID, Date date) {
        this.message = message;
        this.serderID = serderID;
        this.date = date;
    }
}
