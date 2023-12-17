package com.example.chess;

import com.example.chess.board.Board;
import com.example.chess.board.Cell;
import com.example.chess.figures.Color;
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
    static String whiteMoveText = "White's move";
    static String blackMoveText = "Black's move";
    static String whiteWinsText = "WHITE WINS!";
    static String blackWinsText = "BLACK WINS!";
    static Image dot = null;

    @Override
    public void start(Stage stage) throws IOException {
        dot = new Image(Objects.requireNonNull(ChessApplication.class.getResourceAsStream("dot.png")));
        stage.setTitle("Chess game");
        primaryStage = stage;

        board = new Board();
        Cell[][] cells = board.getCells();
        boardPane = new Pane[8][8];
        FXMLLoader fxmlLoader = new FXMLLoader(ChessApplication.class.getResource("chess-view.fxml"));
        GridPane boardGrid = fxmlLoader.load();
        ChessController chessController = fxmlLoader.getController();
        chessController.openDialog();
        if (chessController.getCurGameMode().equals(GameMode.ONE_PLAYER)) {
            if (Math.random() < 0.5) {
                whiteMoveText = "Player's move";
                blackMoveText = "AI's move";
                whiteWinsText = "PLAYER WINS!";
                blackWinsText = "AI WINS!";
                chessController.setRealPlayerTeam(Color.WHITE);
                chessController.setAiPlayer(new AiPlayer(Color.BLACK));
            } else {
                whiteMoveText = "AI's move";
                blackMoveText = "Player's move";
                whiteWinsText = "AI WINS!";
                blackWinsText = "PLAYER WINS!";
                chessController.setRealPlayerTeam(Color.BLACK);
                chessController.setAiPlayer(new AiPlayer(Color.WHITE));
            }
        }
        initRootLayout();
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
        if (chessController.getCurGameMode().equals(GameMode.ONE_PLAYER)) {
            chessController.statusHandler(board.getCurrentStatus());
        }
    }

    public void initRootLayout() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ChessApplication.class.getResource("root-layout-view.fxml"));
        rootLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(rootLayout));
        primaryStage.show();

        whiteLabel = new Label(whiteMoveText);
        whiteLabel.setStyle("-fx-font-size: 72px");
        rootLayout.setBottom(new StackPane(whiteLabel));

        blackLabel = new Label(blackMoveText);
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