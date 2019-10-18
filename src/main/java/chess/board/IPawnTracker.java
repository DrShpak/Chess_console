package chess.board;

import chess.misc.Point;
import chess.misc.Team;
import chess.units.Unit;

public interface IPawnTracker extends IFeedback {
    void trackingPawn(Unit unit, Point currPos);
}
