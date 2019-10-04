package chess.units;

import chess.misc.Direction;
import chess.misc.Team;

public class Knight extends Unit {
    public Knight(Team team) {
        super(new Direction[] {
                new Direction(1, 2, 1),
                new Direction(-1, 2, 1),
                new Direction(1, -2, 1),
                new Direction(-1, -2, 1),
                new Direction(2, 1, 1),
                new Direction(-2, 1, 1),
                new Direction(2, -1, 1),
                new Direction(-2, -1, 1)
        });
        this.team = team;
    }

}
