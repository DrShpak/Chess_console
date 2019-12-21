package chess.ui.impl.consoleUI;

import chess.SaverUtils;
import chess.base.board.ChessState;
import chess.base.board.GameState;
import chess.misc.CastlingType;
import chess.misc.Point;
import chess.ui.UI;
import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import org.apache.commons.lang3.SystemUtils;

import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConsoleUI
extends UI {
    private final AtomicBoolean finished = new AtomicBoolean(false);

    private final Scanner input = new Scanner(System.in);

    public ConsoleUI(ChessState chessState) {
        super(chessState);
    }

    @Override
    public void start() {
        var whitePlayer = new Thread(() -> {
            try {
                this.play("White");
            } catch (InterruptedException e) {
                throw new IllegalStateException("interrupted");
            }
        });
        var blackPlayer = new Thread(() -> {
            try {
                this.play("Black");
            } catch (InterruptedException e) {
                throw new IllegalStateException("interrupted");
            }
        });
        blackPlayer.start();
        whitePlayer.start();
    }

    private void play(String teamTag) throws InterruptedException {
        //noinspection StatementWithEmptyBody
        while (frame(teamTag)) ;
    }

    private synchronized boolean frame(String teamTag) throws InterruptedException {
        if (!chessState.getCurrentTeam().getTeamTag().equals(teamTag)) {
            this.notify();
            this.wait();
        }
        if (this.finished.get()) {
            return false;
        }
        draw();
        System.out.println(teamTag);
        if (chessState.getState() != GameState.RUNNING) {
            System.out.println(chessState.getState() + "!Program terminated");
            this.finished.set(true);
            this.notify();
            return false;
        }
        var nextLine = input.nextLine();
        if (nextLine.matches("^test [a-h][1-8]$")) {
            System.out.println(Arrays.stream(chessState.getPossibleMovements(Point.parse(nextLine.replaceFirst("test ", "")))).map(x -> x + " ")
                .reduce("", (s, c) -> s + c));
            return true;
        }
        if (nextLine.matches("save")) {
            SaverUtils.saveState(this.chessState);
            return true;
        }
        if (nextLine.matches("load")) {
            this.chessState = SaverUtils.loadState();
            return true;
        }
        if (nextLine.matches("undo (\\d)")) {
            //noinspection OptionalGetWithoutIsPresent
            var length = Integer.parseInt(Pattern.compile("undo (\\d)")
                    .matcher(nextLine).
                    results().
                    findFirst().
                    get().
                    group(1));
            chessState.undo(length);
            return true;
        }
        if (nextLine.matches("^0-0(-0)? [a-h][1-8]")) {
            String[] temp = nextLine.split("\\s");
            chessState.makeCastling(CastlingType.parse(temp[0]), Point.parse(temp[1]));
            return true;
        }
        if (!nextLine.matches("^([a-h][1-8](\\s|$)){2}")) {
            this.finished.set(true);
            this.notify();
            return false;
        }
        var pos = Arrays.stream(nextLine.split("\\s")).map(Point::parse).toArray(Point[]::new);
        chessState.move(pos[0], pos[1]);
        return true;
    }

    private void draw() {
        //noinspection UnstableApiUsage
        Lists.reverse(
            Streams.mapWithIndex
                (
                    Arrays.stream(chessState.getBoard().getBoard()),
                    (x, i) -> Streams.mapWithIndex
                        (
                            Arrays.stream(x), (y, j) ->
                                Arrays.stream(UnitSymbols.values()).
                                    filter(s -> s.match(y.getUnit())).
                                    findFirst().
                                    orElse
                                        (
                                            (i + j) % 2 == 0 ?
                                                SystemUtils.IS_OS_WINDOWS ? UnitSymbols.EvenCellNT : UnitSymbols.EvenCell :
                                                SystemUtils.IS_OS_WINDOWS ? UnitSymbols.OddCellNT : UnitSymbols.OddCell
                                        )
                        )
                ).
                map(x -> x.map(y -> y.getSymbol() + " ").
                    reduce("", (s, c) -> s + c) + "\n").
                collect(Collectors.toList())
        ).forEach(System.out::print);
    }
}
