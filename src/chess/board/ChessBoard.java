package chess.board;

import chess.units.*;

public class ChessBoard {

    private Unit[][] board = new Unit[8][8];

    public ChessBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }
    }

    //todo расставить фигуры на исходные позиции
    public void setBlackUnits() {
        board[0][0] = new Rook(Codes.getBlackRook());
        board[0][7] = new Rook(Codes.getBlackRook());

        board[0][1] = new Knight(Codes.getBlackKnight());
        board[0][6] = new Knight(Codes.getBlackKnight());

        board[0][2] = new Bishop(Codes.getBLACK_BISHOP());
        board[0][5] = new Bishop(Codes.getBLACK_BISHOP());

        board[0][3] = new Queen(Codes.getBlackQueen());
        board[0][4] = new King(Codes.getBlackKing());

        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn(Codes.getBlackPawn());
        }
    }

    public void setWhiteUnits() {
        board[7][0] = new Rook(Codes.getWhiteRook());
        board[7][7] = new Rook(Codes.getWhiteRook());

        board[7][1] = new Knight(Codes.getWhiteKnight());
        board[7][6] = new Knight(Codes.getWhiteKnight());

        board[7][2] = new Bishop(Codes.getWHITE_BISHOP());
        board[7][5] = new Bishop(Codes.getWHITE_BISHOP());

        board[7][3] = new Queen(Codes.getWhiteQueen());
        board[7][4] = new King(Codes.getWhiteKing());

        for (int i = 0; i < 8; i++) {
            board[6][i] = new Pawn(Codes.getWhitePawn());
        }
    }

    public void move(Point startPoint, Point endPoint) {
        var unit = board[startPoint.getX()][startPoint.getY()];
        if (unit == null) {
            //...
        }
        var victim = board[endPoint.getX()][endPoint.getY()];
        var attack = false;
        if (victim != null &&
                ((!victim.isEnemy(unit)) || (attack = !unit.canAttack(startPoint, endPoint)))) {
            //...
        }
        if (!attack && !unit.canMove(startPoint, endPoint)) {
            throw new RuntimeException("Неверный ход!");
        }

        board[startPoint.getX()][startPoint.getY()] = null;
        board[endPoint.getX()][endPoint.getY()] = unit;
    }

    public Unit[][] getBoard() {
        return board;
    }
}
