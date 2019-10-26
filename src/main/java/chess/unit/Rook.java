package chess.unit;

import chess.chessInterface.IFeedback;
import chess.chessInterface.IMoveHandler;
import chess.base.board.MoveHandlers;
import chess.misc.Direction;
import chess.misc.Point;
import chess.base.Team;

public class Rook extends Castling implements IMoveHandler<IFeedback> {
    public Rook(Team team) {
        super(new Direction[]{
                new Direction(1, 0),
                new Direction(-1, 0),
                new Direction(0, 1),
                new Direction(0, -1)
        }, team);
    }

    @Override
    public void handleMove(IFeedback feedback, Point oldPoint, Point newPoint) {
        handleMove();
    }

    @Override
    public void register(MoveHandlers registry) {
        registry.register(this);
    }
}
