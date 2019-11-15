package chess.misc;

import xml.XML;

@XML
public enum MovePolicy {
    WALK,
    BOTH,
    ATTACK;

    public boolean allowAttack() {
        return this.compareTo(MovePolicy.BOTH) >= 0;
    }

    public boolean allowWalk() {
        return this.compareTo(MovePolicy.BOTH) <= 0;
    }
}
