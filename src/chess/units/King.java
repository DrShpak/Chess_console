package chess.units;

import chess.board.Point;

public class King extends Unit {

    public King(int code) {
        super(code);
    }

    @Override
    public boolean canMove(Point startPoint, Point endPoint) {
        return (endPoint.getX() - startPoint.getX() <= 1 && endPoint.getY() - startPoint.getY() <= 1);
    }

    @Override
    public boolean canAttack(Point startPoint, Point endPoint) {
        return false;
    }
}