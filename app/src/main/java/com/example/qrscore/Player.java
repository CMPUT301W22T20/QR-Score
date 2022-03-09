package com.example.qrscore;

public class Player {
    private Account account;

    /**
     * Constructor for player
     *
     * @param account
     *      The player's account
     */
    public Player(Account account) {
        this.account = account;
    }

    /**
     * Gets the Username of the player
     *
     * @return
     *      username of the player
     */
    public String getUsername() {
        return account.getUsername();
    }
}
