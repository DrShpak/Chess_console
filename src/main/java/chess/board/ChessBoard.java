package chess.board;

import chess.misc.*;
import chess.units.*;

import java.util.Arrays;

public class ChessBoard {
    private Cell[][] board;

    ChessBoard(Unit[][] board) {
        this.board = Arrays.stream(board).map(x -> Arrays.stream(x).map(Cell::new).toArray(Cell[]::new)).toArray(Cell[][]::new);
    }

    public Point[] getPossibleMoves(Point unitCell) {
        var unit = board[unitCell.getX()][unitCell.getY()].getHolding();
        if (unit == null) {
            throw new NullPointerException();
        }

        return Arrays.stream(unit.getDirections()).
                map(
                        x -> StreamUtils.takeWhileEx(
                                x.move(unitCell),
                                y -> board[y.getX()][y.getY()].getHolding() == null
                        ).
                        filter(y -> board[y.getX()][y.getY()].getHolding() == null ||
                                x.getMovePolicy().compareTo(MovePolicy.BOTH) >= 0 &&
                                unit.isEnemy(board[y.getX()][y.getY()].getHolding())).
                        filter(y -> board[y.getX()][y.getY()].getHolding() != null ||
                                x.getMovePolicy().compareTo(MovePolicy.BOTH) <= 0)
                ).
                flatMap(x -> x).
                toArray(Point[]::new);
    }

    public void move(Point startPoint, Point endPoint) {
        var cell = board[startPoint.getX()][startPoint.getY()];
        if (cell.getHolding() == null) {
            throw new IllegalArgumentException("can`t move from " + startPoint + " to " + endPoint + " <= unit not found");
        }
        var possibleMoves = this.getPossibleMoves(startPoint);
        if (Arrays.stream(possibleMoves).noneMatch(x -> x.equals(endPoint))) {
            throw new IllegalArgumentException("can`t move from " + startPoint + " to " + endPoint + " <= forbidden move");
        }

        var newCell = board[endPoint.getX()][endPoint.getY()];

        onRaisingUnit(startPoint);
        if (newCell.getHolding() != null) {
            onRaisingUnit(endPoint);
        }
        Cell.moveUnit(cell, board[endPoint.getX()][endPoint.getY()]);
        onLowingUnit(endPoint);
    }

    private void onRaisingUnit(Point from) {
        var oldCell = getBoard()[from.getX()][from.getY()];
        oldCell.getOwnIncidents().forEach(AttackingIncident::unpinIncident);
        oldCell.getIncidents().forEach(x -> x.iterateIncidents().forEach(y -> y.getBarrages().remove(oldCell.getHolding())));
    }

    private void onLowingUnit(Point to) {
        var newCell = getBoard()[to.getX()][to.getY()];
        newCell.
                getIncidents().
                stream().
                map(AttackingIncident::iterateIncidents).
                flatMap(y -> y).
                forEach(y -> y.getBarrages().add(newCell.getHolding()));

        var newIncident = new AttackingIncident(newCell.getHolding(), newCell);
        Arrays.stream(newCell.
                getHolding().
                getDirections()).
                forEach(x -> StreamUtils.mapEx(
                        newIncident,
                        x.move(to),
                        (y, z) -> new AttackingIncident(newIncident, getBoard()[z.getX()][z.getY()]))
                );
    }

    public Cell[][] getBoard() {
        return board;
    }
}
