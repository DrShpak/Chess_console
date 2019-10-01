package chess.misc;

import chess.units.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class AttackingIncident {
    private Unit attacker;
    private Cell holder;
    private AttackingIncident identity;
    private List<Unit> barrages = new ArrayList<>();
    private AttackingIncident next;

    public AttackingIncident(Unit attacker, Cell holder) {
        this.attacker = attacker;
        this.holder = holder;
        identity = this;
    }

    public void unpinIncident() {
        identity.iterateIncidents().forEach(x -> x.holder.getIncidents().remove(x));
    }

    public AttackingIncident(AttackingIncident previous, Cell holder) {
        this(previous.attacker, holder);
        identity = previous.identity;
        barrages.addAll(previous.barrages);
        previous.next = this;

        holder.getIncidents().add(this);
    }

    Unit getAttacker() {
        return attacker;
    }

    public boolean isEquivalent(AttackingIncident other) {
        return identity.equals(other.identity);
    }

    public boolean isInvolved(Unit other) {
        return iterateIncidents().anyMatch(x -> x.barrages.contains(other));
    }

    public Stream<AttackingIncident> iterateIncidents() {
        return Stream.iterate(this, Objects::nonNull, AttackingIncident::getNext);
    }

    public List<Unit> getBarrages() {
        return barrages;
    }

    private AttackingIncident getNext() {
        return next;
    }
}
