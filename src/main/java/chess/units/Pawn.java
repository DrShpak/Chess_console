package chess.units;

import chess.board.IMoveHandler;
import chess.board.IPawnMark;
import chess.board.MoveHandlers;
import chess.misc.Direction;
import chess.misc.MovePolicy;
import chess.misc.Point;
import chess.misc.Team;

@SuppressWarnings("unused")
public class Pawn extends Unit
implements IMoveHandler<IPawnMark> {
    public Pawn(Team team) {
        super(null);
        this.team = team;
    }

    public Pawn(Team team, Integer multiplier) {
        super(new Direction[] {
                new Direction((int)Math.signum(multiplier), 0, 1, MovePolicy.PEACE),
                //new Direction(2, 0, 1, MovePolicy.PEACE),
                new Direction((int)Math.signum(multiplier), 1, 1, MovePolicy.ATTACK),
                new Direction((int)Math.signum(multiplier), -1, 1, MovePolicy.ATTACK),
        });
        this.team = team;
    }

    @Override
    public void handleMove(IPawnMark feedback, Point oldPoint, Point newPoint) {
    }

    @Override
    public void register(MoveHandlers registry) {
        registry.register(this);
    }
}
