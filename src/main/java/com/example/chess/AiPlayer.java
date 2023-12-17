package com.example.chess;

import com.example.chess.board.Board;
import com.example.chess.board.Cell;
import com.example.chess.figures.Color;
import com.example.chess.figures.Piece;

import java.util.*;

public class AiPlayer {
    private Color team;
    public void performMove(Board board) {
        ArrayList<Piece> pieces = board.getPiecesByTeam(team);
        List<Move> moves = new ArrayList<>();
        for (Piece itr : pieces) {
            List<Cell> cellsAvailable = new ArrayList<>();
            cellsAvailable.addAll(itr.getCellsToMove(board));
            cellsAvailable.addAll(itr.getCellsToAttack(board));
            cellsAvailable.stream().forEach(cell -> moves.add(new Move(itr, cell)));
        }
        moves.stream().filter(move -> !move.getPiece().getCell().equals(move.getCellToMove()));

        Move currentMove = moves.get(new Random().nextInt(moves.size()));
        while (!currentMove.getPiece().moveTo(currentMove.getCellToMove(), board)) {
            currentMove = moves.get(new Random().nextInt(moves.size()));
        }
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
}
