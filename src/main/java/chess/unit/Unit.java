package chess.unit;

import chess.misc.Direction;
import chess.base.Team;
import xml.XML;

import java.io.Serializable;

/**
 * Abstract class for chess units
 */
@XML
public abstract class Unit implements Serializable {
    @XML
    Direction[] directions;
    @XML
    private final Team team;

    public Unit(Direction[] directions, Team team) {
        this.directions = directions;
        this.team = team;
    }

    /**
     * Defines if this unit is enemy for other
     * @param unit other unit
     * @return true if other unit is enemy for this unit
     */
    public boolean isEnemy(Unit unit) {
        return !this.getTeam().equals(unit.getTeam());
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isEnemy(Team team) {
        return !this.getTeam().equals(team);
    }

    /**
     * Get {@link Direction} array for this unit
     * @return {@link Direction} array
     */
    public Direction[] getDirections() {
        return this.directions;
    }

    /**
     * Indicates that this unit should not be under attack by enemy
     *
     * @return true for such a unit
     */
    public boolean isImportant() {return false;}

    public Team getTeam() {
        return team;
    }
}
