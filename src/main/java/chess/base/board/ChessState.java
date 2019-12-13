package chess.base.board;

import chess.misc.CastlingType;
import chess.misc.Point;
import com.google.common.collect.Streams;
import xml.XML;

import java.util.LinkedList;

@XML(isClone=true)
public class ChessState {
    private static final int MAX_EQUAL_MOVES = 1;
    private static final int MAX_REVERSIBLE_MOVES = 5;

    @XML
    private final LinkedList<ChessBoardImpl> positionHistory = new LinkedList<>();

    public ChessState(ChessBoardImpl boardSeed) {
        this.positionHistory.add(boardSeed);
    }

    private ChessBoardImpl getHeadBoard() {
        return this.positionHistory.getLast();
    }

    public ChessBoardImpl getBoard() {
        return this.getHeadBoard();
    }

    public Point[] getPossibleMovements(Point point) {
        return this.getHeadBoard().getPossibleMovements(point);
    }

    public ChessState() {

    }

    public void move(Point start, Point end) {
        this.positionHistory.add((ChessBoardImpl)this.getHeadBoard().fork());
        this.getHeadBoard().move(start, end);
    }

    public void makeCastling(CastlingType type, Point point) {
        this.getHeadBoard().makeCastling(type, point);
    }

    public void undo(int length) {
        if (this.positionHistory.size() <= length) {
            throw new IllegalStateException("board history less than requested undo length");
        }
        for (int i = 0; i < length; i++) {
            this.positionHistory.removeLast();
        }
    }

    public boolean checkDraw() {
        return checkReversibleLength() > MAX_REVERSIBLE_MOVES || checkEqualsLength() > MAX_EQUAL_MOVES;
    }

    public int checkReversibleLength() {
        //noinspection UnstableApiUsage
        return Streams.mapWithIndex(
                positionHistory.stream(),
                (x, y) -> y == 0 ? 0 : positionHistory.subList((int)y, positionHistory.size()).stream().
                        noneMatch(ChessBoardImpl::isIrreversible) ? positionHistory.size() - (int)y : 0
        ).mapToInt(x -> x).max().orElse(0);
    }

    public int checkEqualsLength() {
        //noinspection UnstableApiUsage,OptionalGetWithoutIsPresent
        return Streams.mapWithIndex(
                positionHistory.stream(),
                (x, y) -> (int)positionHistory.subList((int)y, positionHistory.size()).stream().
                        filter(z -> z.equals(x)).count()
        ).mapToInt(x -> x).max().getAsInt();
    }
}
