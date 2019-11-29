package chess.unit;

import chess.base.Team;
import chess.misc.Direction;
import xml.XML;

@XML
public class Dummy extends Unit {
    public Dummy(Team team) {
        super(new Direction[] {}, team);
    }

    @SuppressWarnings("unused")
    public Dummy() {
        super(new Direction[0], Team.INVALID_TEAM);
    }
}
