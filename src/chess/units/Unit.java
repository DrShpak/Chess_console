package chess.units;

import chess.board.Point;

public abstract class Unit {

    int code;

    Unit(int code) {
        this.code = code;
    }

    public abstract boolean canMove(Point startPoint, Point endPoint);

    public abstract boolean canAttack(Point startPoint, Point endPoint);

    public boolean isEnemy(Unit unit2) {
        return this.code != unit2.code;
    }

    public int getCode() {
        return code;
    }
}