package chess.base;

import chess.unit.Unit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class AttackingContext implements Serializable {
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

    Unit getAttacker() {
        return attacker;
    }

    boolean isEquivalent(AttackingContext other) {
        return identity == other.identity;
    }

    public boolean isInvolved(Unit other) {
        return iterateContexts().anyMatch(x -> x.barrages.contains(other));
    }

    boolean isInferior(AttackingContext other) {
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
}
