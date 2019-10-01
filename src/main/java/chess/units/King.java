package chess.units;

import chess.misc.Direction;
import chess.misc.Point;
import chess.misc.Team;

import java.util.Arrays;

public class King extends Unit {
    public King(Team team) {
        super(new Direction[] {
                new Direction(1, 0, 1),
                new Direction(-1, 0, 1),
                new Direction(0, 1, 1),
                new Direction(0, -1, 1),
                new Direction(0, -1, 1),
                new Direction(1, -1, 1),
                new Direction(-1, 1, 1),
                new Direction(1, -1, 1),
                new Direction(1, -1, 1)
        });
        this.team = team;
    }

    @Override
    public boolean canMove(Point startPoint, Point endPoint) {
        var diff = Point.diff(endPoint, startPoint);
        return diff.getY() <=1 && diff.getX() <= 1;
    }

    @Override
    public boolean canAttack(Point startPoint, Point endPoint) {
        return canMove(startPoint, endPoint);
    }

    @Override
    public boolean isFortified() {
        return true;
    }
}