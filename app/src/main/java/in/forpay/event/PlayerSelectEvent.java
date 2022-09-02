package in.forpay.event;

import in.forpay.model.response.TeamModel;

public class PlayerSelectEvent {
    private TeamModel teamModel;
    private int team;
    private String id;
    private boolean isRemove;
    private boolean isCountUpdate;

    public PlayerSelectEvent(TeamModel teamModel, int team, String id, boolean isRemove, boolean isCountUpdate) {
        this.teamModel = teamModel;
        this.team = team;
        this.id = id;
        this.isRemove = isRemove;
        this.isCountUpdate = isCountUpdate;
    }

    public boolean isCountUpdate() {
        return isCountUpdate;
    }

    public boolean isRemove() {
        return isRemove;
    }

    public String getId() {
        return id;
    }

    public TeamModel getTeamModel() {
        return teamModel;
    }

    public int getTeam() {
        return team;
    }
}
