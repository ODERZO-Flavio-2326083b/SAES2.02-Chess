package com.chessapp.chessapp.controller;

import com.chessapp.chessapp.model.PlayerHandler;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;

/**
 * Contrôleur pour la création d'une nouvelle partie.
 */
public class NewGameController {

    private GameController gameController;

    @FXML
    private TextField textFieldPlayerOne;
    @FXML
    private TextField textFieldPlayerTwo;
    @FXML
    private Button playButton;
    @FXML
    private Button importPlayerOneButton;
    @FXML
    private Button importPlayerTwoButton;
    @FXML
    private Label timeLabel;
    @FXML
    private TextField timeTextField;
    @FXML
    private Label infoLabel;
    @FXML
    private CheckBox botCheckbox;

    private String playerOneName, playerTwoName;
    private BooleanProperty tournamentRunning;
    private BooleanProperty gameRunning;
    private BooleanProperty playerOneImported;
    private BooleanProperty playerTwoImported;
    private BooleanProperty playingAgainstBot;
    private int gameTime = 600;

    /**
     * Initialisation du contrôleur de la nouvelle partie.
     */
    @FXML
    private void initialize() throws IOException {
        infoLabel.setText("Merci d'importer vos pseudos (3 chars min.)");

        createBindings();
    }

    /**
     * Définit le temps de jeu à partir d'un texte.
     * @param timeText Le texte représentant le temps (mm:ss).
     * @return Le temps en secondes.
     */
    private int parseTime(String timeText) {
        try {
            String[] parts = timeText.split(":");
            int minutes = Integer.parseInt(parts[0]);
            int seconds = Integer.parseInt(parts[1]);
            return minutes * 60 + seconds;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return 600; // Défaut de 10 minutes en cas d'erreur de parsing
        }
    }

    /**
     * Vérifie si le format de temps est valide.
     * @param timeText Le texte représentant le temps (mm:ss).
     * @return Vrai si le format est valide, faux sinon.
     */
    private boolean isValidTimeFormat(String timeText) {
        if (timeText == null || !timeText.matches("^\\d{1,2}:\\d{2}$")) {
            return false;
        }

        String[] parts = timeText.split(":");
        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);

        return minutes >= 0 && minutes <= 99 && seconds >= 0 && seconds < 60;
    }

    /**
     * Méthode appelée lors du clic sur l'image du temps pour le modifier.
     */
    @FXML
    private void onTimeImageClick() {
        String newTime = timeTextField.getText();
        if (isValidTimeFormat(newTime) && parseTime(newTime) >= 10) {
            gameTime = parseTime(newTime);
            timeLabel.setText(newTime);
        } else {
            infoLabel.setText("Format de temps invalide. Utilisez mm:ss avec minimum 10 secondes");
        }
    }

    /**
     * Lance la partie.
     * @throws Exception en cas d'erreur lors du lancement.
     */
    @FXML
    public void startGame() throws Exception {

        gameRunning.set(true);
        gameController.startGame(playerOneName, playerTwoName, playingAgainstBot.get(), gameTime);
        infoLabel.setText("Partie commencée, bonne chance !");
    }

    /**
     * Importe le joueur 1 lorsque le bouton d'importation du joueur 1 est cliqué
     */
    @FXML
    public void importPlayerOne() {
        playerOneName = textFieldPlayerOne.getText();
        try {
            PlayerHandler.verficationJoueur(
                    playerOneName.toLowerCase().replaceAll("\\s", ""));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        infoLabel.setText(String.format("Joueur 1 (%s) importé avec succès.", playerOneName));

        playerOneImported.set(true);
    }

    /**
     * Importe le joueur 2 lorsque le bouton d'importation du joueur 2 est cliqué
     */
    @FXML
    public void importPlayerTwo() {
        playerTwoName = textFieldPlayerTwo.getText();
        try {
            PlayerHandler.verficationJoueur(
                    playerTwoName.toLowerCase().replaceAll("\\s", ""));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        infoLabel.setText(String.format("Joueur 2 (%s) importé avec succès.", playerTwoName));

        playerTwoImported.set(true);
    }

    /**
     * crée les bindings nécessaire au bon fonctionnement de l'ihm
     */
    public void createBindings(){

        gameRunning = new SimpleBooleanProperty(false);
        playerOneImported = new SimpleBooleanProperty(false);
        playerTwoImported = new SimpleBooleanProperty(false);
        playingAgainstBot = new SimpleBooleanProperty(false);
        tournamentRunning = new SimpleBooleanProperty(false);

        BooleanBinding isTextfieldPlyOneAvailable = new BooleanBinding() {
            {
                this.bind(textFieldPlayerOne.textProperty());
            }
            @Override
            protected boolean computeValue() {
                return textFieldPlayerOne.getText().length() > 2;
            }
        };

        BooleanBinding isTextfieldPlyTwoAvailable = new BooleanBinding() {
            {
                this.bind(textFieldPlayerTwo.textProperty(), botCheckbox.selectedProperty());
            }
            @Override
            protected boolean computeValue() {
                return textFieldPlayerTwo.getText().length() > 2 && !botCheckbox.isSelected();
            }
        };

        BooleanBinding checkPlayButtonAvailable = new BooleanBinding() {
            {
                this.bind(playerOneImported, playerTwoImported, playingAgainstBot, tournamentRunning);
            }

            @Override
            protected boolean computeValue() {
                return playerOneImported.get() && (playerTwoImported.get() || playingAgainstBot.get()) && !gameRunning.get() && !tournamentRunning.get();
            }
        };

        BooleanBinding checkBotCheckbox = new BooleanBinding() {
            {
                this.bind(botCheckbox.selectedProperty());
            }

            @Override
            protected boolean computeValue() {
                playerTwoName = "BOT";
                return botCheckbox.isSelected();
            }
        };

        playingAgainstBot.bind(checkBotCheckbox);

        importPlayerOneButton.disableProperty().bind(isTextfieldPlyOneAvailable.not());
        importPlayerTwoButton.disableProperty().bind(isTextfieldPlyTwoAvailable.not());

        playButton.disableProperty().bind(checkPlayButtonAvailable.not());
    }


    /**
     * est appelée lorsque la partie se termine, modifie les fichiers des joueurs et permet au joueur de relancer la partie
     * @param winner -1 / 1
     * @throws IOException si erreur lors de la lecture des fichiers
     */
    public void gameEnded(int winner) throws IOException {
        gameRunning.set(false);

        infoLabel.setText(String.format("Victoire de %s! Vous pouvez à présent relancer une partie.",
                (winner == 1) ? playerOneName : playerTwoName));

        if (!playingAgainstBot.get()) {
            if (winner == 1) {
                PlayerHandler.finPartie(playerOneName, playerTwoName);
            } else {
                PlayerHandler.finPartie(playerTwoName, playerOneName);
            }
        }
    }

    /**
     * importe le controlleur principal du jeu
     * @param gameController
     */
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        System.out.println(gameController);
    }

    public GameController getGameController() {
        return gameController;
    }

    /**
     * importe le controlleur de tournois, nécessaire pour bloquer le bouton jouer pendant le déroulé d'un tournoi
     * @param tournamentRunning
     */
    public void setTournamentRunning(boolean tournamentRunning) {
        this.tournamentRunning.set(tournamentRunning);
    }
}
