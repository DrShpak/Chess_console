package chess.units;

import chess.board.ChessBoard;
import chess.board.Point;

public class Bishop extends Unit {

    public Bishop(Point currPoint, int code) {
        this.code = code;
        this.currentPoint = currPoint;
    }

    public Bishop(int code) {
        this.code = code;
    }

    @Override
    public boolean canMove(Point startPoint, Point endPoint) {
        return Math.abs(endPoint.getX() - startPoint.getX()) == Math.abs(endPoint.getY() - startPoint.getY());
    }

    //todo определить целесообразность этого метода
    @Override
    public boolean canAttack(Point startPoint, Point endPoint, Unit unit2) {
        return !isFriendly(unit2) && endPoint != null;
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
            //todo сделать так чтобы на этом моменте не вылетало а давало ввести ход заново
            throw new RuntimeException("Неверный ход!");
    }
}
