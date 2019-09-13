package board;

public class ChessBoard {

    private int[][] board = new int[8][8];
    private final int WHITE_EMPTY_CELL_CODE = 9898;
    private final int BLACK_EMPTY_CELL_CODE = 9899;

    public ChessBoard() {
        int flag = 1;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (flag == 1) {
                    board[i][j] = WHITE_EMPTY_CELL_CODE;
                    flag = 2;
                } else {
                    board[i][j] = BLACK_EMPTY_CELL_CODE;
                    flag = 1;
                }
            }

            if (flag == 2)
                flag = 1;
            else
                flag = 2;
        }
    }

    public int[][] getBoard() {
        return board;
    }
}
