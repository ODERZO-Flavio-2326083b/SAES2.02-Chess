package com.chessapp.chessapp.controller;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.chessapp.chessapp.model.Tuple;

public class HistoriqueController {
    public static void ecritureHistorique(String fileName, Tuple tuple1, Tuple tuple2) throws IOException {
        String directoryPath = "Data/Historique";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = directoryPath + "/" + fileName;
        File file = new File(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            if (file.createNewFile()) {
                writer.write("oldx,oldy,newx,newy\n");
            }
            writer.write(tuple1.getFirst() + "," + tuple1.getSecond() + "," + tuple2.getFirst() + "," + tuple2.getSecond() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Problème durant l'écriture du fichier");
        }
    }

    public static String createName(String pseudo1, String pseudo2) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm");
        String formattedDate = now.format(formatter);

        return pseudo1 + "-" + pseudo2 + "-" + formattedDate + ".csv";
    }

    public static void lectureHistorique(String file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line == null) {
                throw new IOException("Le fichier est vide");
            }

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length != 4) {
                    throw new IOException("Ligne de format incorrect: " + line);
                }

                int oldx = Integer.parseInt(values[0]);
                int oldy = Integer.parseInt(values[1]);
                int newx = Integer.parseInt(values[2]);
                int newy = Integer.parseInt(values[3]);

                System.out.println("oldx: " + oldx + ", oldy: " + oldy + ", newx: " + newx + ", newy: " + newy);


            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Problème durant la lecture du fichier", e);
        }
    }


    public static void main(String[] args) {

        String pseudo1 = "Player1";
        String pseudo2 = "Player2";
        String fileName = createName(pseudo1, pseudo2);

        // Simulate a few moves
        Tuple move1 = new Tuple(1, 2);
        Tuple move2 = new Tuple(2, 3);

        // Additional moves can be added as needed
        Tuple move3 = new Tuple(3, 4);
        Tuple move4 = new Tuple(4, 5);

        HistoriqueController controller = new HistoriqueController();

        try {
            // Writing the moves to the history file
            controller.ecritureHistorique(fileName, move1, move2);
            controller.ecritureHistorique(fileName, move3, move4);

            controller.lectureHistorique("Data/Historique/" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
