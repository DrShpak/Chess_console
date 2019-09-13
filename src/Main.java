import board.ChessBoard;

public class Main {

    public static void main(String[] args) {

        int code = 9818;
//        System.out.println((char) code);

        ChessBoard board = new ChessBoard();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print((char) board.getBoard()[i][j] + " ");
            }
            System.out.println();
        }
    }
}
