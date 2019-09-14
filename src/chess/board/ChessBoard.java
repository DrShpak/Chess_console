package chess.board;

import chess.units.Bishop;
import chess.units.Codes;
import chess.units.Pawn;
import chess.units.Unit;

public class ChessBoard {

//    private char[][] board = new char[8][8];
    private Unit[][] board = new Unit[8][8];

    public ChessBoard() {
        /*for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) {
                    board[i][j] = WHITE_EMPTY_CELL_CODE;
                } else {
                    board[i][j] = BLACK_EMPTY_CELL_CODE;
                }
            }
        }*/

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) {
                    board[i][j] = null;
                } else {
                    board[i][j] = null;
                }
            }
        }
    }

    public void setUnits() {
        Bishop bishop = new Bishop(new Point(0, 7), Codes.getBLACK_BISHOP());
        board[0][7] = bishop;

        Pawn pawn = new Pawn(Codes.getBlackPawn());
        board[0][0] = pawn;
    }

    public Unit[][] getBoard() {
        return board;
    }
}
