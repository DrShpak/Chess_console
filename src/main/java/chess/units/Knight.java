package chess.units;

import chess.misc.Direction;
import chess.misc.Point;
import chess.misc.Team;

public class Knight extends Unit {
    public Knight(Team team) {
        super(new Direction[] {
                new Direction(1, 2, 1),
                new Direction(-1, 2, 1),
                new Direction(1, -2, 1),
                new Direction(-1, -2, 1),
                new Direction(2, 1, 1),
                new Direction(-2, 1, 1),
                new Direction(2, -1, 1),
                new Direction(-2, -1, 1)
        });
        this.team = team;
    }

    @Override
    public boolean canMove(Point startPoint, Point endPoint) {
        var diff = Point.diff(endPoint, startPoint);
        return
                  diff.equals(new Point(2, 1)) ||
                  diff.equals(new Point(1, 2)) ||
                 diff.equals(new Point(-2, 1)) ||
                 diff.equals(new Point(1, -2)) ||
                 diff.equals(new Point(-1, 2)) ||
                 diff.equals(new Point(2, -1)) ||
                diff.equals(new Point(-2, -1)) ||
                diff.equals(new Point(-1, -2));
    }

    @Override
    public boolean canAttack(Point startPoint, Point endPoint) {
        return false;
    }
}
