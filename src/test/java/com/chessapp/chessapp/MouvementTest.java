package com.chessapp.chessapp;

import com.chessapp.chessapp.model.*;
import com.chessapp.chessapp.model.chessPiece.*;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MouvementTest {

    private Plateau plateau;

    @BeforeAll
    public static void initJfx(){
        Platform.startup(() -> {});
    }

    @BeforeEach
    public void setUp() {
        plateau = new Plateau();
        try {
            // Initialisation des pièces sur le plateau
            Pawn pionA4 = new Pawn(0,3,-1);
            plateau.addPiece(0,3,pionA4);
            Pawn pionB2 = new Pawn(1,1,-1);
            plateau.addPiece(1,1,pionB2);
            Rook tourC6 = new Rook(2,5,-1);
            plateau.addPiece(2, 5, tourC6); // Tour en C6
            King roiC3 = new King(2,2,-1);
            plateau.addPiece(2, 2, roiC3); // Roi en C3
            Pawn pionD2 = new Pawn(3,1,-1);
            plateau.addPiece(3, 1, pionD2); // Pion en D2
            Bishop fouD4 = new Bishop(3,3,-1);
            plateau.addPiece(3, 3, fouD4); // Fou en D4
            Queen reineE7 = new Queen(4,6,-1);
            plateau.addPiece(4, 6, reineE7); // Reine en E7
            Knight cavalierH6 = new Knight(7,5,-1);
            plateau.addPiece(7, 5, cavalierH6); // Cavalier en H6
            Pawn pionH4 = new Pawn(7,3,-1);
            plateau.addPiece(7, 3, pionH4); // Pion en H4

            // Initialisation de quelques pièces noires
            Pawn pionA5 = new Pawn(0,4,1);
            plateau.addPiece(0, 4, pionA5); // Pion en D5
            Pawn pionB7 = new Pawn(1,6,1);
            plateau.addPiece(1, 6, pionB7); // Pion en B7
            Pawn pionD6 = new Pawn(3,5,1);
            plateau.addPiece(3, 5, pionD6); // Pion en D6
            Pawn pionG5 = new Pawn(6,4,1);
            plateau.addPiece(6, 4, pionG5); // Pion en G5
            Pawn pionG7 = new Pawn(6,6,1);
            plateau.addPiece(6, 6, pionG7); // Pion en G7
            Pawn pionG8 = new Pawn(6,7,1);
            plateau.addPiece(6, 7, pionG8); // Cavalier en H8

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Test des mouvements du pion blanc en D4 bloqué par un pion noir")
    public void test1() throws Exception {
        try {
            Piece piece = plateau.getPiece(0, 3);
            List<Tuple> moves = piece.calculateMovements(plateau);
            assertTrue(moves.isEmpty());
        } catch (Exception e) {
            fail("Exception during test execution: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Test des mouvements du pion blanc en position initiale (B2)")
    public void test2() {
        try {
            Piece piece = plateau.getPiece(1, 1);
            List<Tuple> moves = piece.calculateMovements(plateau);
            List<Tuple> expectedMoves = Arrays.asList(
                    new Tuple(1, 2), new Tuple(1, 3));

            assertTrue(moves.containsAll(expectedMoves) && expectedMoves.containsAll(moves));
        } catch (Exception e) {
            fail("Exception during test execution: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Test des mouvements de la Tour blanche en C6")
    public void test3() {
        try {
            Piece piece = plateau.getPiece(2, 5);
            List<Tuple> moves = piece.calculateMovements(plateau);
            List<Tuple> expectedMoves = Arrays.asList(
                    new Tuple(2, 4), new Tuple(2, 3), new Tuple(2, 6), new Tuple(2, 7), // Verticalement
                    new Tuple(1, 5), new Tuple(0, 5), new Tuple(3, 5) // Horizontalement
            );
            assertTrue(moves.containsAll(expectedMoves) && expectedMoves.containsAll(moves));
        } catch (Exception e) {
            fail("Exception during test execution: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Test des mouvements du Roi blanc en C3")
    public void test4() {
        try {
            Piece piece = plateau.getPiece(2, 2);
            List<Tuple> moves = piece.calculateMovements(plateau);
            List<Tuple> expectedMoves = Arrays.asList(
                    new Tuple(2, 1), new Tuple(2, 3),
                    new Tuple(1, 2), new Tuple(3, 2),
                    new Tuple(1, 3)
            );
            assertTrue(moves.containsAll(expectedMoves) && expectedMoves.containsAll(moves));
        } catch (Exception e) {
            fail("Exception during test execution: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Test des mouvements du Pion en position initiale bloque sur l'avance de 2 (D2)")
    public void test5() {
        try {
            Piece piece = plateau.getPiece(3, 1);
            List<Tuple> moves = piece.calculateMovements(plateau);
            List<Tuple> expectedMoves = List.of(
                    new Tuple(3, 2)
            );
            assertTrue(moves.containsAll(expectedMoves) && expectedMoves.containsAll(moves));
        } catch (Exception e) {
            fail("Exception during test execution: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Test des mouvements du Fou blanc en D4")
    public void test6() {
        try {
            Piece piece = plateau.getPiece(3, 3);
            List<Tuple> moves = piece.calculateMovements(plateau);
            List<Tuple> expectedMoves = Arrays.asList(
                    new Tuple(4, 4), new Tuple(5, 5), new Tuple(6, 6),
                    new Tuple(2, 4), new Tuple(1, 5), new Tuple(0, 6),
                    new Tuple(4, 2), new Tuple(5, 1), new Tuple(6, 0)
            );
            assertTrue(moves.containsAll(expectedMoves) && expectedMoves.containsAll(moves));
        } catch (Exception e) {
            fail("Exception during test execution: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Test des mouvements de la Reine blanche en E7")
    public void test7() {
        try {
            Piece piece = plateau.getPiece(4, 6);
            List<Tuple> moves = piece.calculateMovements(plateau);
            List<Tuple> expectedMoves = Arrays.asList(
                    new Tuple(4, 5), new Tuple(4, 4), new Tuple(4, 3), new Tuple(4, 2), new Tuple(4, 1), new Tuple(4, 0),
                    new Tuple(4, 7),
                    new Tuple(3, 6), new Tuple(2, 6), new Tuple(1, 6),
                    new Tuple(5, 6), new Tuple(6, 6),
                    new Tuple(3, 5),
                    new Tuple(5, 7),
                    new Tuple(3, 7),
                    new Tuple(5, 5), new Tuple(6, 4)
            );
            assertTrue(moves.containsAll(expectedMoves) && expectedMoves.containsAll(moves));
        } catch (Exception e) {
            fail("Exception during test execution: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Test des mouvements du Cavalier blanc en H6")
    public void test8() {
        try {
            Piece piece = plateau.getPiece(7, 5);
            List<Tuple> moves = piece.calculateMovements(plateau);
            List<Tuple> expectedMoves = Arrays.asList(
                    new Tuple(5, 4), new Tuple(5, 6), // Saute en G4 et G6
                    new Tuple(6, 3), new Tuple(6, 7) // Saute en F3 et F7
            );
            assertTrue(moves.containsAll(expectedMoves) && expectedMoves.containsAll(moves));
        } catch (Exception e) {
            fail("Exception during test execution: " + e.getMessage());
        }
    }


    @Test
    @DisplayName("Test des mouvements du pion blanc en H4 en diagonale")
    public void test9() {
        try {
            Piece piece = plateau.getPiece(7, 3);
            List<Tuple> moves = piece.calculateMovements(plateau);
            List<Tuple> expectedMoves = Arrays.asList(
                    new Tuple(7, 4), new Tuple(6, 4)
            );
            assertTrue(moves.containsAll(expectedMoves) && expectedMoves.containsAll(moves));
        } catch (Exception e) {
            fail("Exception during test execution: " + e.getMessage());
        }
    }



}