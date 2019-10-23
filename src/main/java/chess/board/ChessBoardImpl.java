package chess.board;

import chess.misc.*;
import chess.units.*;
import com.google.common.collect.Streams;
import org.javatuples.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ChessBoardImpl implements IBoard {
    private final Cell[][] board;
    private final MoveHandlers moveHandlers = new MoveHandlers();
    private final Map<Team, Cell> kingsCells = new HashMap<>();

    ChessBoardImpl(Unit[][] board) {
        this.board = Arrays.
                stream(board).
                map(x -> Arrays.stream(x).
                        map(Cell::new).
                        toArray(Cell[]::new)
                ).
                toArray(Cell[][]::new);
        //noinspection UnstableApiUsage,unchecked
        Streams.mapWithIndex(
                Arrays.stream(board), (units, i) ->
                        Streams.mapWithIndex(
                                Arrays.stream(units), (unit, j) -> new Pair<>(unit, new Point(i, j))
                        )
        ).
                flatMap(x -> x).
                filter(pair -> pair.getValue0() instanceof IMoveHandler).
                map(pair -> new Pair<>((IMoveHandler<? super IKingTracker>) pair.getValue0(), pair.getValue1())).
                forEach(pair -> {
                    pair.getValue0().register(moveHandlers);
                    pair.getValue0().handleMove(this, null, pair.getValue1());
                });
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

    private boolean checkMovementsForCastling(Direction direction, Point start) {
        var king = getCell(start).getUnit();
        return direction.getPointsAlong(start).takeWhile(point -> checkKingMovement(king, getCell(point))).count() == direction.getMaxLength();
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
        var importantUnitCell = this.kingsCells.get(unit.getTeam());
        if (unit.isImportant()) {
            return this.checkKingMovement(unit, destination);
        } else return
                this.checkDoubleShah(importantUnitCell, unit) &&
                        this.checkSingleShah(importantUnitCell, destination, unit) &&
                        this.checkBack(importantUnitCell, unit, destination);
    }

    private boolean checkKingMovement(Unit unit, Cell destination) {
        return destination.countAttackers(unit) < 1;
    }

    private boolean checkDoubleShah(Cell cell, Unit unit) {
        return cell.countAttackers(unit) <= 1;
    }

    private boolean checkSingleShah(Cell cell, Cell destination, Unit unit) {
        if (cell.countAttackers(unit) > 0) {
            return this.getEquivalentContext(
                    destination,
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
        return (context == null) || (context.getBarrages().size() != 1) ||
                destination.
                        getContexts().
                        stream().
                        anyMatch(context::isInferior);
    }

    private Optional<AttackingContext> getEquivalentContext(Cell destination, AttackingContext otherContext, Unit importantUnit) {
        return destination.
                getContexts().
                stream().
                filter(x -> x.isEquivalent(otherContext)).
                filter(x -> !x.getBarrages().contains(importantUnit)).
                findAny();
    }

    private boolean checkMove(Point startPoint, Point endPoint) {
        return Arrays.asList(this.getPossibleMovements(startPoint)).contains(endPoint);
    }

    public void move(Point startPoint, Point endPoint) {
        var startCell = board[startPoint.getX()][startPoint.getY()];
        if (startCell.getUnit() == null) {
            throw new IllegalArgumentException("can`t move from " + startPoint + " to " + endPoint + " <= unit not found");
        }
        if (!this.checkMove(startPoint, endPoint)) {
            throw new IllegalArgumentException("can`t move from " + startPoint + " to " + endPoint + " <= forbidden move");
        }

        moveInternal(startPoint, endPoint);
    }

    private void moveInternal(Point startPoint, Point endPoint) {
        var startCell = board[startPoint.getX()][startPoint.getY()];
        var endCell = board[endPoint.getX()][endPoint.getY()];
        onRaisingUnit(startPoint);
        if (endCell.getUnit() != null) {
            chopFigure(endPoint);
        }
        Cell.moveUnit(startCell, getCell(endPoint));
        onLowedUnit(endPoint);

        this.moveHandlers.handle(
                this, endCell.getUnit(),
                startPoint, endPoint
        );
    }


    private void chopFigure(Point pos) {
        onRaisingUnit(pos);
        // getCell(pos).getUnit() - chopped figure
        this.moveHandlers.handle(
                this, getCell(pos).getUnit(),
                pos, null
        );
        this.moveHandlers.remove(getCell(pos).getUnit());
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

    public Cell[][] getBoard() {
        return board;
    }

    private Cell getCell(Point coords) {
        return this.board[coords.getX()][coords.getY()];
    }

    @Override
    public void trackKing(Team team, Point newPoint) {
        this.kingsCells.put(team, board[newPoint.getX()][newPoint.getY()]);
    }

    @Override
    public void trackingPawn(Unit unit, Point currPos) {
        var team = unit.getTeam();
        unit = new Queen(team);
        this.getCell(currPos).replace(unit);
    }


    public void makeCastling(CastlingType type, Point start) {
        var king = getCell(start).getUnit();
        if (king != null && ((Castling) king).isMoved()) {
            var direction = type.getDirection();
            if (checkMovementsForCastling(direction, start)) {
                Point end = new Point(start.getX(), start.getY() + direction.getDy() * direction.getMaxLength());

                var startRookPoint = Point.sum(end, new Point(0, (type == CastlingType.LONG ? 2 : 1) * (int) Math.signum(direction.getDy())));
                var endRookPoint = Point.sum(end, new Point(0, - (int) Math.signum(direction.getDy())));

                var rook = getCell(startRookPoint).getUnit();
                if (rook != null && ((Castling) rook).isMoved()) {
                    moveInternal(start, end);
                    moveInternal(startRookPoint, endRookPoint);
                }
            }
        }
    }
}