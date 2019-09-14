package chess.units;

import chess.board.ChessBoard;
import chess.board.Point;

public abstract class Unit {

    Point currentPoint;

    int code;
//    protected int color = 0; //0 - white, 1 - black


//    public Unit(int code) {
//        this.code = code;
//    }

    public abstract boolean canMove(Point startPoint, Point endPoint);

    //todo целесообразность этого метода???
    public abstract boolean canAttack(Point startPoint, Point endPoint, Unit unit2);

    public abstract boolean isFriendly(Unit unit2);

    public abstract void move(Point startPoint, Point endPoint, ChessBoard board, Unit unit2);

    public Point getCurrentPoint() {
        return currentPoint;
    }

    public int getCode() {
        return code;
    }
}