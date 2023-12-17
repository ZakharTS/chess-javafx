package com.example.chess;

import com.example.chess.board.Cell;
import com.example.chess.figures.Piece;

public class Move {
    private Piece piece;
    private Cell cellToMove;

    public Move(Piece piece, Cell cellToMove) {
        this.piece = piece;
        this.cellToMove = cellToMove;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Cell getCellToMove() {
        return cellToMove;
    }

    public void setCellToMove(Cell cellToMove) {
        this.cellToMove = cellToMove;
    }
}
