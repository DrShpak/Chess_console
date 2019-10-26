package chess.base.board;

import chess.base.Cell;
import chess.chessInterface.IBoard;
import chess.chessInterface.IMoveHandler;
import chess.misc.Direction;
import chess.misc.Point;
import chess.misc.StreamUtils;
import chess.unit.Unit;
import com.google.common.collect.Streams;

import java.util.Arrays;
import java.util.Objects;

@SuppressWarnings("WeakerAccess")
public abstract class ChessBoardBase implements IBoard {
    private final Cell[][] board;
    private final MoveHandlers moveHandlers = new MoveHandlers();

    @SuppressWarnings("UnstableApiUsage")
    ChessBoardBase(Unit[][] board) {
        this.board = Arrays.
                stream(board).
                map(x -> Arrays.stream(x).
                        map(Cell::new).
                        toArray(Cell[]::new)
                ).
                toArray(Cell[][]::new);

        Streams.mapWithIndex(
                Arrays.stream(board), (units, i) ->
                        Streams.mapWithIndex(
                                Arrays.stream(units).
                                        filter(Objects::nonNull),
                                (unit, j) -> new Point(i, j)
                        )
        ).
                flatMap(x -> x).
                forEach(this::initUnit);
    }

    @SuppressWarnings("unchecked")
    protected void initUnit(Point position) {
        this.onLowedUnit(position);
        var unit = this.getCell(position).getUnit();
        if (unit instanceof IMoveHandler) {
            var handler = (IMoveHandler<? super IBoard>)unit;
            handler.register(this.moveHandlers);
            handler.handleMove(this, position, position);
        }
    }

    /**
     * Get all possible movements for unit on unitPos allowed by game rules
     *
     * @param unitPos unit position
     * @return all possible movements for unit on unitPos
     * @throws IllegalArgumentException if there is no unit on unitPos
     */
    public Point[] getPossibleMovements(Point unitPos) {
        var unit = getCell(unitPos).getUnit();
        if (unit == null) {
            throw new IllegalArgumentException("there is no unit on " + unitPos);
        }

        return Arrays.stream(unit.getDirections()).
                map(direction ->
                        StreamUtils.takeWhileEx
                                (
                                    direction.getPointsAlong(unitPos),
                                    point -> getCell(point).isEmpty()
                                ).
                                filter(point -> checkPolicyRestrictions(unit, point, direction)).
                                filter(point -> checkDefense(unit, getCell(point)))
                ).
                flatMap(x -> x).
                toArray(Point[]::new);
    }

    /**
     * Forbid {@link Unit}
     * to move to {@link Point}
     * if such a movement violates policy restrictions for {@link Direction}
     *
     * @param unit      moving unit
     * @param point     destination
     * @param direction direction of movement
     * @return check result
     */
    private boolean checkPolicyRestrictions(Unit unit, Point point, Direction direction) {
        return getCell(point).hasEnemyUnit(unit) && direction.getMovePolicy().allowAttack()
                || getCell(point).isEmpty() && direction.getMovePolicy().allowWalk();
    }

    /**
     * Forbid {@link Unit}
     * destination move destination {@link Point}
     * if such a movement cause destination King`s defenselessness
     *
     * @param unit        moving unit
     * @param destination destination cell
     * @return check result
     */
    private boolean checkDefense(Unit unit, Cell destination) {
        var importantUnitCell = this.getImportantUnitCell(unit);
        if (unit.isImportant()) {
            return this.checkKingMovement(unit, destination);
        } else return
                this.checkDoubleShah(importantUnitCell, unit) &&
                        this.checkSingleShah(importantUnitCell, destination, unit) &&
                        this.checkBack(importantUnitCell, unit, destination);
    }//todo check bugs(1 and 2)

    protected abstract Cell getImportantUnitCell(Unit friendlyUnit);

    protected boolean checkKingMovement(Unit unit, Cell destination) {
        return destination.countAttackers(unit) < 1;
    }

    private boolean checkDoubleShah(Cell cell, Unit unit) {
        return cell.countAttackers(unit) <= 1;
    }

    private boolean checkSingleShah(Cell cell, Cell destination, Unit unit) {
        if (cell.countAttackers(unit) > 0) {
            return destination.getEquivalentContext(
                    cell.getAttackerContext(unit),
                    cell.getUnit()
            ).isPresent();
        }
        return true;
    }

    private boolean checkBack(Cell importantUnitCell, Unit unit, Cell destination) {
        var context = importantUnitCell.
                getPotentialAttackers(unit).
                filter(x -> x.isInvolved(unit)).
                findAny().
                orElse(null);
        return  (context == null) ||
                (context.getBarrages().size() != 1) ||
                 destination.hasInferiorContext(context);
    }

    private boolean checkMove(Point startPoint, Point endPoint) {
        return Arrays.asList(this.getPossibleMovements(startPoint)).contains(endPoint);
    }

    public void move(Point startPoint, Point endPoint) {
        if (this.getCell(startPoint).getUnit() == null) {
            throw new IllegalArgumentException("can`t move from " + startPoint + " to " + endPoint + " <= unit not found");
        }
        if (!this.checkMove(startPoint, endPoint)) {
            throw new IllegalArgumentException("can`t move from " + startPoint + " to " + endPoint + " <= forbidden move");
        }
        moveInternal(startPoint, endPoint);
        this.postMove(startPoint, endPoint);
    }

    protected void moveInternal(Point startPoint, Point endPoint) {
        if (this.getCell(endPoint).getUnit() != null) {
            chopFigure(endPoint);
        }
        if (!startPoint.equals(endPoint)) {
            doMoveFigure(startPoint, endPoint);
        }
    }

    protected void newUnit(Unit unit, Point pos) {
        this.getCell(pos).replace(unit);
        this.initUnit(pos);
    }

    private void chopFigure(Point pos) {
        onRaisingUnit(pos);
        var choppedFigure = getCell(pos).getUnit();
        this.moveHandlers.remove(choppedFigure);
        getCell(pos).replace(null);
    }

    private void doMoveFigure(Point startPoint, Point endPoint) {
        onRaisingUnit(startPoint);
        Cell.moveUnit(getCell(startPoint), getCell(endPoint));
        onLowedUnit(endPoint);
        this.moveHandlers.handle(
                this,
                getCell(endPoint).getUnit(),
                startPoint, endPoint
        );
    }

    private void onRaisingUnit(Point from) {
        var oldCell = this.getCell(from);
        oldCell.unpinContexts();
        oldCell.removeBarrageFromContexts();
    }

    private void onLowedUnit(Point to) {
        var newCell = getCell(to);
        newCell.addBarrageToContexts();
        newCell.emitContexts(to, this::getCell);
//        addBarrages();
    }

    protected Cell getCell(Point pos) {
        return this.getBoard()[pos.getX()][pos.getY()];
    }

    protected abstract void postMove(Point from, Point to);

    public Cell[][] getBoard() {
        return board;
    }
}