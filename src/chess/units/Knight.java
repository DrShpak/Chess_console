package chess.units;

import chess.board.ChessBoard;
import chess.board.Point;

public class Knight extends Unit {

    public Knight(int code) {
        super(code);
    }

    @Override
    public boolean canMove(Point startPoint, Point endPoint) {
        return  ((Math.abs(endPoint.getX() - startPoint.getX()) == 2 && Math.abs(endPoint.getY() - startPoint.getY()) == 1)
                ||(Math.abs(endPoint.getX() - startPoint.getX()) == 1 && Math.abs(endPoint.getY() - startPoint.getY()) == 2));
    }

    @Override
    public boolean canAttack(Point startPoint, Point endPoint) {

        return false;
    }
}
