module com.chessapp.chessapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.chessapp.chessapp to javafx.fxml;
    opens com.chessapp.chessapp.controller to javafx.fxml;
    opens com.chessapp.chessapp.model to javafx.fxml;
    exports com.chessapp.chessapp;
    opens com.chessapp.chessapp.model.chessPiece to javafx.fxml;
}