package chess.units;

import chess.board.ChessBoard;
import chess.board.Point;

public class Pawn extends Unit {

    public Pawn(int code) {
        this.code = code;
    }

    @Override
    public boolean canMove(Point startPoint, Point endPoint) {
        return startPoint.getY() == endPoint.getY();
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
        if (canMove(startPoint, endPoint)) {
            board.getBoard()[startPoint.getX()][startPoint.getY()] = null;
            board.getBoard()[endPoint.getX()][endPoint.getY()] = this;
        } else
            throw new RuntimeException("кек");
    }
}
