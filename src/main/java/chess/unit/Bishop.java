package chess.unit;

import chess.misc.Direction;
import chess.base.Team;

public class Bishop extends Unit {
    public Bishop(Team team) {
        super(new Direction[] {
                new Direction(1, 1),
                new Direction(-1, 1),
                new Direction(1, -1),
                new Direction(-1, -1)
        }, team);
    }
}
