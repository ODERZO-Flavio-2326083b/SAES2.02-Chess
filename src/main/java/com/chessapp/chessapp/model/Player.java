package com.chessapp.chessapp.model;

import java.io.IOException;

/**
 * Classe simple pour stocker les statistiques de chaque joueur, utile pour l'affichage dans un TableView ou
 * pour l'implémentation des tournois
 */
public class Player {
    private String name;
    private int gamesPlayed;
    private int wins;
    private int losses;
    private double winRate;

    public Player(String name) throws Exception {
        this.name = name;
        if (name == "SKIP") { // si jamais c'est un faux-joueur juste pour remplir une case d'un nombre de joueurs impairs dans un tournoi
            return;
        }
        String[] stats = PlayerHandler.lireStats(name);

        if (stats != null) {
            this.gamesPlayed = Integer.parseInt(stats[1]);
            this.wins = Integer.parseInt(stats[2]);
            this.losses = Integer.parseInt(stats[3]);
            if (losses == 0) {
                if (wins == 0){
                    this.winRate = 0.5;
                } else {
                    this.winRate = 1.0;
                }
            } else
                this.winRate = (double) wins / gamesPlayed;
        } else {
            throw new Exception(String.format("Player : fichier joueur %s n'existe pas", name));
        }
    }

    public String getName() {
        return name;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public double getWinRate() {
        return winRate;
    }

    @Override
    public String toString() {
        return "Player [name=" + name + "]";
    }
}
