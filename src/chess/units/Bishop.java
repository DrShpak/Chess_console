package chess.units;

import chess.board.ChessBoard;
import chess.board.Point;

public class Bishop extends Unit {

    public Bishop(Point currPoint, int code) {
        this.code = code;
        this.currentPoint = currPoint;
    }

    @Override
    public boolean canMove(Point startPoint, Point endPoint) {
        return Math.abs(endPoint.getX() - startPoint.getX()) == Math.abs(endPoint.getY() - startPoint.getY());
    }

    //зачем этот метод???
    @Override
    public boolean canAttack(Point startPoint, Point endPoint, Unit unit2) {
        if (!isFriendly(this, unit2) && endPoint != null)
            return true;
        else
            return false;
    }

    @Override
    public boolean isFriendly(Unit unit1, Unit unit2) {
        return this.code == unit1.code;
    }

    @Override
    public void move(Point startPoint, Point endPoint, ChessBoard board, Unit unit2) {
        if (canMove(startPoint, endPoint) && canAttack(startPoint, endPoint, unit2)) {
            board.getBoard()[startPoint.getX()][startPoint.getY()] = null;
            board.getBoard()[endPoint.getX()][endPoint.getY()] = this;
        }
    }
}
