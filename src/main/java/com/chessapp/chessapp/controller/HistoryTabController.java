package com.chessapp.chessapp.controller;

import com.chessapp.chessapp.model.HistoriqueHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

/**
 * Controleur chargé de l'affichage de l'historique des parties.
 */
public class HistoryTabController {

    @FXML
    private TableView<HistoryEntry> tableViewHistory;

    @FXML
    private TableColumn<HistoryEntry, String> pseudo1Column;

    @FXML
    private TableColumn<HistoryEntry, String> pseudo2Column;

    @FXML
    private TableColumn<HistoryEntry, String> dateColumn;

    @FXML
    private void initialize() {
        setupTable();
        loadHistories();
    }

    /**
     * initialise les colonnes de la tableView
     */
    private void setupTable() {
        pseudo1Column.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().pseudo1()));
        pseudo2Column.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().pseudo2()));
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().date()));
    }

    /**
     * charge les parties du dossier Data/Historique et les place dans le TableView
     */
    private void loadHistories() {
        List<String> historyFiles = HistoriqueHandler.obtenirHistoriques();
        ObservableList<HistoryEntry> historyEntries = FXCollections.observableArrayList();

        for (String file : historyFiles) {
            String[] parts = file.replace(".csv", "").split("-");
            if (parts.length >= 3) {
                String pseudo1 = parts[0];
                String pseudo2 = parts[1];
                String date = parts[2] + "/" + parts[3] + "/" + parts[4] + "/" + parts[5] + "h" + parts[6];
                historyEntries.add(new HistoryEntry(pseudo1, pseudo2, date));
            }
        }

        tableViewHistory.setItems(historyEntries);
    }

    /**
     * Classe simple utilisée pour l'implémentation des données des parties dans l'historique
     * @param pseudo1 pseudo du gagnant
     * @param pseudo2 pseudo du perdant
     * @param date date de la partie
     */
    public record HistoryEntry(String pseudo1, String pseudo2, String date) {
    }
}
