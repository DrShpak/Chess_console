package chess.units;

import chess.misc.Direction;
import chess.misc.Team;

public class Queen extends Unit {
    public Queen(Team team) {
        super(new Direction[] {
                new Direction(1, 0),
                new Direction(-1, 0),
                new Direction(0, 1),
                new Direction(0, -1),
                new Direction(0, -1),
                new Direction(1, -1),
                new Direction(-1, 1),
                new Direction(1, -1),
                new Direction(1, -1)
        });
        this.team = team;
    }

}
