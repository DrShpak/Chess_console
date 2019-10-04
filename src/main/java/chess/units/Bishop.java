package chess.units;

import chess.misc.Direction;
import chess.misc.Team;

public class Bishop extends Unit {
    public Bishop(Team team) {
        super(new Direction[] {
                new Direction(1, 1),
                new Direction(-1, 1),
                new Direction(1, -1),
                new Direction(-1, -1)
        });
        this.team = team;
    }

}
