package in.forpay.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetFPLMyContestResponse {


    /**
     * data : [{"matchId":"","team1":"","team2":"","matchTime":"<b>19:30:00<\/b>","remark":"Match Contest Running","matchTitle":"<b>Lucknow Super Giants VS Mumbai Indians<\/b>","matchDate":"<b>2022-04-24<\/b>","groupName":"Rajdeep Kumar","rank":"0","pointsEarned":"","investedPoint":"1","groupId":"743181","players":"1"}]
     * resCode : 200
     * resText : SUCCESS
     */

    @SerializedName("resCode")
    private String resCode;
    @SerializedName("resText")
    private String resText;
    /**
     * matchId :
     * team1 :
     * team2 :
     * matchTime : <b>19:30:00</b>
     * remark : Match Contest Running
     * matchTitle : <b>Lucknow Super Giants VS Mumbai Indians</b>
     * matchDate : <b>2022-04-24</b>
     * groupName : Rajdeep Kumar
     * rank : 0
     * pointsEarned :
     * investedPoint : 1
     * groupId : 743181
     * players : 1
     */

    @SerializedName("data")
    private List<DataBean> data;

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResText() {
        return resText;
    }

    public void setResText(String resText) {
        this.resText = resText;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        @SerializedName("matchId")
        private String matchId;
        @SerializedName("team1")
        private String team1;
        @SerializedName("team2")
        private String team2;
        @SerializedName("matchTime")
        private String matchTime;
        @SerializedName("remark")
        private String remark;
        @SerializedName("matchTitle")
        private String matchTitle;
        @SerializedName("matchDate")
        private String matchDate;
        @SerializedName("groupName")
        private String groupName;
        @SerializedName("rank")
        private String rank;
        @SerializedName("pointsEarned")
        private String pointsEarned;
        @SerializedName("investedPoint")
        private String investedPoint;
        @SerializedName("groupId")
        private String groupId;
        @SerializedName("players")
        private String players;

        public String getMatchId() {
            return matchId;
        }

        public void setMatchId(String matchId) {
            this.matchId = matchId;
        }

        public String getTeam1() {
            return team1;
        }

        public void setTeam1(String team1) {
            this.team1 = team1;
        }

        public String getTeam2() {
            return team2;
        }

        public void setTeam2(String team2) {
            this.team2 = team2;
        }

        public String getMatchTime() {
            return matchTime;
        }

        public void setMatchTime(String matchTime) {
            this.matchTime = matchTime;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getMatchTitle() {
            return matchTitle;
        }

        public void setMatchTitle(String matchTitle) {
            this.matchTitle = matchTitle;
        }

        public String getMatchDate() {
            return matchDate;
        }

        public void setMatchDate(String matchDate) {
            this.matchDate = matchDate;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getPointsEarned() {
            return pointsEarned;
        }

        public void setPointsEarned(String pointsEarned) {
            this.pointsEarned = pointsEarned;
        }

        public String getInvestedPoint() {
            return investedPoint;
        }

        public void setInvestedPoint(String investedPoint) {
            this.investedPoint = investedPoint;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getPlayers() {
            return players;
        }

        public void setPlayers(String players) {
            this.players = players;
        }
    }
}
