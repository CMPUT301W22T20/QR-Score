package com.example.qrscore;

public class Profile {
    private String username;

    /**
     * Constructor for profile
     *
     * @param username
     *      The profile's username
     */
    public Profile(String username) {
        this.username = username;
    }

    /** Gets the Username of the player
     *
     * @return
     *      username of the player
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}


