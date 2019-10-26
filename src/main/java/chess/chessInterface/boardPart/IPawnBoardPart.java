package chess.chessInterface.boardPart;

import chess.chessInterface.IFeedback;
import chess.misc.Point;
import chess.unit.Unit;

public interface IPawnBoardPart
extends IFeedback {
    void transformPawn(Unit unit, Point currPos);
    void markPawnEnPassant(Point pawnOldPosition, Point pawnNewPosition, Point enPassant);
    void attemptEnPassant(Point position);
}
