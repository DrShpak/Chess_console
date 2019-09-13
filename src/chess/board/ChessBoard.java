package chess.board;

public class ChessBoard {

    private char[][] board = new char[8][8];
    private final char BLACK_EMPTY_CELL_CODE = '\u26AB';
    private final char WHITE_EMPTY_CELL_CODE = '\u26AA';

    public ChessBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) {
                    board[i][j] = WHITE_EMPTY_CELL_CODE;
                } else {
                    board[i][j] = BLACK_EMPTY_CELL_CODE;
                }
            }
        }
    }

    public char[][] getBoard() {
        return board;
    }
}
