package com.chessapp.chessapp.model.chessPiece;

import com.chessapp.chessapp.model.Plateau;
import com.chessapp.chessapp.model.Tuple;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Bishop, extension de Piece, représente le fou
 */
public class Bishop extends Piece {


    public static Image bishop_w = new Image("file:src/main/resources/com/chessapp/chessapp/img/bishop_w.png");
    public static Image bishop_b = new Image("file:src/main/resources/com/chessapp/chessapp/img/bishop_b.png");

    /**
     * Constructeur d'une pièce, initialise les paramètres de base
     *
     * @param xTab      coordonnée d'apparition en X
     * @param yTab      coordonnée d'apparition en Y
     * @param color     couleur de la pièce (-1 / 1)
     * @throws Exception Erreur si couleur non valide
     */
    public Bishop(int xTab, int yTab, int color) throws Exception {
        super(xTab, yTab, "bishop", color);

        if (color == -1) {
            super.setImage(bishop_b);
        } else if (color == 1) {
            super.setImage(bishop_w);
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

        int[] gapX = {1, 1, -1, -1};
        int[] gapY = {-1, 1, 1, -1};

        for (int gap = 0; gap < 4; ++gap) {
            int newX = x + gapX[gap];
            int newY = y + gapY[gap];

            while (newX >= 0 && newX < 8 && newY >= 0 && newY < 8) {


                if (plateau.getPiece(newX, newY) != null) {
                    if (plateau.getPiece(newX, newY).getColor() != color) {
                        availableMovements.add(new Tuple(newX, newY));
                    } break;
                }

                availableMovements.add(new Tuple(newX, newY));

                newX += gapX[gap];
                newY += gapY[gap];
            }
        }

        return availableMovements;
    }
}
