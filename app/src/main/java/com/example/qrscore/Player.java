package com.example.qrscore;

public class Player {
    private Account account;

    /** Gets the Username of the player
     *
     * @return
     *      username of the player
     */
    public String getUsername() {
        return account.getUsername();
    }
}
