package chess.units;

import chess.board.IKingTracker;
import chess.board.IMoveHandler;
import chess.board.MoveHandlers;
import chess.misc.Direction;
import chess.misc.Point;
import chess.misc.Team;

public class King extends Unit
implements IMoveHandler<IKingTracker> {
    public King(Team team) {
        super(new Direction[] {
                new Direction(1, 0, 1),
                new Direction(-1, 0, 1),
                new Direction(0, 1, 1),
                new Direction(0, -1, 1),
                new Direction(0, -1, 1),
                new Direction(1, -1, 1),
                new Direction(-1, 1, 1),
                new Direction(1, -1, 1),
                new Direction(1, -1, 1)
        });
        this.team = team;
    }

    @Override
    public boolean isFortified() {
        return true;
    }

    @Override
    public void handleMove(IKingTracker feedback, Point oldPoint, Point newPoint) {
        feedback.trackKing(team, newPoint);
    }

    @Override
    public void register(MoveHandlers registry) {
        registry.register(this);
    }
}