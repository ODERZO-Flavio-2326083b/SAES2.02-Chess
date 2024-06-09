package com.chessapp.chessapp.model;


/**
 * Classe simple utilisée pour la gestion des tournois, garde en mémoire le duel
 */
public class Match {
    private Player plyOne;
    private Player plyTwo;

    /**
     * Initialise un objet Match
     * @param plyOne joueur blanc
     * @param plyTwo joueur noir
     */
    public Match(Player plyOne, Player plyTwo) {
        this.plyOne = plyOne;
        this.plyTwo = plyTwo;
    }

    public Player getPlyOne() {
        return plyOne;
    }

    public Player getPlyTwo() {
        return plyTwo;
    }

    @Override
    public String toString() {
        return "Match [plyOne=" + plyOne + ", plyTwo=" + plyTwo + "]";
    }
}
