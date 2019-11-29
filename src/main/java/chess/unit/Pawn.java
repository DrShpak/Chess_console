package chess.unit;

import chess.chessInterface.IMoveHandler;
import chess.chessInterface.boardPart.IPawnBoardPart;
import chess.base.board.MoveHandlers;
import chess.misc.Direction;
import chess.misc.MovePolicy;
import chess.misc.Point;
import chess.base.Team;
import xml.XML;

@XML
@SuppressWarnings("unused")
public class Pawn extends Unit implements IMoveHandler<IPawnBoardPart> {
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

    @SuppressWarnings("unused")
    public Pawn() {
        super(new Direction[0], Team.INVALID_TEAM);
    }

    @Override
    public void handleMove(IPawnBoardPart feedback, Point oldPoint, Point newPoint) {
        if (this.getDirections()[0].getPointsAlong(newPoint).count() == 0) {
            feedback.transformPawn(this, newPoint);
        } else {
            var direction = Direction.getAvgDistance(Point.diff(newPoint, oldPoint));
            direction.
                    getPointsAlong(oldPoint).
                    findFirst().
                    ifPresentOrElse(
                     x -> feedback.markPawnEnPassant(oldPoint, newPoint, x),
                    () -> feedback.attemptEnPassant(newPoint)
            );
        }
    }

    @Override
    public void register(MoveHandlers registry) {
        registry.register(this);
    }
}
