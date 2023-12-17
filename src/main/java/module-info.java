module com.example.chess {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.chess to javafx.fxml;
    exports com.example.chess;
    exports com.example.chess.board;
    opens com.example.chess.board to javafx.fxml;
}