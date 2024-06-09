package com.chessapp.chessapp.controller;

import com.chessapp.chessapp.model.Player;
import com.chessapp.chessapp.model.PlayerHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

import javax.security.auth.callback.Callback;

/**
 * Controleur qui gère l'affichage du tableView des joueurs et de leur statistiques
 */
public class PlayerTableController {

    @FXML
    private TableView<Player> tableViewPlayer;
    @FXML
    private VBox vboxTableView, vboxDetailedStats;
    @FXML
    private Label detailedStatsName, detailedPercentageWin, detailedPercentageLose;
    @FXML
    private Label detailedWinCount, detailedLoseCount, detailedGameCount;
    @FXML
    private ProgressBar winrateBar;

    @FXML
    public void initialize() throws Exception {
        statsInit();
        setEvents();
        vboxTableView.setVisible(true);
        vboxDetailedStats.setVisible(false);
    }

    /**
     * Initialise les colonnes et leur propriétés de la tableView
     * @throws Exception
     */
    public void statsInit() throws Exception {


        TableColumn<Player, String> nameColumn = new TableColumn<>("Pseudo");
        TableColumn<Player, String> winRateColumn = new TableColumn<>("Taux de victoire");
        TableColumn<Player, String> gamesWonColumn = new TableColumn<>("Victoires");
        TableColumn<Player, String> gamesLostColumn = new TableColumn<>("Défaites");

        nameColumn.setCellValueFactory(player -> new SimpleStringProperty(player.getValue().getName()));
        winRateColumn.setCellValueFactory(player -> new SimpleStringProperty(Math.round(player.getValue().getWinRate() * 100) + " %"));
        gamesWonColumn.setCellValueFactory(player -> new SimpleStringProperty(player.getValue().getWins() + ""));
        gamesLostColumn.setCellValueFactory(player -> new SimpleStringProperty(player.getValue().getLosses() + ""));

        tableViewPlayer.getColumns().setAll(nameColumn, winRateColumn, gamesWonColumn, gamesLostColumn);
        tableViewPlayer.setPlaceholder(new Label("Aucun fichier joueur, allez jouer aux échecs"));

        ObservableList<Player> allPlayersData = FXCollections.observableArrayList(PlayerHandler.obtenirJoueurs());
        tableViewPlayer.setItems(allPlayersData);

    }

    /**
     * initialise les events lors du double-clic sur une entrée de la table
     */
    private void setEvents() {
        tableViewPlayer.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && tableViewPlayer.getSelectionModel().getSelectedItem() != null) {
                Player player = tableViewPlayer.getSelectionModel().getSelectedItem();
                showPlayerDetails(player);
            }
        });

        vboxDetailedStats.setOnMouseClicked(e -> {
            stopShowingPlayerDetails();
        });
    }

    /**
     * actualise les labels de chaque statistiques sur un joueur dans l'écran de statistiques détaillées
     * @param player le joueur en question
     */
    private void showPlayerDetails(Player player) {
        detailedLoseCount.setText(player.getLosses() + " défaites");
        detailedWinCount.setText(player.getWins() + " victoires");
        detailedPercentageLose.setText(Math.round(100 - player.getWinRate() * 100) + " %");
        detailedPercentageWin.setText(Math.round(player.getWinRate() * 100) + " %");
        detailedGameCount.setText(player.getGamesPlayed() + " jouées");
        detailedStatsName.setText(player.getName());
        winrateBar.setProgress(player.getWinRate());

        vboxTableView.setVisible(false);
        vboxDetailedStats.setVisible(true);
    }

    /**
     * arrête l'affichage de l'écran de statistiques détaillées
     */
    private void stopShowingPlayerDetails() {
        vboxDetailedStats.setVisible(false);
        vboxTableView.setVisible(true);
    }

}
