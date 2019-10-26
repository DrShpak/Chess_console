package chess.unit;

import chess.misc.Direction;
import chess.base.Team;

public class Queen extends Unit {
    public Queen(Team team) {
        super(new Direction[] {
                new Direction(1, 0),
                new Direction(1, 1),
                new Direction(-1, 0),
                new Direction(0, 1),
                new Direction(0, -1),
                new Direction(-1, 1),
                new Direction(1, -1),
                new Direction(-1, -1),
        }, team);
    }
}
