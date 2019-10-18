package chess;

import chess.board.BoardBuilder;
import chess.misc.Direction;
import chess.misc.MovePolicy;
import chess.ui.UI;
import chess.ui.impl.consoleUI.ConsoleUI;
import chess.units.*;


class Main {
    public static void main(String[] args) {
        var board = new BoardBuilder().
            startGroup().
                    withUnit(Rook.class).
                    withUnit(Knight.class).
                    withUnit(Bishop.class).
                    withUnit(Queen.class).
                    withUnit(King.class).
                    withUnit(Bishop.class).
                    withUnit(Knight.class).
                    withUnit(Rook.class).
                endGroup().
                withTeam("White").
                    withGroup(0).
                    withUnitRange(Pawn.class, 8, new Direction(1, 0, 1, MovePolicy.WALK)).
                withGap().
                withTeam("Black").
                    withUnitRange(Pawn.class, 8, new Direction(-1, 0, 1, MovePolicy.WALK)).
                    withGroup(0).
                height(8).
                width(8).
                build();
        UI ui = new ConsoleUI(board);
        ui.start();
    }
}
