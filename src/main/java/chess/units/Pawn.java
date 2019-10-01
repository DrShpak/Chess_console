package chess.units;

import chess.misc.Direction;
import chess.misc.MovePolicy;
import chess.misc.Point;
import chess.misc.Team;

public class Pawn extends Unit {
    public Pawn(Team team, Integer multiplier) {
        super(new Direction[] {
                new Direction((int)Math.signum(multiplier), 0, 1, MovePolicy.PEACE),
                //new Direction(2, 0, 1, MovePolicy.PEACE),
                new Direction((int)Math.signum(multiplier), 1, 1, MovePolicy.ATTACK),
                new Direction((int)Math.signum(multiplier), -1, 1, MovePolicy.ATTACK),
        });
        this.team = team;
    }

    @Override
    public boolean canMove(Point startPoint, Point endPoint) {
        var diff = Point.diff(endPoint, startPoint);
        return diff.getY() == 0 && diff.getX() == (team.getTeamTag().equals("White") ? 1 : -1);
    }

    @Override
    public boolean canAttack(Point startPoint, Point endPoint) {
        var diff = Point.diff(endPoint, startPoint);
        return Math.abs(diff.getY()) == 1 && diff.getX() == (team.getTeamTag().equals("White") ? 1 : -1);
    }
}
