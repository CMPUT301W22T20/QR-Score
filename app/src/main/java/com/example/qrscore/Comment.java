package com.example.qrscore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This is a class that contains the info of comments
 */
public class Comment {
    private final String commenter;
    private String comment;  // keep as is for now might want to be able to edit comment later
    private final String date;
    private final String qrID; // ID for each of the qrCodes in DB

    /**
     * Constructor for the class when someone creates a new comment
     * @param commenter
     *      Name of the commenter
     * @param comment
     *      String of the comment
     * @param qrID
     *      ID for which qr code the comment belongs to
     */
    public Comment(String commenter, String comment, String qrID) {
        this.commenter = commenter;
        this.comment = comment;
        this.qrID = qrID;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date c = Calendar.getInstance().getTime();
        this.date = sdf.format(c);
    }

    /**
     * Constructor for the class when fetching data from the DB
     * @param commenter
     *      Name of the commenter
     * @param comment
     *      String of the comment
     * @param qrID
     *      ID for which qr code the comment belongs to
     * @param date
     *      Date the comment was created
     */
    public Comment(String commenter, String comment, String qrID, String date) {
        this.commenter = commenter;
        this.comment = comment;
        this.qrID = qrID;
        this.date = date;
    }

    /**
     * Returns the name of the commenter
     * @return
     */
    public String getCommenter() {
        return commenter;
    }

    /**
     * Returns the comment
     * @return
     */
    public String getComment() {
        return comment;
    }

    /**
     * Returns the date of when the comment was created
     * @return
     */
    public String getDate() {
        return date;
    }

    /**
     * Returns the qr code ID the comment belongs to
     * @return
     */
    public String getID() {
        return qrID;
    }
}