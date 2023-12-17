package com.example.chess;

import com.example.chess.figures.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class ChessController {


    static Pane[][] boardPane = ChessApplication.boardPane;
    static Board board = ChessApplication.board;
    static Cell[][] cells = board.getCells();
    Piece curPiece = null;
    Color curTeam = Color.WHITE;

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
            // pieces on board update, change team;
            updateBoard();
            ChessApplication.clearSelection(boardGrid);
            curPiece = null;
            curTeam = curTeam.opposite();
            // status update
            board.updateCurrentStatus(curTeam);
            statusHandler(board.getCurrentStatus());
        } else {
            curPiece = null;
            ChessApplication.clearSelection(boardGrid);
        }
    }
    public static void updateBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                putPieceInPane(boardPane[i][j], cells[i][j].getPiece());
            }
        }
    }

    private static void statusHandler(GameStatus status) {
        switch (status) {
            case BLACKS_MOVE -> {
                ChessApplication.whiteLabel.setVisible(false);
                ChessApplication.blackLabel.setText("Black's move");
                ChessApplication.blackLabel.setVisible(true);
            }
            case WHITES_MOVE -> {
                ChessApplication.blackLabel.setVisible(false);
                ChessApplication.whiteLabel.setText("White's move");
                ChessApplication.whiteLabel.setVisible(true);
            }
            case DRAW -> putLabelInCenter(ChessApplication.rootLayout, "DRAW");
            case BLACK_CHECK ->{
                ChessApplication.whiteLabel.setVisible(false);
                ChessApplication.blackLabel.setText("CHECK!");
                ChessApplication.blackLabel.setVisible(true);
            }
            case WHITE_CHECK -> {
                ChessApplication.blackLabel.setVisible(false);
                ChessApplication.whiteLabel.setText("CHECK!");
                ChessApplication.whiteLabel.setVisible(true);
            }
            case BLACK_CHECK_AND_MATE -> {
                putLabelInCenter(ChessApplication.rootLayout, "WHITE WINS!");
                ChessApplication.whiteLabel.setVisible(false);
                ChessApplication.blackLabel.setVisible(false);

            }
            case WHITE_CHECK_AND_MATE -> {
                putLabelInCenter(ChessApplication.rootLayout, "BLACK WINS!");
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

}