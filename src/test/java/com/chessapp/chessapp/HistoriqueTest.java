package com.chessapp.chessapp;

import com.chessapp.chessapp.model.HistoriqueHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class HistoriqueTest {

    @BeforeAll
    public static void setup() {


    }

    @Test
    @DisplayName("Création du nom de fichier")
    public void test1() {
        String pseudo1 = "Player1";
        String pseudo2 = "Player2";
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm");
        String formattedDate = now.format(formatter);

        String fileName = HistoriqueHandler.createName(pseudo1, pseudo2);

        assertEquals("Player1-Player2-" + formattedDate + ".csv", fileName);
    }

}
