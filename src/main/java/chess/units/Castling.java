package chess.units;

import chess.misc.Direction;
import chess.misc.Team;

public abstract class Castling extends Unit {
    private int moveCount = 0;

    Castling(Direction[] directions, Team team) {
        super(directions, team);
    }

    void handleMove() {
        moveCount++;
    }

    public boolean isMoved() {
        return moveCount != 0;
    }
}
