package in.forpay.model.response;

import java.util.HashMap;
import java.util.List;

public class GetMatchDetailResponse {
    public HashMap<String, List<TeamModel>> teamPlayers;
    public String totalPoint;
    public String rank;
    public String isJoined;
    public String resCode;
    public String title;
    public String resText;
    public String onlinePlayer;
    public String contestClosed;
    public String team1Name;
    public String team2Name;
    public String referUrl;
    public String showMatchingLayout="";

    public String getShowMatchingLayout() {
        return showMatchingLayout;
    }

    public String getReferUrl() {
        return referUrl;
    }

    public String getTeam1Name() {
        return team1Name;
    }

    public String getTeam2Name() {
        return team2Name;
    }


    public String getContestClosed() {
        return contestClosed;
    }

    public void setContestClosed(String contestClosed) {
        this.contestClosed = contestClosed;
    }

    public String getOnlinePlayer() {
        return onlinePlayer;
    }

    public void setOnlinePlayer(String onlinePlayer) {
        this.onlinePlayer = onlinePlayer;
    }

    public String getJoiningPoint() {
        return joiningPoint;
    }

    public String joiningPoint;

    public HashMap<String, List<TeamModel>> getTeamPlayers() {
        return teamPlayers;
    }

    public String getTotalPoint() {
        return totalPoint;
    }

    public String getRank() {
        return rank;
    }

    public String getIsJoined() {
        return isJoined;
    }

    public String getResCode() {
        return resCode;
    }

    public String getTitle() {
        return title;
    }

    public String getResText() {
        return resText;
    }
}






