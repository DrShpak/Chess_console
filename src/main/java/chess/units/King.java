package chess.units;

import chess.board.IKingTracker;
import chess.board.IMoveHandler;
import chess.board.MoveHandlers;
import chess.misc.Direction;
import chess.misc.Point;
import chess.misc.Team;

public class King extends Castling implements IMoveHandler<IKingTracker> {
    public King(Team team) {
        super(new Direction[] {
                new Direction(1, 0, 1),
                new Direction(1, 1, 1),
                new Direction(-1, 0, 1),
                new Direction(0, 1, 1),
                new Direction(0, -1, 1),
                new Direction(-1, 1, 1),
                new Direction(1, -1, 1),
                new Direction(-1, -1, 1)
        }, team);
    }

    @Override
    public boolean isImportant() {
        return true;
    }

    @Override
    public void handleMove(IKingTracker feedback, Point oldPoint, Point newPoint) {
        handleMove();
        feedback.trackKing(this.getTeam(), newPoint);
    }

    @Override
    public void register(MoveHandlers registry) {
        registry.register(this);
    }
}