package chess.units;

import chess.board.IMoveHandler;
import chess.board.IPawnTracker;
import chess.board.MoveHandlers;
import chess.misc.Direction;
import chess.misc.MovePolicy;
import chess.misc.Point;
import chess.misc.Team;

@SuppressWarnings("unused")
public class Pawn extends Unit implements IMoveHandler<IPawnTracker> {

    public Pawn(Team team) {
        super(null, team);
    }

    public Pawn(Team team, Direction direction) {
        super(new Direction[] {
                direction,
                new Direction(direction.getDx() * 2, direction.getDy(), direction.getMaxLength(), MovePolicy.WALK),
                new Direction(direction.getDx(), direction.getDy() + 1, direction.getMaxLength(), MovePolicy.ATTACK),
                new Direction(direction.getDx(), direction.getDy() - 1, direction.getMaxLength(), MovePolicy.ATTACK),
        }, team);
    }


    @Override
    public void handleMove(IPawnTracker feedback, Point oldPoint, Point newPoint) {
        if (newPoint == null)
            return;
        if (this.getDirections()[0].getPointsAlong(newPoint).count() == 0) {
            feedback.trackingPawn(this, newPoint);
        }
    }

    @Override
    public void register(MoveHandlers registry) {
        registry.register(this);
    }
}
