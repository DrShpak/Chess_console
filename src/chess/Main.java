package chess;

import chess.board.ChessBoard;

public class Main {

    public static void main(String[] args) {
        ChessBoard board = new ChessBoard();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(board.getBoard()[i][j] + " ");
            }
            System.out.println();
        }
    }
}
