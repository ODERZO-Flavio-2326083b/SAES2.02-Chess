package com.chessapp.chessapp.model.chessPiece;

import com.chessapp.chessapp.model.Plateau;
import com.chessapp.chessapp.model.Tuple;
import javafx.scene.image.ImageView;
import java.util.List;


/**
 * Classe abstraite Piece, extension d'ImageView, qui sera utilisée pour chaque pièce du jeu d'èchecs.
 * La couleur est représentée par un int : -1 pour les noirs, 1 pour les blancs
 */
public abstract class Piece extends ImageView {

    private int xTab;
    private int yTab;
    private String pieceType;
    private int color;


    /**
     * Constructeur d'une pièce, initialise les paramètres de base
     * @param xTab coordonnée d'apparition en X
     * @param yTab coordonnée d'apparition en Y
     * @param pieceType String du type de pièce (pawn, knight, ..)
     * @param color couleur de la pièce (-1 / 1)
     * @throws Exception Erreur si couleur non valide
     */
    public Piece(int xTab, int yTab, String pieceType, int color) throws Exception{
        if (color != 1 && color != -1) {
            throw new Exception("ERREUR Pion : couleur non valide");
        }

        this.pieceType = pieceType;
        this.xTab = xTab;
        this.yTab = yTab;
        this.color = color;

        super.setFitHeight(50);
        super.setPreserveRatio(true);
    }

    public String getPieceType() {
        return pieceType;
    }

    public int getColor() {
        return color;
    }

    public int getxTab() {
        return xTab;
    }

    public int getyTab() {
        return yTab;
    }
    public Tuple getCoords() {
        return new Tuple(xTab, yTab);
    }

    public void setxTab(int xTab) {
        this.xTab = xTab;
    }

    public void setyTab(int yTab) {
        this.yTab = yTab;
    }


    /**
     * Calcule tous les mouvements possible de la pièce
     * @param plateau plateau sur lequel donner les mouvements possibles
     * @return La liste de tous les mouvements possible de la pièce
     */
    public abstract List<Tuple> calculateMovements(Plateau plateau);


    /**
     * Vérifie si les coordonnées du mouvement fourni est un mouvement valide pour la pièce
     * @param x X cible
     * @param y Y cible
     * @param plateau Plateau sur lequel vérifier le mouvement
     * @return booléen, si le mouvement est valide ou non
     */
    public boolean isValidMovement(int x, int y, Plateau plateau) {
        Tuple destinationCoords = new Tuple(x, y);

        for(Tuple coords : this.calculateMovements(plateau)) {
            if (destinationCoords.equals(coords)) return true;
        }

        return false;
    }

    /**
     * renvoie true si la pièce fournie est attaquée par une autre
     * @param enemyTeam liste des pièces de l'équipe ennemie
     * @param plateau plateau sur lequel vérifier l'attaque
     * @return
     */
    public boolean isAttacked(Plateau plateau, List<Piece> enemyTeam) {
        Tuple pieceCoords = new Tuple(getxTab(), getyTab());

        for(Piece p : enemyTeam) {
            for(Tuple move : p.calculateMovements(plateau)) {
                if (move.equals(pieceCoords)) return true;
            }
        }

        return false;
    }

    public boolean canPieceMove(Plateau plateau) {
        return !this.calculateMovements(plateau).isEmpty();
    }

}
