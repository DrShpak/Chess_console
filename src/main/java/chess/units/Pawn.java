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
        super(null, team);
    }

    public Pawn(Team team, Integer multiplier) {
        super(new Direction[] {
                new Direction((int)Math.signum(multiplier), 0, 1, MovePolicy.WALK),
                new Direction((int)Math.signum(multiplier) * 2, 0, 1, MovePolicy.WALK),
                new Direction((int)Math.signum(multiplier), 1, 1, MovePolicy.ATTACK),
                new Direction((int)Math.signum(multiplier), -1, 1, MovePolicy.ATTACK),
        }, team);
    }

    @Override
    public void handleMove(IPawnMark feedback, Point oldPoint, Point newPoint) {
        oldPoint.notify();//todo dummy
    }

    @Override
    public void register(MoveHandlers registry) {
        registry.register(this);
    }
}
