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
        vboxTableView.setVisible(true);
        vboxMovesView.setVisible(false);
        setupTable();
        loadHistories();
        setEvents();

    }

    private void setupTable() {
        pseudo1Column.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().pseudo1()));
        pseudo2Column.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().pseudo2()));
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().date()));

        moveColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().move()));
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
                    loadGame(selectedEntry.fileName());
                    displayMoves();
                    vboxTableView.setVisible(false);
                    vboxMovesView.setVisible(true);
                }
            }
        });

        tableViewMoves.setOnMouseClicked(e -> stopShowingMoves());
    }

    private void stopShowingMoves() {
        vboxMovesView.setVisible(false);
        vboxTableView.setVisible(true);
    }

    public record HistoryEntry(String pseudo1, String pseudo2, String date, String fileName) {
    }

    public record MoveEntry(String move) {
    }
}