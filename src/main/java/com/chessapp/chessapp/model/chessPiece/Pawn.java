package com.chessapp.chessapp.model.chessPiece;

import com.chessapp.chessapp.model.Plateau;
import com.chessapp.chessapp.model.Tuple;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Pion, extension de Piece, représente le pion de base des échecs
 */
public class Pawn extends Piece {

    public static Image pawn_w = new Image("file:src/main/resources/com/chessapp/chessapp/img/pawn_w.png");
    public static Image pawn_b = new Image("file:src/main/resources/com/chessapp/chessapp/img/pawn_b.png");

    public Pawn(int xTab, int yTab, int color) throws Exception {
        super(xTab, yTab, "pawn", color);

        if (color == -1) {
            super.setImage(pawn_b);
        } else if (color == 1) {
            super.setImage(pawn_w);
        }
    }

    @Override
    public List<Tuple> calculateMovements(Plateau plateau){
        ArrayList<Tuple> availableMovements = new ArrayList<>();
        
        int x, y, color;
        
        x = super.getxTab();
        y = super.getyTab();
        color = super.getColor();
        
        int direction = color * -1;

        if (y + direction >= 0 && y + direction < 8 && plateau.getPiece(x, y + direction) == null) { // déplacement tout droit
            availableMovements.add(new Tuple(x, y + direction));
            if (color == -1 && y == 1 && plateau.getPiece(x, y + 2 * direction) == null || color == 1 && y == 6 && plateau.getPiece(x, y + 2 * direction) == null){

                    availableMovements.add(new Tuple(x, y + 2 * direction));
                }

        }

        if (x - 1 >= 0 && y + direction >= 0 && y + direction < 8 // si la diagonale avant gauche ne dépasse pas le tableau
                && plateau.getPiece(x - 1, y + direction) != null // et qu'il y a une pièce à l'avant gauche
                && plateau.getPiece(x - 1, y + direction).getColor() != color) { // et que sa couleur est différente
            availableMovements.add(new Tuple(x - 1, y + direction));
        }

        if (x + 1 < 8 && y + direction >= 0 && y + direction < 8 // si la diagonale avant droite ne dépasse pas
                && plateau.getPiece(x + 1, y + direction) != null // et qu'il y a une pièce
                && plateau.getPiece(x + 1, y + direction).getColor() != super.getColor()) { // et que sa couleur est différente
            availableMovements.add(new Tuple(x + 1, y + direction));
        }

        return availableMovements;
    }


}
