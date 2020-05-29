/*
 * ChatMessage.java
 *   Go4Lunch
 *
 *   Created by paulleclerc on 5/29/20 3:23 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.model;

import java.util.Date;

public class ChatMessage {
    public final String message;
    public final String senderID;
    public final Date date;

    public ChatMessage(String message, String senderID, Date date) {
        this.message = message;
        this.senderID = senderID;
        this.date = date;
    }
}
