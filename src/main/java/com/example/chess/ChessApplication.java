package com.example.chess;

import com.example.chess.figures.Board;
import com.example.chess.figures.Cell;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ChessApplication extends Application {
    static Stage primaryStage;
    static BorderPane rootLayout;
    static Board board;
    static Pane[][] boardPane;
    static String WHITE_CELL_STYLE = "-fx-background-color: rgb(255,255,255);";
    static String BLACK_CELL_STYLE = "-fx-background-color: rgb(136, 184, 140);";
    static String SELECTED_CELL_STYLE = "-fx-background-color: rgb(232, 229, 132);";
    static String ATTACK_CELL_STYLE = "-fx-background-color: rgb(219, 127, 127);";

    static Label whiteLabel = null;
    static Label blackLabel = null;
    static Image dot = null;

    @Override
    public void start(Stage stage) throws IOException {
        dot = new Image(Objects.requireNonNull(ChessApplication.class.getResourceAsStream("dot.png")));
        stage.setTitle("Chess game");
        primaryStage = stage;

        initRootLayout();
        board = new Board();
        Cell[][] cells = board.getCells();
        boardPane = new Pane[8][8];
        FXMLLoader fxmlLoader = new FXMLLoader(ChessApplication.class.getResource("chess-view.fxml"));
        GridPane boardGrid = fxmlLoader.load();
        for (Node node : boardGrid.getChildren()) {
            Integer rowObj = GridPane.getRowIndex(node);
            Integer colObj = GridPane.getColumnIndex(node);

            int row = rowObj == null ? 0 : rowObj;
            int col = colObj == null ? 0 : colObj;
            row = 7 - row;
            if (node instanceof Pane) {
                boardPane[row][col] = (Pane) node;
            }

            if (node instanceof Pane) {
                ImageView imageView;
                imageView = cells[row][col].getPiece() != null ?
                        new ImageView(cells[row][col].getPiece().getImage()) : new ImageView();
                imageView.setFitHeight(60);
                imageView.setFitWidth(60);
                ((Pane) node).getChildren().add(imageView);
            }
        }
        clearSelection(boardGrid);
        rootLayout.setCenter(boardGrid);
    }

    public void initRootLayout() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ChessApplication.class.getResource("root-layout-view.fxml"));
        rootLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(rootLayout));
        primaryStage.show();

        whiteLabel = new Label("White's move");
        whiteLabel.setStyle("-fx-font-size: 72px");
        rootLayout.setBottom(new StackPane(whiteLabel));

        blackLabel = new Label("Black's move");
        blackLabel.setStyle("-fx-font-size: 72px");
        blackLabel.setVisible(false);
        rootLayout.setTop(new StackPane(blackLabel));
    }

    public static void main(String[] args) {
        launch();
    }

    public static void clearSelection(GridPane boardGrid) {
        for (Node node : boardGrid.getChildren()) {
            Integer rowObj = GridPane.getRowIndex(node);
            Integer colObj = GridPane.getColumnIndex(node);

            int row = rowObj == null ? 0 : rowObj;
            int col = colObj == null ? 0 : colObj;
            row = 7 - row;
            if ((row+col)%2 == 1) {
                node.setStyle(ChessApplication.WHITE_CELL_STYLE);
            } else {
                node.setStyle(ChessApplication.BLACK_CELL_STYLE);
            }
        }
        ChessController.updateBoard();
    }
}