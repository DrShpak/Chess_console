package chess.units;

import chess.misc.Direction;
import chess.misc.Point;
import chess.misc.Team;

public class Bishop extends Unit {
    public Bishop(Team team) {
        super(new Direction[] {
                new Direction(1, 1),
                new Direction(-1, 1),
                new Direction(1, -1),
                new Direction(-1, -1)
        });
        this.team = team;
    }

    @Override
    public boolean canMove(Point startPoint, Point endPoint) {
        var diff = Point.diff(endPoint, startPoint);
        return Math.abs(diff.getY()) == Math.abs(diff.getX());
    }
    @Override
    public boolean canAttack(Point startPoint, Point endPoint) {
        return canMove(startPoint, endPoint);
    }
}
