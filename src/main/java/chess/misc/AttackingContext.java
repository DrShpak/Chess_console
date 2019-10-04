package chess.misc;

import chess.units.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class AttackingContext {
    private Unit attacker;
    private AttackingContext identity;
    private List<Unit> barrages = new ArrayList<>();
    private AttackingContext next;

    public AttackingContext(Unit attacker) {
        this.attacker = attacker;
        identity = this;
    }

    public AttackingContext(AttackingContext previous) {
        this(previous.attacker);
        identity = previous.identity;
        barrages.addAll(previous.barrages);
        previous.next = this;
    }

    Unit getAttacker() {
        return attacker;
    }

    public boolean isEquivalent(AttackingContext other) {
        return identity.equals(other.identity);
    }

    public boolean isInvolved(Unit other) {
        return iterateContexts().anyMatch(x -> x.barrages.contains(other));
    }

    public boolean isInferior(AttackingContext other) { return iterateContexts().anyMatch(x -> x == other); }

    public Stream<AttackingContext> iterateContexts() {
        return Stream.iterate(this, Objects::nonNull, AttackingContext::getNext);
    }

    public List<Unit> getBarrages() {
        return barrages;
    }

    private AttackingContext getNext() {
        return next;
    }
}
