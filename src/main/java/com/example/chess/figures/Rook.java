package com.example.chess.figures;

import com.example.chess.ChessApplication;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class Rook extends Piece{
    private boolean isMoved;

    public Rook(Color team, Cell cell) {
        super(team, cell);
        isMoved = false;
        if (this.team == Color.WHITE) {
            image = new Image(ChessApplication.class.getResourceAsStream("wrook.png"));

        } else {
            image = new Image(ChessApplication.class.getResourceAsStream("brook.png"));
        }
    }
    @Override
    public boolean moveTo(Cell dest, Board board) {
        if (this.verifyMove(dest, board)) {
            isMoved = true;
            this.cell.setPiece(null);
            dest.setPiece(this);
            this.cell = dest;
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

        for (int i = curRow + 1; i <= 7; i++) {
            cellsToMove.add(cells[i][curCol]);
            if (cells[i][curCol].isOccupied()) break;
        }
        for (int i = curRow - 1; i >= 0; i--) {
            cellsToMove.add(cells[i][curCol]);
            if (cells[i][curCol].isOccupied()) break;
        }
        for (int j = curCol + 1; j <= 7; j++) {
            cellsToMove.add(cells[curRow][j]);
            if (cells[curRow][j].isOccupied()) break;
        }
        for (int j = curCol - 1; j >= 0; j--) {
            cellsToMove.add(cells[curRow][j]);
            if (cells[curRow][j].isOccupied()) break;
        }

        return cellsToMove;
    }

    @Override
    public ArrayList<Cell> getCellsToAttack(Board board) {
        return this.getCellsToMove(board);
    }

    public boolean isMoved() {
        return isMoved;
    }

    public void setMoved(boolean moved) {
        isMoved = moved;
    }
}
