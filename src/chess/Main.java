package chess;

import chess.board.ChessBoard;
import chess.board.Point;
import chess.units.Codes;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ChessBoard board = new ChessBoard();
        board.setBlackUnits();
        board.setWhiteUnits();

//        Scanner input = new Scanner(System.in);
        int code = 1;
        test(board);

        while (code != 0) {
            Scanner input = new Scanner(System.in);
            String line = input.nextLine();
//            input.close();
            Point startPoint = new Point(Integer.parseInt(line.split(" ")[0]), Integer.parseInt(line.split(" ")[1]));
            Point endPoint = new Point(Integer.parseInt(line.split(" ")[2]), Integer.parseInt(line.split(" ")[3]));

            board.getBoard()[startPoint.getX()][startPoint.getY()].move(startPoint, endPoint, board, board.getBoard()[endPoint.getX()][endPoint.getY()]);

            System.out.println("\n\n\n\n");

            code = input.nextByte();

            test(board);
        }
    }

    private static void test(ChessBoard board) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getBoard()[i][j] == null) {
                    if (((i + j) % 2 == 0))
                        System.out.print((char) Codes.getWHITE_CELL() + " ");
                    else
                        System.out.print((char) Codes.getBLACK_CELL() + " ");
                } else {
                    System.out.print((char) board.getBoard()[i][j].getCode() + " ");
                }
            }
            System.out.println();
        }
    }
}
