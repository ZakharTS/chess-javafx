package com.example.chess.figures;

import com.example.chess.board.Board;
import com.example.chess.board.Cell;
import com.example.chess.ChessApplication;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Objects;

public class Pawn extends Piece {
    public Pawn(Color team, Cell cell) {
        super(team, cell);
        value = 10;
        if (this.team == Color.WHITE) {
            image = new Image(Objects.requireNonNull(ChessApplication.class.getResourceAsStream("wpawn.png")));

        } else {
            image = new Image(Objects.requireNonNull(ChessApplication.class.getResourceAsStream("bpawn.png")));
        }
    }

    @Override
    public boolean moveTo(Cell dest, Board board) {
        if (this.verifyMove(dest, board)) {
            moveTo(dest);
            if ((this.team == Color.WHITE && this.cell.getRow() == 7) || (this.team == Color.BLACK && this.cell.getRow() == 0)) {
                dest.setPiece(new Queen(this.team, dest));
            }
            return true;
        }
        return false;
    }
    @Override
    public ArrayList<Cell> getCellsToMove(Board board) {
        ArrayList<Cell> cellsToMove = new ArrayList<>();
        Cell[][] cells = board.getCells();
        Cell curCell = this.getCell();
        int curRow = curCell.getRow();
        int curCol = curCell.getCol();

        if (this.team == Color.WHITE) {
            if (curRow == 1) {
                for (int i = curRow + 1; i <= curRow + 2; i++) {
                    if (cells[i][curCol].isOccupied()) {
                        break;
                    } else {
                        cellsToMove.add(cells[i][curCol]);
                    }
                }
            } else {
                if (Cell.verifyPos(curRow + 1)) {
                    if (!cells[curRow + 1][curCol].isOccupied()) {
                        cellsToMove.add(cells[curRow + 1][curCol]);
                    }
                }
            }
        } else {
            if (curRow == 6) {
                for (int i = curRow - 1; i >= curRow - 2; i--) {
                    if (cells[i][curCol].isOccupied()) {
                        break;
                    } else {
                        cellsToMove.add(cells[i][curCol]);
                    }
                }
            } else {
                if (Cell.verifyPos(curRow - 1)) {
                    if (!cells[curRow - 1][curCol].isOccupied()) {
                        cellsToMove.add(cells[curRow - 1][curCol]);
                    }
                }
            }
        }
        return cellsToMove;
    }

    @Override
    public ArrayList<Cell> getCellsToAttack(Board board) {
        ArrayList<Cell> cellsToMove = new ArrayList<>();
        Cell[][] cells = board.getCells();
        Cell curCell = this.getCell();
        int curRow = curCell.getRow();
        int curCol = curCell.getCol();
        if (this.team == Color.WHITE) {
            if (Cell.verifyPos(curRow + 1)) {
                if (Cell.verifyPos(curCol + 1)) {
                    cellsToMove.add(cells[curRow + 1][curCol + 1]);
                }
                if (Cell.verifyPos(curCol - 1)) {
                    cellsToMove.add(cells[curRow + 1][curCol - 1]);
                }
            }
        } else {
            if (Cell.verifyPos(curRow - 1)) {
                if (Cell.verifyPos(curCol + 1)) {
                    cellsToMove.add(cells[curRow - 1][curCol + 1]);
                }
                if (Cell.verifyPos(curCol - 1)) {
                    cellsToMove.add(cells[curRow - 1][curCol - 1]);
                }
            }
        }

        return cellsToMove;
    }
}
