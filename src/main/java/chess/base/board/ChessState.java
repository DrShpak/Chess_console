package chess.base.board;

import chess.base.Team;
import chess.misc.CastlingType;
import chess.misc.Point;
import chess.unit.King;
import com.google.common.collect.Streams;
import org.apache.commons.lang3.SerializationUtils;
import xml.XML;

import java.io.Serializable;
import java.util.LinkedList;

@XML(isClone=true)
public class ChessState implements Serializable {
    private GameState state = GameState.RUNNING;
    private static final int MAX_EQUAL_MOVES = 3;
    private static final int MAX_REVERSIBLE_MOVES = 50;

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

    @SuppressWarnings("unused")
    public ChessState() {

    }

    public void move(Point start, Point end) {
        checkGameState();
        this.positionHistory.add((ChessBoardImpl)this.getHeadBoard().fork());
        this.getHeadBoard().move(start, end);
        updateGameState();
    }

    public void makeCastling(CastlingType type, Point point) {
        checkGameState();
        this.getHeadBoard().makeCastling(type, point);
        updateGameState();
    }

    public void undo(int length) {
        checkGameState();
        if (this.positionHistory.size() <= length) {
            throw new IllegalStateException("board history less than requested undo length");
        }
        for (int i = 0; i < length; i++) {
            this.positionHistory.removeLast();
        }
        updateGameState();
    }

    private void checkGameState() {
        if (this.state != GameState.RUNNING) {
            throw new IllegalStateException();
        }
    }

    private void updateGameState() {
        this.state =
                checkMat() ? GameState.MAT :
                checkStalemate() ? GameState.STALEMATE :
                checkDraw() ? GameState.DRAW :
                GameState.RUNNING;
    }

    private boolean checkMat() {
        return getHeadBoard().
                getUnitCells().
                filter(x -> !x.getUnit().isEnemy(getHeadBoard().getCurrentTeam())).
                filter(x -> x.getUnit() instanceof King).
                anyMatch(x -> x.countAttackers(x.getUnit()) > 0) &&
               this.checkStalemate();
    }

    private boolean checkStalemate() {
       return getHeadBoard().
               getUnitCells().
               filter(x -> !x.getUnit().isEnemy(getHeadBoard().getCurrentTeam())).
               allMatch(x -> getPossibleMovements(x.getPosition()).length < 1);
    }

    private boolean checkDraw() {
        return checkReversibleLength() > MAX_REVERSIBLE_MOVES || checkEqualsLength() > MAX_EQUAL_MOVES;
    }

    private int checkReversibleLength() {
        //noinspection UnstableApiUsage
        return Streams.mapWithIndex(
                positionHistory.stream(),
                (x, y) -> y == 0 ? 0 : positionHistory.subList((int)y, positionHistory.size()).stream().
                        noneMatch(ChessBoardImpl::isIrreversible) ? positionHistory.size() - (int)y : 0
        ).mapToInt(x -> x).max().orElse(0);
    }

    private int checkEqualsLength() {
        //noinspection UnstableApiUsage,OptionalGetWithoutIsPresent
        return Streams.mapWithIndex(
                positionHistory.stream(),
                (x, y) -> (int)positionHistory.subList((int)y, positionHistory.size()).stream().
                        filter(z -> z.equals(x)).count()
        ).mapToInt(x -> x).max().getAsInt();
    }

    public GameState getState() {
        return state;
    }

    public Team getCurrentTeam() {
        return this.getHeadBoard().getCurrentTeam();
    }

    @SuppressWarnings("unused")
    public ChessState fork() {
        return SerializationUtils.clone(this);
    }
}
