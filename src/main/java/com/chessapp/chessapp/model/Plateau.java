package com.chessapp.chessapp.model;

import com.chessapp.chessapp.model.chessPiece.Piece;

/**
 * La matrice 8x8 de jeu où la partie se déroule.
 */
public class Plateau {
    private Piece[][] plateau;

    /**
     * Constructeur plateau
     */
    public Plateau() {
        plateau = new Piece[8][8];
    }

    /**
     * Initie le déplacement d'un pion à une autre case. Ne vérifie pas si le mouvement est valide
     * @param oldX X source
     * @param oldY Y source
     * @param newX X destination
     * @param newY Y destination
     * @throws Exception Renvoie une erreur si la case source est vide
     */
    public void movement(int oldX, int oldY, int newX, int newY) throws Exception {
        if(plateau[oldY][oldX] == null) {
            throw new Exception(String.format("ERREUR Plateau.movement() : case source X:%d, Y:%d vide", oldX, oldY));
        }

        plateau[newY][newX] = plateau[oldY][oldX];
        plateau[oldY][oldX] = null;

        plateau[newY][newX].setxTab(newX);
        plateau[newY][newX].setyTab(newY);


    }

    public Piece getPiece(int x, int y){
        return plateau[y][x];
    }

    /**
     * Ajoute l'objet Piece fourni dans la matrice aux coordonnées précisées
     * @param x coordonnée x
     * @param y coordonnée y
     * @param piece objet piece
     * @throws Exception renvoie une erreur si la case n'est pas vide
     */
    public void addPiece(int x, int y, Piece piece) throws Exception {
        if (plateau[y][x] != null) {
            throw new Exception("ERREUR Plateau.addPawn() : case non vide");
        }

        plateau[y][x] = piece;
    }

    /**
     * Ajoute l'objet Piece fourni dans la matrice aux coordonnées de cette pièce
     * @param piece objet piece
     * @throws Exception renvoie une erreur si la case n'est pas vide
     */
    public void addPiece(Piece piece) throws Exception {
        if (plateau != null) {
            throw new Exception("ERREUR Plateau.addPawn() : case non vide");
        }

        plateau[piece.getyTab()][piece.getxTab()] = piece;
    }

    /**
     * Affiche la grille dans la console, utile pour le debug
     */
    public void showGrid() {

        for(Piece[] liste : plateau) {
            for(Piece p : liste) {
                System.out.print((p == null) ? "0" : p.getPieceType().charAt(0));
            }
            System.out.println();
        }

    }

    /**
     * Remplace toute la matrice plateau par une autre manuellement
     * @param newPlateau matrice plateau de remplacement
     */
    public void setGrid(Piece[][] newPlateau) {
        this.plateau = newPlateau;
    }

    public Piece[][] getGrid() {
        return plateau;
    }

    public void clearPlateau() {
        this.plateau = new Piece[8][8];
    }
}
