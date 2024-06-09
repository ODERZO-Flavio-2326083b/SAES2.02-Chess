package com.chessapp.chessapp.model.chessPiece;

import com.chessapp.chessapp.model.Plateau;
import com.chessapp.chessapp.model.Tuple;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {

    public static Image rook_w = new Image("file:src/main/resources/com/chessapp/chessapp/img/rook_w.png");
    public static Image rook_b = new Image("file:src/main/resources/com/chessapp/chessapp/img/rook_b.png");

    /**
     * Constructeur d'une pièce, initialise les paramètres de base
     *
     * @param xTab  coordonnée d'apparition en X
     * @param yTab  coordonnée d'apparition en Y
     * @param color couleur de la pièce (-1 / 1)
     * @throws Exception Erreur si couleur non valide
     */
    public Rook(int xTab, int yTab, int color) throws Exception {
        super(xTab, yTab, "rook", color);

        if (color == -1) {
            super.setImage(rook_b);
        } else if (color == 1) {
            super.setImage(rook_w);
        }
    }


    /**
     * Calcule tous les mouvements possible de la pièce
     *
     * @param plateau plateau sur lequel donner les mouvements possibles
     * @return La liste de tous les mouvements possible de la pièce
     */
    @Override
    public List<Tuple> calculateMovements(Plateau plateau) {
        List<Tuple> availableMovements = new ArrayList<>();

        int x, y, color;
        x = super.getxTab();
        y = super.getyTab();
        color = super.getColor();

        // décalages X et Y du mouvement
        int[] gapX = {0, 0, -1, 1};
        int[] gapY = {-1, 1, 0, 0};

        for (int gap = 0; gap < 4; ++gap) {
            int newX = x + gapX[gap]; // En additionnant les valeurs initialisées plus tôt, on se déplace dans une direction
            int newY = y + gapY[gap];

            while (newX >= 0 && newX < 8 && newY >= 0 && newY < 8) { // on effectue le déplacement soit jusqu'à dépasser..

                if (plateau.getPiece(newX, newY) != null) { // soit jusqu'à rencontrer une autre pièce..
                    if (plateau.getPiece(newX, newY).getColor() != color) { // et si cette pièce est ennemie, on peut la prendre
                        availableMovements.add(new Tuple(newX, newY));
                    }
                    break;
                }

                availableMovements.add(new Tuple(newX, newY));

                newX += gapX[gap]; // incrémentation du déplacement
                newY += gapY[gap];
            }
        }

        return availableMovements;
    }
}
