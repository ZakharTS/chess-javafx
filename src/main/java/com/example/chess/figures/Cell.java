package com.example.chess.figures;

import java.util.ArrayList;

public class Cell {
    private final int col;
    private final int row;
    private Piece piece;

    public Cell() {
        this(0, 0);
    }

    public Cell(int row, int col) {
        if (!verifyPos(row)) {
            row = 0;
        }
        if (!verifyPos(col)) {
            col = 0;
        }
        this.row = row;
        this.col = col;
        piece = null;
    }

    public static boolean verifyPos(int pos) {
        return (pos >= 0 && pos <= 7);
    }

    public boolean isUnderAttackBy(Color team, Board board) {
        ArrayList<Piece> pieces = board.getPiecesByTeam(team);
        for (Piece itr : pieces) {
            if (itr instanceof King) { // avoid recursion (да, костыль)
                Cell[][] cells = board.getCells();
                int curRow = itr.cell.getRow();
                int curCol = itr.cell.getCol();
                for (int i = curRow - 1; i <= curRow + 1; i++) {
                    for (int j = curCol - 1; j <= curCol + 1; j++) {
                        if (!Cell.verifyPos(i) || !Cell.verifyPos(j)) continue;
                        if (cells[i][j] == this) return true;
                    }
                }
                continue;
            }
            ArrayList<Cell> cellsUnderAttack = itr.getCellsToAttack(board);
            if (cellsUnderAttack.contains(this)) return true;
        }
        return false;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public boolean isOccupied() {
        return piece != null;
    }

    public boolean isOccupiedBy(Color team) {
        if (this.isOccupied()) {
            return this.piece.getTeam() == team;
        }
        return false;
    }
}
