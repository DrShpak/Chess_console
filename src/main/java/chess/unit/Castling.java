package chess.unit;

import chess.misc.Direction;
import chess.base.Team;

public abstract class Castling extends Unit {
    private boolean moved = false;

    Castling(Direction[] directions, Team team) {
        super(directions, team);
    }

    void handleMove() {
        this.moved = true;
    }

    public boolean isMoved() {
        return this.moved;
    }
}
