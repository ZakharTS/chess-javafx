package com.example.chess;

import com.example.chess.board.Board;
import com.example.chess.board.Cell;
import com.example.chess.figures.Color;
import com.example.chess.figures.Piece;

import java.util.*;

public class AiPlayer {
    private Color team;
    public void performMove(Board board) {
        List<Move> moves = getPossibleMoves(board);

        Move bestMove = null;
        int bestValue = -9999;
        for (Move itr : moves) {
            itr.getPiece().moveTo(itr.getCellToMove(), board);
            int currentValue = minimax(3, board, -10000, 10000, team.equals(Color.WHITE));
            itr.getPiece().undoLastMove();

            if (team.equals(Color.WHITE)) {
                currentValue *= -1;
            }

            if (currentValue >= bestValue) {
                bestValue = currentValue;
                bestMove = itr;
            }
        }

        bestMove.getPiece().moveTo(bestMove.getCellToMove(), board);
    }

    public AiPlayer(Color team) {
        this.team = team;
    }

    public Color getTeam() {
        return team;
    }

    public void setTeam(Color team) {
        this.team = team;
    }
    private List<Move> getPossibleMoves(Board board) {
        ArrayList<Piece> pieces = board.getPiecesByTeam(team);
        List<Move> moves = new ArrayList<>();
        for (Piece itr : pieces) {
            List<Cell> cellsAvailable = new ArrayList<>();
            cellsAvailable.addAll(itr.getCellsToMove(board));
            cellsAvailable.addAll(itr.getCellsToAttack(board));
            moves.addAll(cellsAvailable.stream().map(cell -> (new Move(itr, cell))).toList());
        }
        Collections.shuffle(moves);
        moves = moves.stream().filter(move -> move.getPiece().verifyMove(move.getCellToMove(), board)).toList();
        return moves;
    }

    private int minimax(int depth, Board board, int alpha, int beta, boolean isMaxPlayer) {
        if (depth == 0) {
            return -board.getBoardValue();
        }
        List<Move> moves = getPossibleMoves(board);

        if (isMaxPlayer) {
            int bestValue = -9999;
            for (Move itr : moves) {
                itr.getPiece().moveTo(itr.getCellToMove(), board);
                bestValue = Math.max(bestValue, minimax(depth - 1, board, alpha, beta, !isMaxPlayer));
                itr.getPiece().undoLastMove();

                alpha = Math.max(alpha, bestValue);
                if (beta <= alpha) {
                    return bestValue;
                }
            }
            return bestValue;
        } else {
            int bestValue = 9999;
            for (Move itr : moves) {
                itr.getPiece().moveTo(itr.getCellToMove(), board);
                bestValue = Math.min(bestValue, minimax(depth - 1, board, alpha, beta, !isMaxPlayer));
                itr.getPiece().undoLastMove();

                beta = Math.min(beta, bestValue);
                if (beta <= alpha) {
                    return bestValue;
                }
            }
            return bestValue;
        }
    }
}
