package chess.chessInterface.boardPart;

import chess.chessInterface.IFeedback;
import chess.misc.Point;
import chess.base.Team;

public interface IKingBoardPart
extends IFeedback {
    void trackKing(Team team, Point newPoint);
}
