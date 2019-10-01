package chess.units;

import chess.misc.Direction;
import chess.misc.Point;
import chess.misc.Team;

public class Queen extends Unit {
    public Queen(Team team) {
        super(new Direction[] {
                new Direction(1, 0),
                new Direction(-1, 0),
                new Direction(0, 1),
                new Direction(0, -1),
                new Direction(0, -1),
                new Direction(1, -1),
                new Direction(-1, 1),
                new Direction(1, -1),
                new Direction(1, -1)
        });
        this.team = team;
    }

    @Override
    public boolean canMove(Point startPoint, Point endPoint) {
        var diff = Point.diff(endPoint, startPoint);
        return Math.abs(diff.getY()) == Math.abs(diff.getX())
                || diff.getX() == 0 || diff.getY() == 0;
    }

    @Override
    public boolean canAttack(Point startPoint, Point endPoint) {
        return canMove(startPoint, endPoint);
    }
}
