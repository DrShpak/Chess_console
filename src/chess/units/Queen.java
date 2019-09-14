package chess.units;

import chess.board.Point;

public class Queen extends Unit {

    public Queen(int code) {
        super(code);
    }

    @Override
    public boolean canMove(Point startPoint, Point endPoint) {
        return false;
    }

    @Override
    public boolean canAttack(Point startPoint, Point endPoint) {
        return false;
    }
}
