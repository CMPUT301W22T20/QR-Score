package com.example.qrscore;

/**
 * Purpose: This class is the interface between activities and
 * the account in the database.
 *
 * Outstanding issues:
 * TODO: Finish Purpose
 * TODO: Connect to database
 * TODO: Unit tests
 */
public class Player {
    private Account account;

    /**
     * Constructor for player
     * @param account
     *      The player's account
     */
    public Player(Account account) {
        this.account = account;
    }

    /**
     * Gets the Username of the player
     * @return
     *      username of the player
     */
//    public String getUsername() {
//        return account.getUsername();
//    }
}
