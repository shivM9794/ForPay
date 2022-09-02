package in.forpay.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TeamModel {
    public String name;
    public String id;
    public String profileUrl;
    public String team;
    public String point;
    public String playerRole;
    public String run;
    public String wicket;
    public String selectedRole;
    public String selectedRoleKey;
    private boolean isSelected;

    public String getSelectedRoleKey() {
        return selectedRoleKey;
    }

    public void setSelectedRoleKey(String selectedRoleKey) {
        this.selectedRoleKey = selectedRoleKey;
    }

    public String getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(String selectedRole) {
        this.selectedRole = selectedRole;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setSelectionOption(List<SelectionOption> selectionOption) {
        this.selectionOption = selectionOption;
    }

    public List<SelectionOption> selectionOption;

    public List<SelectionOption> getSelectionOption() {
        return selectionOption;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getPlayerRole() {
        return playerRole;
    }

    public void setPlayerRole(String playerRole) {
        this.playerRole = playerRole;
    }

    public String getRun() {
        return run;
    }

    public void setRun(String run) {
        this.run = run;
    }

    public String getWicket() {
        return wicket;
    }

    public void setWicket(String wicket) {
        this.wicket = wicket;
    }

    public static class SelectionOption {
        public String key;
        @SerializedName("title")
        public String zero;

        public SelectionOption(String key, String zero) {
            this.key = key;
            this.zero = zero;
        }

        public String getKey() {
            return key;
        }

        public String getTitle() {
            return zero;
        }
    }
}


