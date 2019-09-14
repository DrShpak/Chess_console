package chess.units;

import chess.board.Point;

public class Pawn extends Unit {

    public Pawn(int code) {
        super(code);
    }

    @Override
    public boolean canMove(Point startPoint, Point endPoint) {
        return startPoint.getY() == endPoint.getY();
    }

    @Override
    public boolean canAttack(Point startPoint, Point endPoint) {
        return false;
    }
}
