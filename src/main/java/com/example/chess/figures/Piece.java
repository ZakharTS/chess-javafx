package com.example.chess.figures;

import com.example.chess.board.Board;
import com.example.chess.board.Cell;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public abstract class Piece {
    protected Image image;
    protected Cell cell;
    protected Color team;
    protected int value;
    private Stack<Cell> oldCells;

    public Piece(Color team, Cell cell) {
        this.team = team;
        this.cell = cell;
        oldCells = new Stack<>();
    }

    public void moveTo(Cell dest) {
        oldCells.add(this.cell);

        this.cell.getOldPieces().add(this.cell.getPiece());
        this.cell.setPiece(null);

        this.cell = dest;

        if (this.cell.getPiece() != null) {
            this.cell.getPiece().getOldCells().add(this.cell);
            this.cell.getPiece().setCell(null);
        }

        this.cell.getOldPieces().add(this.cell.getPiece());
        this.cell.setPiece(this);
    }

    public boolean moveTo(Cell dest, Board board) {
        if (this.verifyMove(dest, board)) {
            moveTo(dest);

            return true;
        }
        return false;
    }

    public void undoLastMove() {
        this.cell.setPiece(this.cell.getOldPieces().pop());
        if (this.cell.getPiece() != null) {
            this.cell.getPiece().setCell(this.cell.getPiece().getOldCells().pop());
        }
        setCell(oldCells.pop());
        this.cell.setPiece(this.cell.getOldPieces().pop());
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

    public int tryMoveAndGetBoardValue(Cell dest, Board board) {
        // trying new layout
        Cell oldCell = this.cell;
        Piece destPiece = dest.getPiece();

        this.cell.setPiece(null);
        dest.setPiece(this);
        this.cell = dest;
        // get board value
        int value = board.getBoardValue();
        // set pieces back
        dest.setPiece(destPiece);

        oldCell.setPiece(this);
        this.cell = oldCell;
        return value;
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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Stack<Cell> getOldCells() {
        return oldCells;
    }


}
