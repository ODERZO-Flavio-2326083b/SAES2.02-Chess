package com.chessapp.chessapp.controller;

import com.chessapp.chessapp.model.*;
import com.chessapp.chessapp.model.chessPiece.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Controlleur principal du jeu, gère la boucle du jeu, l'initialisation de la grille, ...
 */
public class GameController {

    private static final String squareOneColor = "#EBECD0";
    private static final String squareTwoColor = "#739552";
    private static final String squareOneRedColor = "#EB7D6A";
    private static final String squareTwoRedColor = "#D36C50";
    private static final String clickedSquareColor = "#F5F682";

    @FXML
    private Label labelPlayerOne;
    @FXML
    private Label labelPlayerTwo;
    @FXML
    private Label labelTimerPlyOne, labelTimerPlyTwo;
    @FXML
    private GridPane grid;
    @FXML
    private NewGameController newGameController; // le fxml place le controlleur lors de l'importation du fichier fxml NewGameTab.fxml
    @FXML
    private TournamentController tournamentTabController; // pareil

    private Piece movingPiece;
    private StackPane[][] cases;
    private Plateau plateau;

    private StackPane firstClickedPane;

    private boolean playingAgainstBot;
    private boolean tournamentGame;
    private boolean gameRunning;
    private int sourceX;
    private int sourceY;
    private int clickNumber;
    private int currentTurnColor;
    private List<Piece> blackPieces;
    private List<Piece> whitePieces;
    private King whiteKing;
    private King blackKing;

    private Timeline timerPlyOne;
    private Timeline timerPlyTwo;
    private int timePerTurnPlyOne, timeLeftPlyOne;
    private int timePerTurnPlyTwo, timeLeftPlyTwo;
    private String matchFileName;



