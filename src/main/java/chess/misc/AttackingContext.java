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

// --Commented out by Inspection START (10/7/2019 4:09 PM):
//    public AttackingContext setBarrage(Unit unit) {
//        if (unit == null) {
//            return this;
//        }
//        this.getBarrages().add(unit);
//        return this;
//    }
// --Commented out by Inspection STOP (10/7/2019 4:09 PM)

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
}
