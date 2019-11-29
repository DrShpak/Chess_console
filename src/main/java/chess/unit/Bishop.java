package chess.unit;

import chess.misc.Direction;
import chess.base.Team;
import xml.XML;

@XML
public class Bishop extends Unit {
    public Bishop(Team team) {
        super(new Direction[] {
                new Direction(1, 1),
                new Direction(-1, 1),
                new Direction(1, -1),
                new Direction(-1, -1)
        }, team);
    }

    @SuppressWarnings("unused")
    public Bishop() {
        super(new Direction[0], Team.INVALID_TEAM);
    }
}
