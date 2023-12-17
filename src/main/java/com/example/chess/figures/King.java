package com.example.chess.figures;

import com.example.chess.board.Board;
import com.example.chess.board.Cell;
import com.example.chess.ChessApplication;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Objects;

public class King extends Piece {
    private boolean isMoved;
    public King(Color team, Cell cell) {
        super(team, cell);
        value = 900;
        isMoved = false;
        if (this.team == Color.WHITE) {
            image = new Image(Objects.requireNonNull(ChessApplication.class.getResourceAsStream("wking.png")));

        } else {
            image = new Image(Objects.requireNonNull(ChessApplication.class.getResourceAsStream("bking.png")));
        }
    }
    @Override
    public boolean moveTo(Cell dest, Board board) {
        if (this.verifyMove(dest, board)) {
            isMoved = true;
            if (dest.getCol() - this.getCell().getCol() == 2) { // caslting
                Cell[][] cells = board.getCells();
                Piece rook = cells[dest.getRow()][dest.getCol() + 1].getPiece();
                if (!(rook instanceof Rook)) return false;
                if (!(rook).moveTo(cells[dest.getRow()][dest.getCol() - 1], board)) {
                    return false;
                }
            } else if (dest.getCol() - this.getCell().getCol() == -2) { // castling
                Cell[][] cells = board.getCells();
                Piece rook = cells[dest.getRow()][dest.getCol() - 2].getPiece();
                if (!(rook instanceof Rook)) return false;
                if (!(rook).moveTo(cells[dest.getRow()][dest.getCol() + 1], board)) {
                    return false;
                }
            }
            moveTo(dest);
            return true;
        }
        return false;
    }

    @Override
    public ArrayList<Cell> getCellsToMove(Board board) {
        ArrayList<Cell> cellsToMove = getCellsToAttack(board);
        Cell[][] cells = board.getCells();
        Cell curCell = this.getCell();
        int row = curCell.getRow();
        int col = curCell.getCol();
        if (!this.isMoved) { // looking for castling
            Piece rook = cells[row][col + 3].getPiece();
            if (rook instanceof Rook) {
                if (rook.getTeam() == this.team) {
                    if (!((Rook) rook).isMoved()) {
                        if (!cells[row][col + 1].isOccupied() && !cells[row][col + 2].isOccupied()) {
                            if (!cells[row][col + 1].isUnderAttackBy(this.team.opposite(), board) &&
                                    !cells[row][col + 2].isUnderAttackBy(this.team.opposite(), board)) {
                                cellsToMove.add(cells[row][col + 2]);
                            }
                        }
                    }
                }
            }
            rook = cells[row][col - 4].getPiece();
            if (rook instanceof Rook) {
                if (rook.getTeam() == this.team) {
                    if (!((Rook) rook).isMoved()) {
                        if (!cells[row][col - 1].isOccupied() && !cells[row][col - 2].isOccupied() &&
                                !cells[row][col - 3].isOccupied()) {
                            if (!cells[row][col - 1].isUnderAttackBy(this.team.opposite(), board) &&
                                    !cells[row][col - 2].isUnderAttackBy(this.team.opposite(), board)) {
                                cellsToMove.add(cells[row][col - 2]);
                            }
                        }
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
        int row = curCell.getRow();
        int col = curCell.getCol();

        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (!Cell.verifyPos(i) || !Cell.verifyPos(j)) continue;
                if (i == row && j == col) continue;
                if (!cells[i][j].isUnderAttackBy(this.team.opposite(), board)) {
                    cellsToMove.add(cells[i][j]);
                }
            }
        }
        return cellsToMove;
    }

}
