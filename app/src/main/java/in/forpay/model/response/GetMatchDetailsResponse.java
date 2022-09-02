package in.forpay.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetMatchDetailsResponse {

    @SerializedName("teamPlayers")
    private ArrayList<Data> teamPlayers = new ArrayList<>();
    private String resText="";
    private String resCode="";

    public ArrayList<Data> getTeamPlayers() {
        return teamPlayers;
    }

    public void setTeamPlayers(ArrayList<Data> teamPlayers) {
        this.teamPlayers = teamPlayers;
    }

    public String getResText() {
        return resText;
    }

    public void setResText(String resText) {
        this.resText = resText;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public class Data{

        private String name;
        private String id;
        private String profileUrl;
        private String team;
        private String point;
        private String playerRole;
        private String run;
        private String wicket;

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
    }
}
