package com.chessapp.chessapp.controller;

import com.chessapp.chessapp.model.Match;
import com.chessapp.chessapp.model.Player;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class TournamentController {

    @FXML
    private Label tournamentCurrentMatch;
    @FXML
    private Label tournamentCurrentRound;
    @FXML
    private Label tournamentInfoLabel;
    @FXML
    private TextField tournamentPlayersTextField;
    @FXML
    private Button tournamentStartButton;

    private GameController gameController;
    private NewGameController newGameController;

    private BooleanProperty tournamentRunning;
    private List<Player> allPlayers;
    private List<Player> nextRoundPlayers;
    private List<Match> allMatches;
    private int matchNumber, roundNumber;

    @FXML
    private void initialize() {
        tournamentInfoLabel.setText("Merci de rentrer tous les joueurs du tournoi au dessus, séparés par des virgules.");
        tournamentCurrentMatch.setText("");
        tournamentCurrentRound.setText("");
        tournamentRunning = new SimpleBooleanProperty(false);
        createBindings();
    }

    private void createBindings() {
        tournamentStartButton.disableProperty().bind(tournamentRunning);
        tournamentPlayersTextField.editableProperty().bind(tournamentRunning.not());
    }

    /**
     * démarre le tournoi
     * il est bon de noter que le tournoi ne peut pas être interrompu sans éteindre l'application
     * de plus, les joueurs qui n'ont pas de fichier .csv seront ignorés
     */
    @FXML
    public void beginTournament() throws Exception {
        tournamentRunning.set(true);
        allPlayers = new ArrayList<>();
        allMatches = new ArrayList<>();
        nextRoundPlayers = new ArrayList<>();
        matchNumber = 0;
        roundNumber = 0;
        // cette ligne ci dessous est moche (c'est loin d'être la seule), mais elle fait plusieurs choses :
        // elle prend le texte entré par l'utilisateur,
        // retire tous les espaces,
        // et utilise les virgules pour former une liste de noms de joueurs
        List<String> allPlayerNames = List.of(tournamentPlayersTextField.getText().replaceAll("\\s", "").split(","));

        for (String playerName : allPlayerNames) {
            try {
                allPlayers.add(new Player(playerName));
            } catch (Exception e) {
                System.err.println(String.format("%s.csv m'existe pas, il ne sera pas présent dans le tournoi", playerName));
            }

        }
        roundNumber = 1;
        tournamentCurrentRound.setText("ROUND " + roundNumber);

        generateMatches();
        playCurrentMatch();
    }

    /**
     * affiche le match actuel à l'écran, ou le grand gagnant si la liste des joueurs est de taille 1
     */
    public void playCurrentMatch() {
        if(matchNumber >= allMatches.size()) {
            if (allPlayers.size() == 1) {
                tournamentCurrentMatch.setText("Grand gagnant : " + allPlayers.get(0).getName());
                tournamentRunning.set(false);
            } else {
                nextRound();
            }
        } else {
            Match currentMatch = allMatches.get(matchNumber);
            tournamentCurrentMatch.setText(String.format("%s VS %s", currentMatch.getPlyOne().getName(), currentMatch.getPlyTwo().getName()));
            if (currentMatch.getPlyOne().getName().equals("SKIP"))
                matchEnded(-1);
            else if (currentMatch.getPlyTwo().getName().equals("SKIP"))
                matchEnded(1);
            else {
                try {
                    gameController.startTournamentGame(currentMatch.getPlyOne().getName(), currentMatch.getPlyTwo().getName(), 10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * génère tous les objets Matchs du tournoi
     * si le nombre de joueurs est impair, le dernier joueur de la liste passe au prochain tour automatiquement
     */
    public void generateMatches() throws Exception {
        allMatches.clear();
        if (allPlayers.size() == 1) {
            return;
        } else {
            if (allPlayers.size() % 2 == 1) {
                allPlayers.add(new Player("SKIP"));
            }
            for (int i = 0; i < allPlayers.size(); i += 2) {
                allMatches.add(new Match(allPlayers.get(i), allPlayers.get(i + 1)));
            }
        }
    }

    /**
     * termine le match en cours, ajoute le gagnant au prochain tour.
     * @param winner couleur du gagnant
     */
    public void matchEnded(int winner) {
        if (matchNumber < allMatches.size()) {
            Match currentMatch = allMatches.get(matchNumber);
            if (winner == 1) {
                nextRoundPlayers.add(currentMatch.getPlyOne());
                tournamentInfoLabel.setText(String.format("Le gagnant de ce match est %s !", currentMatch.getPlyOne().getName()));
            } else if (winner == -1) {
                nextRoundPlayers.add(currentMatch.getPlyTwo());
                tournamentInfoLabel.setText(String.format("Le gagnant de ce match est %s !", currentMatch.getPlyTwo().getName()));
            }
        }
        ++matchNumber;
        playCurrentMatch();
    }

    public void nextRound() {
        ++roundNumber;
        tournamentCurrentRound.setText("ROUND " + roundNumber);
        matchNumber = 0;
        allPlayers = nextRoundPlayers;
        nextRoundPlayers = new ArrayList<>();
        try {
            generateMatches();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        playCurrentMatch();
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void setNewGameController(NewGameController newGameController) {
        this.newGameController = newGameController;
    }

    public NewGameController getNewGameController() {
        return newGameController;
    }
}
