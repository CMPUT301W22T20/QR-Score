package com.example.qrscore;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Purpose: This class represents a comment on a QR Code.
 * Stores the comment posted
 * Stores the user who posted the comment
 * Stores a timestamp of when the comment was posted
 * Stores the ID of the QR Code commented on
 *
 * Outstanding issues:
 * TODO: As a player, I want to add new QR codes to my account.
 * TODO: As a player, I want to see what QR codes I have added to my account.
 * TODO: As a player, I want to remove QR codes from my account.
 * TODO: As a player, I want to see my highest and lowest scoring QR codes.
 * TODO: Unit tests
 * TODO: Javadocs
 */
public class Comment {
    private final String commenter;
    private String comment;  // keep as is for now might want to be able to edit comment later
    private final String date;
    private final String qrID; // ID for each of the qrCodes in DB

    public Comment(String commenter, String comment, String qrID) {
        this.commenter = commenter;
        this.comment = comment;
        this.qrID = qrID;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date c = Calendar.getInstance().getTime();
        this.date = sdf.format(c);
    }

    public String getCommenter() {
        return commenter;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }
}