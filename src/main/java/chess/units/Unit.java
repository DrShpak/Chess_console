package chess.units;

import chess.misc.Direction;
import chess.misc.Point;
import chess.misc.Team;

public abstract class Unit {
    protected Direction[] directions;
    public Team team;

    public Unit(Direction[] directions) {
        this.directions = directions;
    }

    public boolean isEnemy(Unit unit2) {
        return !this.team.equals(unit2.team);
    }

    public Direction[] getDirections() {
        return this.directions;
    };

    public abstract boolean canMove(Point startPoint, Point endPoint);
    public abstract boolean canAttack(Point startPoint, Point endPoint);

    public boolean isFortified() {return false;}
}
