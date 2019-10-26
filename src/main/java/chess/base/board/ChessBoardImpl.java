package chess.base.board;

import chess.base.Cell;
import chess.base.Team;
import chess.misc.CastlingType;
import chess.misc.Direction;
import chess.misc.Point;
import chess.unit.Castling;
import chess.unit.Dummy;
import chess.unit.Queen;
import chess.unit.Unit;
import org.javatuples.Triplet;

import java.util.HashMap;
import java.util.Map;

public final class ChessBoardImpl extends ChessBoardBase {
    private Map<Team, Cell> kingsCells;

    private Map<Team, Cell> getKingsCells() {
        if (this.kingsCells == null) {
            this.kingsCells = new HashMap<>();
        }
        return this.kingsCells;
    }

    ChessBoardImpl(Unit[][] board) {
        super(board);
    }

    @Override
    protected Cell getImportantUnitCell(Unit friendlyUnit) {
        return this.getKingsCells().get(friendlyUnit.getTeam());
    }

    @Override
    public void trackKing(Team team, Point newPoint) {
        this.getKingsCells().put(team, this.getCell(newPoint));
    }

    @Override
    public void transformPawn(Unit unit, Point currPos) {
        var newUnit = new Queen(unit.getTeam());
        this.newUnit(newUnit, currPos);
    }

    private Triplet<Point, Point, Point> enPassant;

    @Override
    public void markPawnEnPassant(Point pawnOldPosition, Point pawnNewPosition, Point enPassant) {
        this.cleanEnPassant(pawnNewPosition);
        this.enPassant = new Triplet<>(pawnOldPosition, pawnNewPosition, enPassant);

        var dummy = new Dummy(Team.INVALID_TEAM);
        this.newUnit(dummy, enPassant);
    }

    @Override
    public void attemptEnPassant(Point position) {
        if (this.enPassant != null && this.enPassant.getValue2().equals(position)) {
            this.moveInternal(this.enPassant.getValue1(), this.enPassant.getValue1());
        }
    }

    @Override
    protected void postMove(Point from, Point to) {
        if (this.enPassant != null && (!this.enPassant.getValue0().equals(from) || !this.enPassant.getValue1().equals(to))) {
            cleanEnPassant(to);
        }
    }

    private void cleanEnPassant(Point to) {
        if (this.enPassant != null && !this.enPassant.getValue2().equals(to)) {
            this.moveInternal(this.enPassant.getValue2(), this.enPassant.getValue2());
        }
        this.enPassant = null;
    }

    private boolean checkMovementsForCastling(Direction direction, Point start) {
        var king = getCell(start).getUnit();
        return direction.getPointsAlong(start).takeWhile(point -> checkKingMovement(king, getCell(point))).count() == direction.getMaxLength();
    }

    public void makeCastling(CastlingType type, Point start) {
        var king = getCell(start).getUnit();
        if (king != null && ((Castling) king).isMoved()) {
            var direction = type.getDirection();
            if (checkMovementsForCastling(direction, start)) {
                var end = new Point(start.getX(), start.getY() + (int) Math.signum(direction.getDy()) * direction.getMaxLength());
                var startRookPoint = Point.sum(
                        end,
                        new Point(0, (type == CastlingType.LONG ? 2 : 1) * (int) Math.signum(direction.getDy()))
                );
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
