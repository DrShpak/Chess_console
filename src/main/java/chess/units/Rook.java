package chess.units;

import chess.misc.Direction;
import chess.misc.Point;
import chess.misc.Team;

public class Rook extends Unit {
    public Rook(Team team) {
        super(new Direction[] {
                new Direction(1, 0),
                new Direction(-1, 0),
                new Direction(0, 1),
                new Direction(0, -1)
        });
        this.team = team;
    }

    @Override
    public boolean canMove(Point startPoint, Point endPoint) {
        var diff = Point.diff(endPoint, startPoint);
        return diff.getX() == 0 || diff.getY() == 0;
    }

    @Override
    public boolean canAttack(Point startPoint, Point endPoint) {
        return canMove(startPoint, endPoint);
    }
}