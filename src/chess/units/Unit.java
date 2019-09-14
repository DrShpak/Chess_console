package chess.units;

import chess.board.ChessBoard;
import chess.board.Point;

public abstract class Unit {

    protected Point currentPoint;

    protected int code;
    protected int color = 0; //0 - white, 1 - black

    public abstract boolean canMove(Point startPoint, Point endPoint);

    public abstract boolean canAttack(Point startPoint, Point endPoint, Unit unit2);

    public abstract boolean isFriendly(Unit unit1, Unit unit2);

    public abstract void move(Point startPoint, Point endPoint, ChessBoard board, Unit unit2);

    public Point getCurrentPoint() {
        return currentPoint;
    }

    public int getCode() {
        return code;
    }

    public int getColor() {
        return color;
    }
}