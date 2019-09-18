package chess.units;

import chess.board.Point;

public class Bishop extends Unit {

    public Bishop(int code) {
        super(code);
    }

    @Override
    public boolean canMove(Point startPoint, Point endPoint) {
        var diff = Point.diff(endPoint, startPoint);
        return Math.abs(diff.getY()) == Math.abs(diff.getX());
//        return Math.abs(endPoint.getX() - startPoint.getX()) == Math.abs(endPoint.getY() - startPoint.getY());
    }

    //todo определить целесообразность этого метода
    @Override
    public boolean canAttack(Point startPoint, Point endPoint) {
        return canMove(startPoint, endPoint);
    }

    @Override
    public boolean isEnemy(Unit unit2) {
        return this.code != unit2.code;
    }
}
