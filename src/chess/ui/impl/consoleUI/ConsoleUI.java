package chess.ui.impl.consoleUI;

import chess.board.ChessBoard;
import chess.misc.Point;
import chess.ui.UI;
import com.google.common.collect.Lists;
import com.google.common.collect.Streams;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ConsoleUI
extends UI {
    private Scanner input = new Scanner(System.in);

    public ConsoleUI(ChessBoard board) {
        super(board);
    }

    @Override
    public void start() {
        //noinspection StatementWithEmptyBody
        while (loop()) ;
    }

    private boolean loop()
    {
        draw();
        var nextLine = input.nextLine();
        if (nextLine.matches("^test [a-h][1-8]$")) {
            System.out.println(Arrays.stream(board.getPossibleMoves(Point.parse(nextLine.replaceFirst("test ", "")))).map(x -> x + " ")
                    .reduce("", (s, c) -> s + c));
            return true;
        }
        if (!nextLine.matches("^([a-h][1-8](\\s|$)){2}")) {
            return false;
        }
        var coords = Arrays.stream(nextLine.split("\\s")).map(Point::parse).toArray(Point[]::new);
        board.move(coords[0], coords[1]);
        return true;
    }

    private void draw()
    {
        //noinspection UnstableApiUsage
        Lists.reverse(
                Streams.mapWithIndex(
                        Arrays.stream(board.getBoard()),
                        (x, i) -> Streams.mapWithIndex(
                                Arrays.stream(x),
                                (y, j) -> Arrays.stream(UnitSymbols.values()).
                                        filter(s -> s.match(y.getHolding())).
                                        findFirst().
                                        orElse(
                                                (i + j) % 2 == 0 ?
                                                UnitSymbols.EvenCell :
                                                UnitSymbols.OddCell
                                        )
                        )
                ).
                map(x -> x.map(UnitSymbols::getSymbol).
                        reduce(
                                "",
                                (s, c) -> s + c
                        )
                        + "\n"
                ).
                collect(Collectors.toList())
        ).forEach(System.out::print);
    }
}
