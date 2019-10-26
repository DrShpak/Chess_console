package chess.base;

public class Team {
    public static final Team INVALID_TEAM = new Team("__invalid__");

    private final String teamTag;

    public Team(String TeamTag) {
        this.teamTag = TeamTag;
    }

    public String getTeamTag() {
        return teamTag;
    }
}
