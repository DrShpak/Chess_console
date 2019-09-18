package chess.units;

import chess.board.Point;

public class King extends Unit {

    public King(int code) {
        super(code);
    }

    @Override
    public boolean canMove(Point startPoint, Point endPoint) {
        var diff = Point.diff(endPoint, startPoint);
        return diff.getY() <=1 && diff.getX() <= 1;
    }

    @Override
    public boolean canAttack(Point startPoint, Point endPoint) {
        return canMove(startPoint, endPoint);
    }
}