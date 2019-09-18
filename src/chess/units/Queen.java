package chess.units;

import chess.board.Point;

public class Queen extends Unit {

    public Queen(int code) {
        super(code);
    }

    @Override
    public boolean canMove(Point startPoint, Point endPoint) {
        var diff = Point.diff(endPoint, startPoint);
        return Math.abs(diff.getY()) == Math.abs(diff.getX())
                || diff.getX() == 0 || diff.getY() == 0;
    }

    @Override
    public boolean canAttack(Point startPoint, Point endPoint) {
        return canMove(startPoint, endPoint);
    }
}
