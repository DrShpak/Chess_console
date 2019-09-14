package chess.units;

import chess.board.ChessBoard;
import chess.board.Point;

public class Pawn extends Unit {

//    private final int CODE = 0;

    @Override
    public boolean canMove(Point startPoint, Point endPoint) {
        return false;
    }

    @Override
    public boolean canAttack(Point startPoint, Point endPoint, Unit unit2) {
        return false;
    }

    @Override
    public boolean isFriendly(Unit unit1, Unit unit2) {
        return false;
    }

    @Override
    public void move(Point startPoint, Point endPoint, ChessBoard board, Unit unit2) {

    }
}
