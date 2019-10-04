package chess.board;

import chess.misc.*;
import chess.units.*;
import com.google.common.collect.Streams;
import org.javatuples.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ChessBoard
extends IBoard{
    private Cell[][] board;
    private MoveHandlers moveHandlers = new MoveHandlers();
    private Map<Team, Cell> kingsCells = new HashMap<>();

    ChessBoard(Unit[][] board) {
        this.board = Arrays.
                stream(board).
                map(x -> Arrays.stream(x).
                        map(Cell::new).toArray(Cell[]::new)).
                toArray(Cell[][]::new);
        //noinspection all
                Streams.mapWithIndex(
                        Arrays.stream(board),
                        (x, i) -> Streams.mapWithIndex(
                                Arrays.stream(x),
                                (y, j) -> new Pair<>(y, new Point(i, j))
                        )
                ).
                        flatMap(x -> x).
                        filter(x -> x.getValue0() instanceof IMoveHandler).
                        map(x -> new Pair<>((IMoveHandler<? super IBoard>)x.getValue0(), x.getValue1())).
                        forEach(x -> {
                            x.getValue0().register(moveHandlers);
                            x.getValue0().handleMove(this, null, x.getValue1());
                        });
    }

    public Point[] getPossibleMoves(Point unitCell) {
        var unit = board[unitCell.getX()][unitCell.getY()].getHolding();
        if (unit == null) {
            throw new NullPointerException();
        }

        return Arrays.stream(unit.getDirections()).
                map(
                        x -> StreamUtils.takeWhileEx(
                                x.move(unitCell),
                                y -> board[y.getX()][y.getY()].getHolding() == null
                        ).
                        filter(y -> board[y.getX()][y.getY()].getHolding() == null ||
                                x.getMovePolicy().compareTo(MovePolicy.BOTH) >= 0 &&
                                unit.isEnemy(board[y.getX()][y.getY()].getHolding())).
                        filter(y -> board[y.getX()][y.getY()].getHolding() != null ||
                                x.getMovePolicy().compareTo(MovePolicy.BOTH) <= 0).
                        filter(y -> checkFortified(unit, board[y.getX()][y.getY()]))
                ).
                flatMap(x -> x).
                toArray(Point[]::new);
    }

    private boolean checkFortified(Unit unit, Cell to) {
        if (unit.isFortified()) {
            return to.onFire(unit) < 1;
        }
        var fortifiedUnit = this.kingsCells.get(unit.team);
        if (fortifiedUnit.onFire(unit) > 1) {
            return false;
        }
        if (fortifiedUnit.onFire(unit) > 0) {
            var context = fortifiedUnit.getEnemy(unit);
            var equivalent = to.
                    getContexts().
                    stream().
                    filter(x -> x.isEquivalent(context)).
                    filter(x -> !x.isInvolved(fortifiedUnit.getHolding())).
                    findAny();
            return equivalent.isPresent();
        }
        var context = fortifiedUnit.
                getSleepingEnemy(unit).
                filter(x -> x.isInvolved(unit)).
                findAny().
                orElse(null);
        //todo check
        return (context == null) || (context.getBarrages().size() != 1) ||
                to.
                        getContexts().
                        stream().
                        anyMatch(context::isInferior);
    }
    //todo check bug(!)

    public void move(Point startPoint, Point endPoint) {
        var cell = board[startPoint.getX()][startPoint.getY()];
        if (cell.getHolding() == null) {
            throw new IllegalArgumentException("can`t move from " + startPoint + " to " + endPoint + " <= unit not found");
        }
        var possibleMoves = this.getPossibleMoves(startPoint);
        if (Arrays.stream(possibleMoves).noneMatch(x -> x.equals(endPoint))) {
            throw new IllegalArgumentException("can`t move from " + startPoint + " to " + endPoint + " <= forbidden move");
        }

        var newCell = board[endPoint.getX()][endPoint.getY()];

        onRaisingUnit(startPoint);
        if (newCell.getHolding() != null) {
            onRaisingUnit(endPoint);
            this.moveHandlers.handle(
                    this,
                    newCell.getHolding(),
                    endPoint,
                    null
            );
            this.moveHandlers.remove(newCell.getHolding());
        }
        Cell.moveUnit(cell, board[endPoint.getX()][endPoint.getY()]);
        onLowedUnit(endPoint);
        this.moveHandlers.handle(
                this,
                newCell.getHolding(),
                startPoint,
                endPoint
        );
    }

    private void onRaisingUnit(Point from) {
        var oldCell = getBoard()[from.getX()][from.getY()];
        oldCell.unpinContexts();
        oldCell.getContexts().forEach(x -> x.iterateContexts().forEach(y -> y.getBarrages().remove(oldCell.getHolding())));
    }

    private void onLowedUnit(Point to) {
        var newCell = getBoard()[to.getX()][to.getY()];
        newCell.
                getContexts().
                stream().
                map(AttackingContext::iterateContexts).
                flatMap(y -> y).
                forEach(y -> y.getBarrages().add(newCell.getHolding()));

        //var newContext = new AttackingContext(newCell.getHolding(), newCell);
        newCell.pinContexts(Arrays.stream(newCell.
                getHolding().
                getDirections()).
                filter(x -> x.getMovePolicy().compareTo(MovePolicy.BOTH) >= 0).
                map(x -> StreamUtils.mapEx(
                        (Pair<Cell, AttackingContext>)null,
                        x.move(to),
                        (y, z) -> new Pair<>(
                                getBoard()[z.getX()][z.getY()],
                                y != null ?
                                        new AttackingContext(y.getValue1()) :
                                        new AttackingContext(newCell.getHolding())
                            )
                        )
                ).
        flatMap(x -> x));
    }

    public Cell[][] getBoard() {
        return board;
    }

    @Override
    public void trackKing(Team team, Point newPoint) {
        this.kingsCells.put(team, board[newPoint.getX()][newPoint.getY()]);
    }
}
