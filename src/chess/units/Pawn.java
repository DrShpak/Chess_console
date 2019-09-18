package chess.units;

import chess.board.Point;
import misc.Codes;

public class Pawn extends Unit {

    public Pawn(int code) {
        super(code);
    }

    @Override
    public boolean canMove(Point startPoint, Point endPoint) {
        var diff = Point.diff(endPoint, startPoint);
        return diff.getY() == 0 && diff.getX() == (Codes.getWhitePawn() == code ? 1 : -1);
    }

    @Override
    public boolean canAttack(Point startPoint, Point endPoint) {
        var diff = Point.diff(endPoint, startPoint);
        return Math.abs(diff.getY()) == 1 && diff.getX() == (Codes.getWhitePawn() == code ? 1 : -1);
//        return false;
    }
}
