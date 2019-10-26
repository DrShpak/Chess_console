package chess.unit;

import chess.base.Team;
import chess.misc.Direction;

public class Dummy extends Unit {
    public Dummy(Team team) {
        super(new Direction[] {}, team);
    }
}
