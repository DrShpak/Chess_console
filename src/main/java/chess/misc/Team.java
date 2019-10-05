package chess.misc;

public class Team {
    private final String teamTag;

    public Team(String TeamTag) {
        this.teamTag = TeamTag;
    }
    public String getTeamTag() {
        return teamTag;
    }
    public boolean isEnemy(Team other) {return this != other;}
}
