package com.chessapp.chessapp.model;

public class Plateau {
    private Piece[][] plateau;

    public Plateau() {
        plateau = new Piece[8][8];
    }

    public void movement(int oldX, int oldY, int newX, int newY) throws Exception {
        if (plateau[oldY][oldX] == null) {
            throw new Exception("ERREUR Plateau.movement() : case vide");
        }

        plateau[newY][newX] = plateau[oldY][oldX];
        plateau[oldY][oldX] = null;
    }

    public void addPawn(int x, int y, char pawnType, int color) throws Exception {
        if (plateau[y][x] != null) {
            throw new Exception("ERREUR Plateau.addPawn() : case non vide");
        }

        Piece pion = new Pion(x, y, pawnType, color, this);
        plateau[y][x] = pion;
    }

    public void showGrid() {
        for (Piece[] liste : plateau) {
            for (Piece p : liste) {
                System.out.print((p == null) ? "0" : "1");
            }
            System.out.println();
        }
    }

    public void setGrid(Piece[][] newPlateau) {
        this.plateau = newPlateau;
    }

    public Piece getPiece(int x, int y) {
        return plateau[y][x];
    }

    public boolean isEmpty(int x, int y) {
        return plateau[y][x] == null;
    }

    public boolean isEnemyPiece(int x, int y, int color) {
        Piece piece = plateau[y][x];
        return piece != null && piece.getColor() != color;
    }
}
