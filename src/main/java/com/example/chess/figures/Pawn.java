package com.example.chess.figures;

import com.example.chess.ChessApplication;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class Pawn extends Piece {
    public Pawn(Color team, Cell cell) {
        super(team, cell);
        if (this.team == Color.WHITE) {
            image = new Image(ChessApplication.class.getResourceAsStream("wpawn.png"));

        } else {
            image = new Image(ChessApplication.class.getResourceAsStream("bpawn.png"));
        }
    }

    @Override
    public boolean moveTo(Cell dest, Board board) {
        if (this.verifyMove(dest, board)) {
            this.cell.setPiece(null);
            dest.setPiece(this);
            this.cell = dest;
            if ((this.team == Color.WHITE && dest.getRow() == 7) || (this.team == Color.BLACK && dest.getRow() == 0)) {
                dest.setPiece(new Queen(this.team, dest));

                this.cell = null;
                this.team = null;
                this.image = null;
            }
            return true;
        }
        return false;
    }
    @Override
    public ArrayList<Cell> getCellsToMove(Board board) {
        ArrayList<Cell> cellsToMove = new ArrayList<>();
        Cell cells[][] = board.getCells();
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
        Cell cells[][] = board.getCells();
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
