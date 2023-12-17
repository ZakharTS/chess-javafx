package com.example.chess.figures;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class Piece {
    protected Image image;
    protected Cell cell;
    protected Color team;

    public Piece(Color team, Cell cell) {
        this.team = team;
        this.cell = cell;
    }

    public boolean moveTo(Cell dest, Board board) {
        if (this.verifyMove(dest, board)) {
            this.cell.setPiece(null);
            dest.setPiece(this);
            this.cell = dest;
            return true;
        }
        return false;
    }

    public abstract ArrayList<Cell> getCellsToMove(Board board);

    public abstract ArrayList<Cell> getCellsToAttack(Board board);

    public boolean verifyMove(Cell dest, Board board) {
        if (this.tryMove(dest, board)) {
            return false;
        }
        if (dest.isOccupied()) {
            return this.getCellsToAttack(board).contains(dest);
        } else {
            return this.getCellsToMove(board).contains(dest);
        }
    }

    public boolean tryMove(Cell dest, Board board) {
        if (dest.isOccupiedBy(this.team)) {
            return true;
        }
        // trying new layout
        Cell oldCell = this.cell;
        Piece destPiece = dest.getPiece();

        this.cell.setPiece(null);
        dest.setPiece(this);
        this.cell = dest;
        // is king under attack?
        boolean isCheck = board.isCheck(this.team);
        // set pieces back
        dest.setPiece(destPiece);

        oldCell.setPiece(this);
        this.cell = oldCell;
        return isCheck;
    }

    public void removeExtra(ArrayList<Cell> cellsToMove, Board board) {
        Iterator<Cell> itr = cellsToMove.iterator();
        while (itr.hasNext()) {
            Cell current = itr.next();
            if (current != null) {
                if (this.tryMove(current, board)) {
                    itr.remove();
                }
            }
        }
    }

    public Color getTeam() {
        return team;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public Image getImage() {
        return image;
    }

}
