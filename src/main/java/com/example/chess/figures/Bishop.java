package com.example.chess.figures;

import com.example.chess.ChessApplication;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Objects;

public class Bishop extends Piece {

    public Bishop(Color team, Cell cell) {
        super(team, cell);
        if (this.team == Color.WHITE) {
            image = new Image(Objects.requireNonNull(ChessApplication.class.getResourceAsStream("wbishop.png")));

        } else {
            image = new Image(Objects.requireNonNull(ChessApplication.class.getResourceAsStream("bbishop.png")));
        }
    }

    @Override
    public ArrayList<Cell> getCellsToMove(Board board) {
        ArrayList<Cell> cellsToMove = new ArrayList<>();
        Cell[][] cells = board.getCells();
        Cell curCell = this.getCell();
        int curRow = curCell.getRow();
        int curCol = curCell.getCol();

        for (int i = curRow + 1, j = curCol + 1; i <= 7 && j <= 7; i++, j++) {
            cellsToMove.add(cells[i][j]);
            if (cells[i][j].isOccupied()) break;
        }
        for (int i = curRow + 1, j = curCol - 1; i <= 7 && j >= 0; i++, j--) {
            cellsToMove.add(cells[i][j]);
            if (cells[i][j].isOccupied()) break;
        }
        for (int i = curRow - 1, j = curCol + 1; i >= 0 && j <= 7; i--, j++) {
            cellsToMove.add(cells[i][j]);
            if (cells[i][j].isOccupied()) break;
        }
        for (int i = curRow - 1, j = curCol - 1; i >= 0 && j >= 0; i--, j--) {
            cellsToMove.add(cells[i][j]);
            if (cells[i][j].isOccupied()) break;
        }


        return cellsToMove;
    }

    @Override
    public ArrayList<Cell> getCellsToAttack(Board board) {
        return this.getCellsToMove(board);
    }
}
