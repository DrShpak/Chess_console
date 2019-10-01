package chess.misc;

import chess.units.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Cell {
    private Unit holding;
    private List<AttackingIncident> incidents = new ArrayList<>();

    public Cell(Unit holding) {
        this.holding = holding;
    }

    public int onFire(Unit unitFor) {
        return (int) incidents.stream().
                filter(x -> unitFor.isEnemy(x.getAttacker()) && x.getBarrages().size() < 1).
                count();
    }

    public static void moveUnit(Cell from, Cell to) {
        to.holding = from.holding;
        from.holding = null;
    }

    public Unit getHolding() {
        return holding;
    }

    public List<AttackingIncident> getIncidents() {
        return incidents;
    }

    public List<AttackingIncident> getOwnIncidents() {
        return incidents.stream().
                filter(x -> holding == x.getAttacker()).
                collect(Collectors.toList());
    }
}
