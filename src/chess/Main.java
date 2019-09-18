package chess;

import chess.board.ChessBoard;
import chess.board.Point;
import misc.Codes;

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
            var splited = line.split(" ");
            Point startPoint = Point.parse(splited[0]);
            Point endPoint = Point.parse(splited[1]);
            board.move(startPoint, endPoint);

            System.out.println("\n\n\n\n");

            code = input.nextByte();

            test(board);
        }
    }

    private static void test(ChessBoard board) {
        for (int i = 7; i >= 0; i--) {
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
            System.out.print('\n');
        }
    }
}
