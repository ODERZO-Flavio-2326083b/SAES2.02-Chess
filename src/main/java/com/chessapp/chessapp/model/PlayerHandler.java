package com.chessapp.chessapp.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PlayerHandler {

    static String directoryPath = "Data/Players";

    /**
     * méthode pour vérifier l'existence d'un joueur et créer le fichier si nécessaire
     * si le fichier (csv) est créé, les données à l'intérieur sont initialisées à 0
     * @param pseudo nom du joueur
     * @throws IOException en cas d'erreur lors de la création ou de l'écriture dans le fichier
     */
    public static void verficationJoueur(String pseudo) throws IOException {
        String filePath = directoryPath + "/" + pseudo + ".csv";

        // Vérifier si le répertoire existe, sinon le créer
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Vérifier si le fichier existe
        if (Files.exists(Paths.get(filePath))) {
            return;
        } else {
            // Créer le fichier et l'initialiser si il n'existe pas
            File file = new File(filePath);
            if (file.createNewFile()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write("\"pseudo\",nb_parties,nb_victoires,nb_defaite\n");
                    writer.write(pseudo + ",0,0,0\n");
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new IOException("erreur lors de l'écriture dans le fichier " + pseudo + ".csv", e);
                }
            } else {
                throw new IOException("erreur lors de la création du fichier " + pseudo + ".csv");
            }
        }
    }


    /**
     * méthode pour finir une partie entre deux joueurs qui va enlever 1
     * ajoutee 1 a nb_vicoire au  gagnant et ajouter 1 a nb_defaite au perdant
     * @param gagnant nom du joueur gagnant
     * @param perdant nom du joueur perdant
     * @throws IOException en cas d'erreur lors de la lecture ou de l'écriture dans le fichier
     */
    public static void finPartie(String gagnant, String perdant) throws IOException {
        modifierStats(gagnant, 1, 1,  0);
        modifierStats(perdant, 1, 0,  1);
    }

    /**
     * méthode pour modifier les statistiques d'un joueur
     * @param pseudo nom du joueur
     * @param nbParties parties à ajouter
     * @param nbVictoires  victoires à ajouter
     * @param nbDefaites  défaites à ajouter
     * @throws IOException si erreur lors de la lecture ou de l'écriture dans le fichier
     */
    public static void modifierStats(String pseudo, int nbParties, int nbVictoires, int nbDefaites) throws IOException {
        String[] stats = lireStats(pseudo);
        if (stats != null) {
            stats[1] = Integer.toString(Integer.parseInt(stats[1]) + nbParties);
            stats[2] = Integer.toString(Integer.parseInt(stats[2]) + nbVictoires);
            stats[3] = Integer.toString(Integer.parseInt(stats[3]) + nbDefaites);
            ecrireStats(pseudo, stats);
        }
    }

    /**
     * lit les statistiques d'un joueur et les renvoie
     * @param pseudo nom du joueur
     * @return les statistiques dans une liste de String
     * @throws IOException si erreur de lecture
     */
    public static String[] lireStats(String pseudo) throws IOException {
        String filePath = directoryPath + "/" + pseudo + ".csv";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // sauter la première ligne
            String line = reader.readLine();
            if (line != null) {
                return line.split(","); // renvoie les stats séparées d'une virgule
            }

        }
        return null;
    }

    /**
     * permet de modifier les statistiques d'un fichier joueur
     * @param pseudo nom du joueur
     * @param stats statistiques de remplacement
     * @throws IOException si erreur de lecture
     */
    private static void ecrireStats(String pseudo, String[] stats) throws IOException {
        String filePath = directoryPath + "/" + pseudo + ".csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("pseudo,nb_parties,nb_victoires,nb_defaite\n");
            writer.write(String.join(",", stats) + "\n");
        }
    }

    public static Player[] obtenirJoueurs() throws Exception {
        File directory = new File(directoryPath);
        String[] playerFiles = directory.list();
        Player[] allPlayers = new Player[playerFiles.length];

        for (int i = 0; i < allPlayers.length; i++) {
            allPlayers[i] = new Player(playerFiles[i].substring(0, playerFiles[i].indexOf(".csv")));
        }

        return allPlayers;
    }

    public static void main(String[] args) {
        try {
            verficationJoueur("joueur1");
            verficationJoueur("joueur2");

            finPartie("joueur1", "joueur2");
            finPartie("joueur1", "joueur2");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
