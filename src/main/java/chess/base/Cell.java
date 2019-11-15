package chess.base;

import chess.misc.MovePolicy;
import chess.misc.Point;
import chess.misc.StreamUtils;
import chess.unit.Unit;
import org.javatuples.Pair;
import xml.XML;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@XML
public class Cell {
    @XML
    private Unit unit;
    private final List<AttackingContext> contexts = new ArrayList<>();
    private final List<Pair<Cell, AttackingContext>> homeContexts = new ArrayList<>();

    public Cell(Unit unit) {
        this.unit = unit;
    }

    public int countAttackers(Unit unit) {
        return (int)
                contexts.stream().
                filter(x -> unit.isEnemy(x.getAttacker()) && x.getBarrages().size() < 1).
                count();
    }

    public AttackingContext getAttackerContext(Unit unit) {
        return contexts.stream().
                        filter(x -> unit.isEnemy(x.getAttacker()) && x.getBarrages().size() < 1).
                        findAny().
                        orElseThrow();
    }

    public Stream<AttackingContext> getPotentialAttackers(Unit unitFor) {
        return contexts.stream().
                filter(x -> unitFor.isEnemy(x.getAttacker()) && x.getBarrages().size() > 0);
    }

    public static void moveUnit(Cell from, Cell to) {
        to.unit = from.unit;
        from.unit = null;
    }

    public void replace(Unit unit) {
        this.unit = unit;
    }

    public void addBarrageToContexts() {
        this.contexts.
                stream().
                map(AttackingContext::iterateContexts).
                flatMap(y -> y).
                forEach(y -> y.getBarrages().add(this.unit));
    }

    public void removeBarrageFromContexts() {
        this.contexts.
                stream().
                map(AttackingContext::iterateContexts).
                flatMap(x -> x).
                forEach(y -> y.getBarrages().remove(this.unit));
    }

    public void emitContexts(Point to, Function<Point, Cell> cellGetter) {
        this.pinContexts(Arrays.stream(this.unit.
                getDirections()).
                filter(direction -> direction.getMovePolicy().compareTo(MovePolicy.BOTH) >= 0).
                map(direction ->
                        StreamUtils.mapEx(
                                (Pair<Cell, AttackingContext>) null,
                                direction.getPointsAlong(to),
                                (pair, point) -> new Pair<>(cellGetter.apply(point), pair != null ?
                                        new AttackingContext(pair.getValue1()) :
                                        new AttackingContext(this.unit)
                                )
                        )
                ).
                flatMap(x -> x));
    }

    public Unit getUnit() {
        return unit;
    }

    public boolean isEmpty() { return unit == null;}

    public boolean hasEnemyUnit(Unit friendlyUnit) { return unit != null && unit.isEnemy(friendlyUnit); }

    private void pinContexts(Stream<Pair<Cell, AttackingContext>> contextStream) {
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

    public Optional<AttackingContext> getEquivalentContext(AttackingContext otherContext, Unit importantUnit) {
        return this.contexts.
                stream().
                filter(x -> x.isEquivalent(otherContext)).
                filter(x -> !x.getBarrages().contains(importantUnit)).
                findAny();
    }

    public boolean hasInferiorContext(AttackingContext otherContext) {
        return this.contexts.
                stream().
                anyMatch(otherContext::isInferior);
    }
}
