package chess.base;

import xml.XML;

import java.io.Serializable;
import java.util.Objects;

@XML(isStrict=true)
public class Team implements Serializable {
    public static final Team INVALID_TEAM = new Team("__invalid__");

    @XML
    private final String teamTag;

    public Team(String TeamTag) {
        this.teamTag = TeamTag;
    }

    public Team() {
        this.teamTag = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team)) return false;
        Team team = (Team) o;
        return Objects.equals(teamTag, team.teamTag);
    }

    public String getTeamTag() {
        return teamTag;
    }
}
