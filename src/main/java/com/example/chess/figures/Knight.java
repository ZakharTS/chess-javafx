package com.example.chess.figures;

import com.example.chess.ChessApplication;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Objects;

public class Knight extends Piece{
    public Knight(Color team, Cell cell) {
        super(team, cell);
        if (this.team == Color.WHITE) {
            image = new Image(Objects.requireNonNull(ChessApplication.class.getResourceAsStream("wknight.png")));

        } else {
            image = new Image(Objects.requireNonNull(ChessApplication.class.getResourceAsStream("bknight.png")));
        }
    }

    @Override
    public ArrayList<Cell> getCellsToMove(Board board) {
        ArrayList<Cell> cellsToMove = new ArrayList<>();
        Cell[][] cells = board.getCells();
        Cell curCell = this.getCell();
        int curRow = curCell.getRow();
        int curCol = curCell.getCol();

        if (Cell.verifyPos(curRow + 2) && Cell.verifyPos(curCol + 1)) {
            cellsToMove.add(cells[curRow + 2][curCol + 1]);
        }
        if (Cell.verifyPos(curRow + 2) && Cell.verifyPos(curCol - 1)) {
            cellsToMove.add(cells[curRow + 2][curCol - 1]);
        }
        if (Cell.verifyPos(curRow - 2) && Cell.verifyPos(curCol + 1)) {
            cellsToMove.add(cells[curRow - 2][curCol + 1]);
        }
        if (Cell.verifyPos(curRow - 2) && Cell.verifyPos(curCol - 1)) {
            cellsToMove.add(cells[curRow - 2][curCol - 1]);
        }
        if (Cell.verifyPos(curRow + 1) && Cell.verifyPos(curCol + 2)) {
            cellsToMove.add(cells[curRow + 1][curCol + 2]);
        }
        if (Cell.verifyPos(curRow + 1) && Cell.verifyPos(curCol - 2)) {
            cellsToMove.add(cells[curRow + 1][curCol - 2]);
        }
        if (Cell.verifyPos(curRow - 1) && Cell.verifyPos(curCol + 2)) {
            cellsToMove.add(cells[curRow - 1][curCol + 2]);
        }
        if (Cell.verifyPos(curRow - 1) && Cell.verifyPos(curCol - 2)) {
            cellsToMove.add(cells[curRow - 1][curCol - 2]);
        }

        return cellsToMove;
    }

    @Override
    public ArrayList<Cell> getCellsToAttack(Board board) {
        return this.getCellsToMove(board);
    }
}
