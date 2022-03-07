package com.example.qrscore;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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