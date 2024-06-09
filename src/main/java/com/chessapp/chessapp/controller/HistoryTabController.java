package com.chessapp.chessapp.controller;

import com.chessapp.chessapp.model.HistoriqueHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
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
    private TableView<MoveEntry> tableViewMoves;

    @FXML
    private TableColumn<MoveEntry, String> moveColumn;

    @FXML
    private VBox vboxTableView, vboxMovesView;

    private List<String[]> moves;

    @FXML
    private void initialize() {
        setupTable();
        loadHistories();
        setEvents();
        vboxTableView.setVisible(true);
        vboxMovesView.setVisible(false);
    }

    private void setupTable() {
        pseudo1Column.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPseudo1()));
        pseudo2Column.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPseudo2()));
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDate()));

        moveColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMove()));
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
                historyEntries.add(new HistoryEntry(pseudo1, pseudo2, date, file));
            }
        }

        tableViewHistory.setItems(historyEntries);
    }

    private void loadGame(String fileName) {
        moves = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("Data/Historique/" + fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                moves.add(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayMoves() {
        ObservableList<MoveEntry> moveEntries = FXCollections.observableArrayList();
        for (String[] move : moves) {
            String from = convertToChessNotation(move[0], move[1]);
            String to = convertToChessNotation(move[2], move[3]);
            String moveText = "Ancienne: " + from + " / Nouvelle: " + to;
            moveEntries.add(new MoveEntry(moveText));
        }
        tableViewMoves.setItems(moveEntries);
    }

    private String convertToChessNotation(String x, String y) {
        int xPos = Integer.parseInt(x);
        int yPos = Integer.parseInt(y);
        return "" + (char) ('A' + xPos) + (yPos + 1);
    }

    private void setEvents() {
        tableViewHistory.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && tableViewHistory.getSelectionModel().getSelectedItem() != null) {
                HistoryEntry selectedEntry = tableViewHistory.getSelectionModel().getSelectedItem();
                if (selectedEntry != null) {
                    loadGame(selectedEntry.getFileName());
                    displayMoves();
                    vboxTableView.setVisible(false);
                    vboxMovesView.setVisible(true);
                }
            }
        });

        vboxMovesView.setOnMouseClicked(e -> {
            stopShowingMoves();
        });
    }

    private void stopShowingMoves() {
        vboxMovesView.setVisible(false);
        vboxTableView.setVisible(true);
    }

    public static class HistoryEntry {
        private final String pseudo1;
        private final String pseudo2;
        private final String date;
        private final String fileName;

        public HistoryEntry(String pseudo1, String pseudo2, String date, String fileName) {
            this.pseudo1 = pseudo1;
            this.pseudo2 = pseudo2;
            this.date = date;
            this.fileName = fileName;
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

        public String getFileName() {
            return fileName;
        }
    }

    public static class MoveEntry {
        private final String move;

        public MoveEntry(String move) {
            this.move = move;
        }

        public String getMove() {
            return move;
        }
    }
}
