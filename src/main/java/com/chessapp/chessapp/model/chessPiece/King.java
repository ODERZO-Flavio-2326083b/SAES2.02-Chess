package com.chessapp.chessapp.model.chessPiece;

import com.chessapp.chessapp.model.Plateau;
import com.chessapp.chessapp.model.Tuple;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant le roi
 */
public class King extends Piece {

    public static Image king_w = new Image("file:src/main/resources/com/chessapp/chessapp/img/king_w.png");
    public static Image king_b = new Image("file:src/main/resources/com/chessapp/chessapp/img/king_b.png");

    /**
     * Constructeur d'une pièce, initialise les paramètres de base
     *
     * @param xTab      coordonnée d'apparition en X
     * @param yTab      coordonnée d'apparition en Y
     * @param color     couleur de la pièce (-1 / 1)
     * @throws Exception Erreur si couleur non valide
     */
    public King(int xTab, int yTab, int color) throws Exception {
        super(xTab, yTab, "king", color);

        if (color == -1) {
            super.setImage(king_b);
        } else if (color == 1) {
            super.setImage(king_w);
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

        // toutes les positions possibles du roi à partir de la position (x, y) initiale
        int[][] possibleMoves = {
                {x + 1, y + 1},
                {x + 1, y - 1},
                {x - 1, y - 1},
                {x - 1, y + 1},
                {x    , y + 1},
                {x    , y - 1},
                {x - 1, y    },
                {x + 1, y    },
        };

        for (int[] move : possibleMoves) { // on vérifie toutes les positions
            if (!(move[0] < 0 || move[0] > 7 || move[1] < 0 || move[1] > 7)) { // tant qu'on ne dépasse pas
                if (plateau.getPiece(move[0], move[1]) == null || plateau.getPiece(move[0], move[1]).getColor() != color) // et qu'il n'y a pas de pièce ennemie
                    availableMovements.add(new Tuple(move[0], move[1])); // le mouvement est valide
            }
        }
        return availableMovements;
    }

}
