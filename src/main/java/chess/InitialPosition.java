package chess;

import chess.base.board.BoardBuilder;
import chess.base.board.ChessBoardImpl;
import chess.misc.Direction;
import chess.misc.MovePolicy;
import chess.unit.*;

public class InitialPosition {
    public static ChessBoardImpl get() {
        return new BoardBuilder().
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
                withGap().withTeam("Black").
                    withUnitRange(Pawn.class, 8, new Direction(-1, 0, 1, MovePolicy.WALK)).
                    withGroup(0).
                height(8).
                width(8).
                build();
    }
}
