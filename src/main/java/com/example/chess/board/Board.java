package com.example.chess.board;

import com.example.chess.figures.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Board {
    private final Cell[][] cells;
    private final King whiteKing;
    private final King blackKing;
    private GameStatus currentStatus;
    public Board() {
        currentStatus = GameStatus.WHITES_MOVE;
        // board init
        cells = new Cell[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
        // pieces init
        for (int j = 0; j < 8; j++) {
            cells[1][j].setPiece(new Pawn(Color.WHITE, cells[1][j]));
            cells[6][j].setPiece(new Pawn(Color.BLACK, cells[6][j]));
        }
        cells[0][0].setPiece(new Rook(Color.WHITE, cells[0][0]));
        cells[0][7].setPiece(new Rook(Color.WHITE, cells[0][7]));
        cells[7][0].setPiece(new Rook(Color.BLACK, cells[7][0]));
        cells[7][7].setPiece(new Rook(Color.BLACK, cells[7][7]));

        cells[0][1].setPiece(new Knight(Color.WHITE, cells[0][1]));
        cells[0][6].setPiece(new Knight(Color.WHITE, cells[0][6]));
        cells[7][1].setPiece(new Knight(Color.BLACK, cells[7][1]));
        cells[7][6].setPiece(new Knight(Color.BLACK, cells[7][6]));

        cells[0][2].setPiece(new Bishop(Color.WHITE, cells[0][2]));
        cells[0][5].setPiece(new Bishop(Color.WHITE, cells[0][5]));
        cells[7][2].setPiece(new Bishop(Color.BLACK, cells[7][2]));
        cells[7][5].setPiece(new Bishop(Color.BLACK, cells[7][5]));

        cells[0][3].setPiece(new Queen(Color.WHITE, cells[0][3]));
        whiteKing = new King(Color.WHITE, cells[0][4]);
        cells[0][4].setPiece(whiteKing);
        cells[7][3].setPiece(new Queen(Color.BLACK, cells[7][3]));
        blackKing = new King(Color.BLACK, cells[7][4]);
        cells[7][4].setPiece(blackKing);
    }

    public Cell[][] getCells() {
        return cells;
    }

    public King getKing(Color team) {
        return team == Color.WHITE ? whiteKing : blackKing;
    }

    public GameStatus getCurrentStatus() {
        return currentStatus;
    }
    public void updateCurrentStatus(Color team) {
        currentStatus = team == Color.WHITE ? GameStatus.WHITES_MOVE : GameStatus.BLACKS_MOVE;
        if (isCheck(team)) {
            currentStatus = team == Color.WHITE ? GameStatus.WHITE_CHECK : GameStatus.BLACK_CHECK;
        }
        if (!areMovesAvailable(team)) {
            if (currentStatus == GameStatus.WHITE_CHECK) {
                currentStatus = GameStatus.WHITE_CHECK_AND_MATE;
            } else if (currentStatus == GameStatus.BLACK_CHECK) {
                currentStatus = GameStatus.BLACK_CHECK_AND_MATE;
            } else {
                currentStatus = GameStatus.DRAW;
            }
        }
    }
    public boolean isCheck(Color team) {
        return getKing(team).getCell().isUnderAttackBy(team.opposite(), this);
    }
    public boolean areMovesAvailable(Color team) {
        ArrayList<Piece> pieces = this.getPiecesByTeam(team);
        int count = 0;
        for (Piece curPiece : pieces) {
            ArrayList<Cell> toMove = curPiece.getCellsToMove(this);
            curPiece.removeExtra(toMove, this);

            ArrayList<Cell> toAttack = curPiece.getCellsToAttack(this);
            curPiece.removeExtra(toAttack, this);
            if (!toMove.isEmpty() || !toAttack.isEmpty()) count++;
        }
        return !(count == 0);
    }
    public ArrayList<Piece> getPiecesByTeam(Color team) {
        ArrayList<Piece> piecesOfTeam = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (cells[i][j].isOccupied()) {
                    if (cells[i][j].getPiece().getTeam() == team) {
                        piecesOfTeam.add(cells[i][j].getPiece());
                    }
                }
            }
        }
        return piecesOfTeam;
    }

    public int getBoardValue() {
        AtomicInteger value = new AtomicInteger();

        List<Piece> pieces = getPiecesByTeam(Color.WHITE);
        pieces.forEach(itr -> value.addAndGet(itr.getValue()));

        pieces = getPiecesByTeam(Color.BLACK);
        pieces.forEach(itr -> value.addAndGet(-itr.getValue()));

        return value.get();
    }
}
