package com.example.chess;

import com.example.chess.board.Board;
import com.example.chess.board.Cell;
import com.example.chess.board.GameStatus;
import com.example.chess.figures.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.chess.ChessApplication.*;

public class ChessController {


    static private Pane[][] boardPane = ChessApplication.boardPane;
    static private Board board = ChessApplication.board;
    private AiPlayer aiPlayer;
    static private Cell[][] cells = board.getCells();
    private Piece curPiece = null;
    private Color curTeam = Color.WHITE;
    private Color realPlayerTeam = null;
    private GameMode curGameMode = null;

    @FXML
    public GridPane boardGrid;


    public void onMouseClick(MouseEvent e) {
        double height = boardGrid.getHeight();
        double width = boardGrid.getWidth();
        int col = (int) (e.getX() / width * 8);
        int row = (int) (e.getY() / height * 8);
        row = 7 - row;
        if (!Cell.verifyPos(col) || !Cell.verifyPos(row)) return;

        if (curPiece == null) {
            select(row, col);
            return;
        }

        if (curPiece.moveTo(cells[row][col], board)) {
            onMoveUpdate();
        } else {
            curPiece = null;
            ChessApplication.clearSelection(boardGrid);
        }
    }
    private void onMoveUpdate() {
        // pieces on board update, change team;
        updateBoard();
        ChessApplication.clearSelection(boardGrid);
        curPiece = null;
        curTeam = curTeam.opposite();
        // status update
        board.updateCurrentStatus(curTeam);
        statusHandler(board.getCurrentStatus());
    }
    public static void updateBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                putPieceInPane(boardPane[i][j], cells[i][j].getPiece());
            }
        }
    }

    public void performAiMove() {
        if (curGameMode.equals(GameMode.ONE_PLAYER) && curTeam.equals(aiPlayer.getTeam())) {
            aiPlayer.performMove(board);
            onMoveUpdate();
        }
    }

    public void statusHandler(GameStatus status) {
        switch (status) {
            case BLACKS_MOVE -> {
                ChessApplication.whiteLabel.setVisible(false);
                ChessApplication.blackLabel.setText(blackMoveText);
                ChessApplication.blackLabel.setVisible(true);
                performAiMove();
            }
            case WHITES_MOVE -> {
                ChessApplication.blackLabel.setVisible(false);
                ChessApplication.whiteLabel.setText(whiteMoveText);
                ChessApplication.whiteLabel.setVisible(true);
                performAiMove();
            }
            case DRAW -> putLabelInCenter(ChessApplication.rootLayout, "DRAW");
            case BLACK_CHECK ->{
                ChessApplication.whiteLabel.setVisible(false);
                ChessApplication.blackLabel.setText("CHECK!");
                ChessApplication.blackLabel.setVisible(true);
                performAiMove();
            }
            case WHITE_CHECK -> {
                ChessApplication.blackLabel.setVisible(false);
                ChessApplication.whiteLabel.setText("CHECK!");
                ChessApplication.whiteLabel.setVisible(true);
                performAiMove();
            }
            case BLACK_CHECK_AND_MATE -> {
                putLabelInCenter(ChessApplication.rootLayout, whiteWinsText);
                ChessApplication.whiteLabel.setVisible(false);
                ChessApplication.blackLabel.setVisible(false);

            }
            case WHITE_CHECK_AND_MATE -> {
                putLabelInCenter(ChessApplication.rootLayout, blackWinsText);
                ChessApplication.whiteLabel.setVisible(false);
                ChessApplication.blackLabel.setVisible(false);
            }
        }
    }
    public static void putLabelInCenter(BorderPane borderPane, String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 72px");
        borderPane.setCenter(new StackPane(label));
    }

    public void select(int row, int col) {
        if (!cells[row][col].isOccupied()) return;
        if (cells[row][col].getPiece().getTeam() != curTeam) {
            return;
        }
        curPiece = cells[row][col].getPiece();
        boardPane[row][col].setStyle(ChessApplication.SELECTED_CELL_STYLE);
        ArrayList<Cell> toMove = curPiece.getCellsToMove(board);
        curPiece.removeExtra(toMove, board);
        for (Cell itr : toMove) {
            if (!itr.isOccupied())
                for (Node node : boardPane[itr.getRow()][itr.getCol()].getChildren()) {
                    if (node instanceof ImageView) {
                        ((ImageView) node).setImage(ChessApplication.dot);
                    }
                }
        }
        toMove = curPiece.getCellsToAttack(board);
        curPiece.removeExtra(toMove, board);
        for (Cell itr : toMove) {
            if (itr.isOccupiedBy(curTeam.opposite()))
                boardPane[itr.getRow()][itr.getCol()].setStyle(ChessApplication.ATTACK_CELL_STYLE);
        }
    }

    public static void removePieceFromPane(Pane pane) {
        Rectangle rectangle = null;
        for (Node node : pane.getChildren()) {
            if (node instanceof ImageView) {
                ((ImageView) node).setImage(null);
            }
            if (node instanceof Rectangle) {
                rectangle = (Rectangle) node;
            }
        }
        pane.getChildren().removeAll(rectangle);
    }

    private static void putPieceInPane(Pane pane, Piece piece) {
        removePieceFromPane(pane);
        if (piece == null) return;
        for (Node node : pane.getChildren()) {
            if (node instanceof ImageView) {
                Image image = piece.getImage();
                ((ImageView) node).setImage(image);
            }
        }
    }

    void openDialog() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("choose-mode-view.fxml"));
        Parent parent = fxmlLoader.load();
        ChooseModeController dialogController = fxmlLoader.getController();

        Scene scene = new Scene(parent, 300, 200);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
        curGameMode = dialogController.getMode();
    }

    public Color getRealPlayerTeam() {
        return realPlayerTeam;
    }

    public void setRealPlayerTeam(Color realPlayerTeam) {
        this.realPlayerTeam = realPlayerTeam;
    }

    public GameMode getCurGameMode() {
        return curGameMode;
    }

    public void setCurGameMode(GameMode curGameMode) {
        this.curGameMode = curGameMode;
    }

    public AiPlayer getAiPlayer() {
        return aiPlayer;
    }

    public void setAiPlayer(AiPlayer aiPlayer) {
        this.aiPlayer = aiPlayer;
    }
}