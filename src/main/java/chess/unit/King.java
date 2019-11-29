package chess.unit;

import chess.chessInterface.boardPart.IKingBoardPart;
import chess.chessInterface.IMoveHandler;
import chess.base.board.MoveHandlers;
import chess.misc.Direction;
import chess.misc.Point;
import chess.base.Team;
import xml.XML;

@XML
public class King extends Castling implements IMoveHandler<IKingBoardPart> {
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

    @SuppressWarnings("unused")
    public King() {
        super(new Direction[0], Team.INVALID_TEAM);
    }

    @Override
    public boolean isImportant() {
        return true;
    }

    @Override
    public void handleMove(IKingBoardPart feedback, Point oldPoint, Point newPoint) {
        handleMove();
        feedback.trackKing(this.getTeam(), newPoint);
    }

    @Override
    public void register(MoveHandlers registry) {
        registry.register(this);
    }
}
