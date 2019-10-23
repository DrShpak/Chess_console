package chess.misc;

import chess.units.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class AttackingContext {
    private final Unit attacker;
    private final AttackingContext identity;
    private final List<Unit> barrages = new ArrayList<>();
    private AttackingContext next;

    AttackingContext(Unit attacker) {
        this.attacker = attacker;
        identity = this;
    }

    AttackingContext(AttackingContext previous) {
        this.attacker = previous.attacker;
        identity = previous.identity;
        this.barrages.addAll(previous.barrages);
        previous.next = this;
    }

    public AttackingContext setBarrage(Unit unit) {
        if (unit == null) {
            return this;
        }
        this.getBarrages().add(unit);
        return this;
    }

    Unit getAttacker() {
        return attacker;
    }

    public boolean isEquivalent(AttackingContext other) {
        return identity == other.identity;
    }

    public boolean isInvolved(Unit other) {
        return iterateContexts().anyMatch(x -> x.barrages.contains(other));
    }

    public boolean isInferior(AttackingContext other) {
        return iterateContexts().anyMatch(x -> x == other);
    }

    Stream<AttackingContext> iterateContexts() {
        return Stream.iterate(this, Objects::nonNull, AttackingContext::getNext);
    }

    public List<Unit> getBarrages() {
        return barrages;
    }

    private AttackingContext getNext() {
        return next;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttackingContext)) return false;
        AttackingContext that = (AttackingContext) o;
        return Objects.equals(attacker, that.attacker) &&
                Objects.equals(identity, that.identity) &&
                Objects.equals(barrages, that.barrages) &&
                Objects.equals(next, that.next);
    }
}
