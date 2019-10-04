package chess.board;

import chess.misc.Point;
import chess.misc.Team;

public interface IKingTracker
extends IFeedback {
    void trackKing(Team team, Point newPoint);
}
