package chess.misc;

import chess.units.Unit;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Cell {
    private Unit holding;
    private List<AttackingContext> contexts = new ArrayList<>();
    private List<Pair<Cell, AttackingContext>> homeContexts = new ArrayList<>();

    public Cell(Unit holding) {
        this.holding = holding;
    }

    public int onFire(Team myTeam) {
        return (int)
                contexts.stream().
                filter(x -> myTeam.isEnemy(x.getAttacker().team) && x.getBarrages().size() < 1).
                count();
    }

    public AttackingContext getEnemy(Team myTeam) {
        //noinspection OptionalGetWithoutIsPresent todo !
        return contexts.stream().
                        filter(x -> myTeam.isEnemy(x.getAttacker().team) && x.getBarrages().size() < 1).
                        findAny().get();
    }

    public Stream<AttackingContext> getSleepingEnemy(Unit unitFor) {
        //todo barrages to NEXT context
        return contexts.stream().
                filter(x -> unitFor.isEnemy(x.getAttacker()) && x.getBarrages().size() > 0);
    }

    public static void moveUnit(Cell from, Cell to) {
        to.holding = from.holding;
        from.holding = null;
    }

    public Unit getHolding() {
        return holding;
    }

    public List<AttackingContext> getContexts() {
        return contexts;
    }

    public void pinContexts(Stream<Pair<Cell, AttackingContext>> contextStream) {
        if (!this.homeContexts.isEmpty()) {
            throw new IllegalStateException("home is busy!");
        }
        contextStream.sequential().collect(Collectors.toCollection(() -> this.homeContexts));
        homeContexts.forEach(x -> x.getValue0().contexts.add(x.getValue1()));
    }

    public void unpinContexts() {
        homeContexts.forEach(x -> x.getValue0().contexts.remove(x.getValue1()));
        homeContexts.clear();
    }
}
