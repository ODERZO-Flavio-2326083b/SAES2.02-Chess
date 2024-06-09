package com.chessapp.chessapp.model.chessPiece;

import com.chessapp.chessapp.model.Plateau;
import com.chessapp.chessapp.model.Tuple;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Knight, extension de Piece, représente le cavalier
 */
public class Knight extends Piece {

    public static Image knight_w = new Image("file:src/main/resources/com/chessapp/chessapp/img/knight_w.png");
    public static Image knight_b = new Image("file:src/main/resources/com/chessapp/chessapp/img/knight_b.png");


    public Knight(int xTab, int yTab, int color) throws Exception {
        super(xTab, yTab, "knight", color);

        if (color == -1) {
            super.setImage(knight_b);
        } else if (color == 1) {
            super.setImage(knight_w);
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
        ArrayList<Tuple> availableMovements = new ArrayList<>();

        int x, y, color;
        x = super.getxTab();
        y = super.getyTab();
        color = super.getColor();

        // toutes les positions possibles du cavalier à partir de la position (x, y) initiale
        int[][] possibleMoves = {
                {x + 2, y + 1},
                {x + 2, y - 1},
                {x - 2, y + 1},
                {x - 2, y - 1},
                {x + 1, y + 2},
                {x + 1, y - 2},
                {x - 1, y + 2},
                {x - 1, y - 2}
        };

        for (int[] move : possibleMoves) { // on vérifie toutes les positions
            if (!(move[0] < 0 || move[0] > 7 || move[1] < 0 || move[1] > 7)) { // tant qu'on ne dépasse pas
                if (plateau.getPiece(move[0], move[1]) == null || plateau.getPiece(move[0], move[1]).getColor() != color) // et qu'il n'y a pas de pièce alliée
                    availableMovements.add(new Tuple(move[0], move[1])); // le mouvement est valide
            }
        }

        return availableMovements;
    }
}
