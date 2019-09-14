package chess.units;

import chess.board.ChessBoard;
import chess.board.Point;

public class Queen extends Unit {

    public Queen(int code) {
        this.code = code;
    }

    @Override
    public boolean canMove(Point startPoint, Point endPoint) {
        return false;
    }

    @Override
    public boolean canAttack(Point startPoint, Point endPoint, Unit unit2) {
        return false;
    }

    @Override
    public boolean isFriendly(Unit unit2) {
        return this.code == unit2.code;
    }

    @Override
    public void move(Point startPoint, Point endPoint, ChessBoard board, Unit unit2) {

    }
}
