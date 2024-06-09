package com.chessapp.chessapp.controller;

import com.chessapp.chessapp.model.HistoriqueHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

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

    private void setupTable() {
        pseudo1Column.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPseudo1()));
        pseudo2Column.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPseudo2()));
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDate()));
    }

    private void loadHistories() {
        List<String> historyFiles = HistoriqueHandler.obtenirHistoriques();
        ObservableList<HistoryEntry> historyEntries = FXCollections.observableArrayList();

        for (String file : historyFiles) {
            String[] parts = file.replace(".csv", "").split("-");
            if (parts.length >= 3) {
                String pseudo1 = parts[0];
                String pseudo2 = parts[1];
                String date = parts[2] + "/" + parts[3] + "/" + parts[4];
                historyEntries.add(new HistoryEntry(pseudo1, pseudo2, date));
            }
        }

        tableViewHistory.setItems(historyEntries);
    }

    public static class HistoryEntry {
        private final String pseudo1;
        private final String pseudo2;
        private final String date;

        public HistoryEntry(String pseudo1, String pseudo2, String date) {
            this.pseudo1 = pseudo1;
            this.pseudo2 = pseudo2;
            this.date = date;
        }

        public String getPseudo1() {
            return pseudo1;
        }

        public String getPseudo2() {
            return pseudo2;
        }

        public String getDate() {
            return date;
        }
    }
}
