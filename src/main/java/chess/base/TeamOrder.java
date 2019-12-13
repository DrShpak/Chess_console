package chess.base;

import xml.XML;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@XML(isStrict=true)
public class TeamOrder implements Serializable {
    @XML
    private final ArrayList<Team> teams = new ArrayList<>();
    @XML
    private int index = -1;

    @SuppressWarnings("unused")
    public TeamOrder() {

    }

    public TeamOrder(Team... teams) {
        this.teams.addAll(Arrays.stream(teams).distinct().collect(Collectors.toList()));
        if (this.teams.size() > 0) {
            this.index = 0;
        }
    }

    public Team get() {
        return this.index > -1 ? this.teams.get(this.index) : null;
    }

    public void next() {
        this.index = this.index + 1 < this.teams.size() ? this.index + 1 : 0;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        TeamOrder teamOrder = (TeamOrder) object;
        return index == teamOrder.index &&
                Objects.equals(teams, teamOrder.teams);
    }
}