    /**
     * Initialisation de la matrice de stackPanes, du plateau de jeu, et des events pour chaque case
     */
    @FXML
    public void initialize() {
        newGameController.setGameController(this);
        tournamentTabController.setGameController(this);
        tournamentTabController.setNewGameController(newGameController);

        try {
            initBoard();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * initialise l'échiéquier pour le jeu
     */
    public void initBoard() throws Exception {
        cases = new StackPane[8][8];
        plateau = new Plateau();
        clickNumber = 0;
        currentTurnColor = 1;

        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        for (int j = 0; j < 8; ++j) {
            for (int i = 0; i < 8; i++) {

                StackPane stackPane = new StackPane();

                String squareColor = ((i + j) % 2 == 0) ? squareOneColor : squareTwoColor;
                stackPane.setStyle("-fx-background-color: " + squareColor + ";");

                Piece piece = getPiece(j, i);

                if(piece != null) {
                    plateau.addPiece(i, j, piece);
                    stackPane.getChildren().add(piece);
                    if (piece.getColor() == 1) {
                        if(piece.getPieceType().equals("king")) whiteKing = (King) piece;
                        whitePieces.add(piece);
                    } else {
                        if(piece.getPieceType().equals("king")) blackKing = (King) piece;
                        blackPieces.add(piece);
                    }
                }

                stackPane.setOnMouseClicked(e -> {
                    try {
                        onMouseClicked(e, stackPane);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                cases[i][j] = stackPane;
                grid.add(stackPane, i, j);
            }
        }
    }

    /**
     * initialise les timers des deux joueurs
     */
    public void initTimers() {
        timeLeftPlyOne = timePerTurnPlyOne;
        timeLeftPlyTwo = timePerTurnPlyTwo;
        labelTimerPlyOne.setText(secondsToTimeFormat(timePerTurnPlyOne));
        labelTimerPlyTwo.setText(secondsToTimeFormat(timePerTurnPlyTwo));

        timerPlyOne = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                    if (timeLeftPlyOne > 0) --timeLeftPlyOne;
                    labelTimerPlyOne.setText(secondsToTimeFormat(timeLeftPlyOne));
                    if (timeLeftPlyOne <= 0) {
                        try {
                            playRandomMove();
                            timeLeftPlyOne = timePerTurnPlyOne;
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                })
        );

        timerPlyOne.setCycleCount(Timeline.INDEFINITE);

        timerPlyTwo = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (timeLeftPlyTwo > 0) --timeLeftPlyTwo;
            labelTimerPlyTwo.setText(secondsToTimeFormat(timeLeftPlyTwo));
            if (timeLeftPlyTwo <= 0) {
                try {
                    playRandomMove();
                    timeLeftPlyTwo = timePerTurnPlyTwo;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        })
        );

        timerPlyTwo.setCycleCount(Timeline.INDEFINITE);

        timerPlyOne.play();

    }

    /**
     * transforme un nombre de seconde en string de la forme
     * mm:ss
     * @param seconds temps en secondes
     * @return le string formaté
     */
    public String secondsToTimeFormat(int seconds) {
        int minutes = seconds / 60;
        int second = seconds % 60;
        return String.format("%02d:%02d", minutes, second);
    }

    /**
     * Fonction appellée quand le bouton de lancement est cliqué
     * @param playerOneName Nom du joueur blanc
     * @param playerTwoName Nom du joueur noir
     * @param playingAgainstBot si le joueur décide de jouer contre un bot ou pas
     * @param timePerTurn temps en secondes par tour
     */
    public void startGame(String playerOneName, String playerTwoName, boolean playingAgainstBot, int timePerTurn) throws Exception {
        this.playingAgainstBot = playingAgainstBot;
        labelPlayerOne.setText(playerOneName);
        labelPlayerTwo.setText(playerTwoName);
        gameRunning = true;
        tournamentGame = false;
        matchFileName = HistoriqueHandler.createName(playerOneName, playerTwoName);

        timePerTurnPlyOne = timePerTurn;
        timePerTurnPlyTwo = timePerTurn;
        System.out.println(timeLeftPlyOne + " " + timeLeftPlyTwo);
        initBoard();
        initTimers();
        System.out.println(timeLeftPlyOne + " " + timeLeftPlyTwo);
    }

    /**
     * Fonction appellée lors du début d'ne partie d'un tournoi
     * @param playerOneName Nom du joueur blanc
     * @param playerTwoName Nom du joueur noir
     * @param timePerTurn temps en minutes par tour
     */
    public void startTournamentGame(String playerOneName, String playerTwoName, int timePerTurn) throws Exception {
        this.playingAgainstBot = false;
        labelPlayerOne.setText(playerOneName);
        labelPlayerTwo.setText(playerTwoName);
        gameRunning = true;
        tournamentGame = true;

        timePerTurnPlyOne = timePerTurn;
        timePerTurnPlyTwo = timePerTurn;
        initBoard();
        initTimers();
    }

    /**
     * Donne la pièce correspondante à une case donnée au début de la partie
     * @param x coordonnée X
     * @param y coordonnée Y
     * @return la pièce correspondante à la case
     * @throws Exception si x, y invalides
     */
    private static Piece getPiece(int y, int x) throws Exception {
        Piece piece = null;

        if (y == 1) piece = new Pawn(x, y, -1);
        else if (y == 6) piece = new Pawn(x, y, 1);

        if (y == 0 || y == 7) {
            int color = (y == 0) ? -1 : 1;

            piece = switch (x) {
                case 0, 7 -> new Rook(x, y, color);
                case 1, 6 -> new Knight(x, y, color);
                case 2, 5 -> new Bishop(x, y, color);
                case 3 -> new Queen(x, y, color);
                case 4 -> new King(x, y, color);
                default -> piece;
            };
        }
        return piece;
    }

    /**
     * Appelle cette fonction lorsqu'on clique sur une case, elle permet de distinguer les deux phases de clics, la pièce bougée, l'affichage des mouvements.
     * Gère aussi le mouvement graphique et technique entre deux cases et sa validité
     * @param e L'event du clic
     * @param stackPane Le stackpane concerné
     */
    public void onMouseClicked(Event e, StackPane stackPane) throws IOException {

        if (!gameRunning) return;

        List<Tuple> availableMoves;
        King currentKing, enemyKing;
        List<Piece> enemyTeam;

        if (currentTurnColor == -1) {
            currentKing = blackKing;
            enemyKing = whiteKing;
            enemyTeam = whitePieces;
        } else {
            currentKing = whiteKing;
            enemyKing = blackKing;
            enemyTeam = blackPieces;
        }
        if (clickNumber == 0 && !stackPane.getChildren().isEmpty()) { // si c'est le premier clic, sur une case non vide, on initie le mouvement

            movingPiece = (Piece) stackPane.getChildren().get(0); // pièce qui doit être bougée

            if (currentKing.isAttacked(plateau, enemyTeam)) { // si le roi est attaqué
                if (!movingPiece.equals(currentKing)) return;

                if(!currentKing.canPieceMove(plateau)) {
                    endGame(currentTurnColor);
                    return;
                }
            }

            if (movingPiece.getColor() == currentTurnColor) {
                stackPane.setStyle("-fx-background-color: " + clickedSquareColor + ";");
                firstClickedPane = stackPane;
                clickNumber = 1;
                sourceX = GridPane.getColumnIndex((Node) e.getSource());
                sourceY = GridPane.getRowIndex((Node) e.getSource());

                if(!movingPiece.equals(currentKing))
                    availableMoves = plateau.getPiece(sourceX, sourceY).calculateMovements(plateau);
                else {
                    availableMoves = currentKing.calculateMovements(plateau);
                    // availableMoves = filterSuicideMoves(availableMoves, enemyTeam);
                    // essai de retirer les déplacements qui tuent le roi, ne fonctionne pas comme prévu
                }
                showAvailableMoves(availableMoves);
            }
            // System.out.println(availableMoves);

        }
        else if (clickNumber == 1) {

            int destX = GridPane.getColumnIndex((Node) e.getSource());
            int destY = GridPane.getRowIndex((Node) e.getSource());

            availableMoves = plateau.getPiece(sourceX, sourceY).calculateMovements(plateau);
            // System.out.println(sourceX + " " + sourceY + " " + destX + " " + destY);

            boolean moveIsValid = isMoveValid(availableMoves, sourceX, sourceY, destX, destY);

            if(moveIsValid) {

                stackPane.getChildren().setAll(movingPiece); // mouvement de la pièce visuellement

                Piece targetedPiece = plateau.getPiece(destX, destY);

                try {
                    plateau.movement(sourceX, sourceY, destX, destY);
                    HistoriqueHandler.ecritureHistorique(matchFileName, new Tuple(sourceX, sourceY), new Tuple(destX, destY));
                    // plateau.showGrid();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                if(targetedPiece != null) {
                    if (targetedPiece.equals(enemyKing)) {
                        endGame(currentTurnColor);
                        return;
                    } else enemyTeam.remove(targetedPiece);
                }

                switchTurn();
                if (playingAgainstBot && gameRunning) {
                    playRandomMove();
                }
            }

            // dans tous les cas on remet les couleurs comme avant
            String sourceColor = ((sourceX + sourceY) % 2 == 0) ? squareOneColor : squareTwoColor;
            firstClickedPane.setStyle("-fx-background-color: " + sourceColor + ";");

            firstClickedPane = null;
            stopShowingAvailableMoves(availableMoves);
            availableMoves.clear();

            clickNumber = 0;



        }
    }

    /**
     * Vérifie si le mouvement fourni est valide
     * @param availableMoves liste des mouvements possible de la pièce
     * @param oldX X source
     * @param oldY Y source
     * @param newX X destination
     * @param newY Y destination
     * @return Booléen
     */
    public boolean isMoveValid(List<Tuple> availableMoves, int oldX, int oldY, int newX, int newY) {
        Piece destinationPiece = plateau.getPiece(newX, newY);
        Piece sourcePiece = plateau.getPiece(oldX, oldY);

        if (destinationPiece != null && destinationPiece.getColor() == sourcePiece.getColor()) return false;

        for (Tuple tuple : availableMoves) {
            Tuple newCoords = new Tuple(newX, newY);
            if (tuple.equals(newCoords)) return true;
        }
        return false;
    }

    /**
     * Commence l'affichage des mouvements possibles
     * @param availableMoves liste des mouvements possibles
     */
    public void showAvailableMoves(List<Tuple> availableMoves) {
        int x, y;
        String color;
        for (Tuple coords : availableMoves) {
            x = (int) coords.getFirst();
            y = (int) coords.getSecond();
            color = ((x + y) % 2 == 0) ? squareOneRedColor : squareTwoRedColor;
            cases[x][y].setStyle("-fx-background-color: " + color + ";");
        }
    }

    /**
     * Arrête l'affichage des mouvements possibles
     * @param availableMoves liste des mouvements possibles
     */
    public void stopShowingAvailableMoves(List<Tuple> availableMoves) {
        int x, y;
        String color;
        for (Tuple coords : availableMoves) {
            x = (int) coords.getFirst();
            y = (int) coords.getSecond();
            color = ((x + y) % 2 == 0) ? squareOneColor : squareTwoColor;
            cases[x][y].setStyle("-fx-background-color: " + color + ";");
        }
    }

    /**
     * Renvoie une liste des mouvements que la pièce peut faire sans être en danger
     * @param availableMoves liste de tous les mouvements possibles
     * @param enemyTeam liste des pièces ennemies
     * @return liste des mouvements sans menaces
     */
    public List<Tuple> filterSuicideMoves(List<Tuple> availableMoves, List<Piece> enemyTeam) {
        List<Tuple> threatenedPositions = new ArrayList<>();

        for (Piece p : enemyTeam) {
            threatenedPositions.addAll(p.calculateMovements(plateau));
        }

        for (Tuple move : availableMoves){
            availableMoves.remove(move);
        }

        return availableMoves;
    }


    /**
     * Joue le mouvement de l'IA si l'option est choisie en début de partie, ou lorsque le temps du joueur est écoulé
     */
    public void playRandomMove() throws IOException {
        Random rand = new Random();
        Piece toMovePiece;
        Piece enemyKing;
        // pièce aléatoire
        if (currentTurnColor == -1) {
            toMovePiece = blackPieces.get(rand.nextInt(blackPieces.size()));
            enemyKing = whiteKing;
        } else {
            toMovePiece = whitePieces.get(rand.nextInt(whitePieces.size()));
            enemyKing = blackKing;
        }

        while (!toMovePiece.canPieceMove(plateau)) { // si la pièce est coincée, on choisit un autre
            if (currentTurnColor == -1)
                toMovePiece = blackPieces.get(rand.nextInt(blackPieces.size()));
            else
                toMovePiece = whitePieces.get(rand.nextInt(whitePieces.size()));

        }

        // on récupère le code de mouvement utilisé par les joueurs
        List<Tuple> allMoves = toMovePiece.calculateMovements(plateau);
        Tuple destCoords = allMoves.get(rand.nextInt(allMoves.size()));
        int randX = (int) destCoords.getFirst();
        int randY = (int) destCoords.getSecond();

        cases[randX][randY].getChildren().setAll(toMovePiece); // mouvement de la pièce visuellement

        if(plateau.getPiece(randX, randY) != null) {
            if (plateau.getPiece(randX, randY).equals(enemyKing)) {
                endGame(currentTurnColor);
            } else {
                if (currentTurnColor == -1)
                    whitePieces.remove(plateau.getPiece(randX, randY));
                else blackPieces.remove(plateau.getPiece(randX, randY));
            }
        }

        try {
            plateau.movement(toMovePiece.getxTab(), toMovePiece.getyTab(), randX, randY);
            HistoriqueHandler.ecritureHistorique(matchFileName, new Tuple(toMovePiece.getxTab(), toMovePiece.getyTab()), destCoords);
            // plateau.showGrid();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        switchTurn();
    }

    /**
     * termine la partie en cours, quand l'un des deux rois est capturé
     * @param winner couleur du gagnant
     */
    public void endGame(int winner) throws IOException {
        System.out.println("victoire " + winner);
        for (StackPane[] spList : cases) {
            for (StackPane sp : spList) {
                sp.setOnMouseClicked(null);
            }
        }

        if (tournamentGame) {
            tournamentTabController.matchEnded(winner);
        } else {
            newGameController.gameEnded(winner);
            gameRunning = false;
        }

        // on renomme le fichier avec gagnant en premier, perdant en second.
        String endMatchFileName = matchFileName.replace(labelPlayerOne.getText()+ "-" + labelPlayerTwo.getText()+ "-", "");
        File matchFile = new File("Data/Historique/" + matchFileName);
        if (winner == -1) {
            matchFile.renameTo(new File("Data/Historique/" + labelPlayerTwo.getText()+ "-" + labelPlayerOne.getText()+ "-" + endMatchFileName));
        } else {
            matchFile.renameTo(new File("Data/Historique/" + labelPlayerOne.getText()+ "-" + labelPlayerTwo.getText()+ "-" + endMatchFileName));
        }
    }

    /**
     * change le tour actuel, et permet de lancer un timer et de stopper l'autre
     */
    public void switchTurn() {
        currentTurnColor = currentTurnColor * -1;

        if (currentTurnColor == 1) {
            timerPlyOne.play();
            timerPlyTwo.stop();
        } else {
            timerPlyTwo.play();
            timerPlyOne.stop();
        }
    }

}
