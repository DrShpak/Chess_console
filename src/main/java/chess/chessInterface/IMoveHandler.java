package chess.chessInterface;

import chess.base.board.MoveHandlers;
import chess.misc.Point;

public interface IMoveHandler<Feedback extends IFeedback> {
    void handleMove(Feedback feedback, Point oldPoint, Point newPoint);
    void register(MoveHandlers registry);
}
