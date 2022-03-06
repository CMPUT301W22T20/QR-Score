package com.example.qrscore;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class CommentTest {
    public ArrayList<Comment> comments = new ArrayList<>();

    @Test
    public void testCreateComment() {
        Comment comment = new Comment("Player1", "This is a test comment", "temp");

        assertEquals(0, comments.size());

        comments.add(comment);
        assertEquals(1, comments.size());
    }

    @Test
    public void testGetters() {
        Comment newComment = new Comment("Player1", "This is a test comment", "temp");
        comments.add(newComment);

        String player1 = comments.get(0).getCommenter();
        String comment = comments.get(0).getComment();
        String date = comments.get(0).getDate();

        assertEquals("Player1", player1);
        assertEquals("This is a test comment", comment);
        assertNotNull(date);
    }
}
