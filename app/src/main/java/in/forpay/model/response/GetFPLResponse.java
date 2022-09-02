package in.forpay.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetFPLResponse {

    @SerializedName("data")
    private ArrayList<Data> data = new ArrayList<>();
    @SerializedName("bonus")
    private ArrayList<Bonus> bonus = new ArrayList<>();
    @SerializedName("rules")
    private ArrayList<Rules> rules = new ArrayList<>();
    private String resCode = "";
    private String resText = "";

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public ArrayList<Bonus> getBonus() {
        return bonus;
    }

    public void setBonus(ArrayList<Bonus> bonus) {
        this.bonus = bonus;
    }

    public ArrayList<Rules> getRules() {
        return rules;
    }

    public void setRules(ArrayList<Rules> rules) {
        this.rules = rules;
    }

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

    public class Data {

        private String matchId;
        private String team1;
        private String team2;
        private String matchTime;
        private String matchDate;
        private String canJoin;
        private String remark;
        private String alreadyJoined;
        private String matchTitle;
        @SerializedName("publicGroups")
        private ArrayList<GetPublicGroupResponse.Data> publicGdata = new ArrayList<>();

        public ArrayList<GetPublicGroupResponse.Data> getPublicGdata() {
            return publicGdata;
        }

        public void setPublicGdata(ArrayList<GetPublicGroupResponse.Data> publicGdata) {
            this.publicGdata = publicGdata;
        }

        public String getMatchDate() {
            return matchDate;
        }

        public void setMatchDate(String matchDate) {
            this.matchDate = matchDate;
        }

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

        public String getCanJoin() {
            return canJoin;
        }

        public void setCanJoin(String canJoin) {
            this.canJoin = canJoin;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getAlreadyJoined() {
            return alreadyJoined;
        }

        public void setAlreadyJoined(String alreadyJoined) {
            this.alreadyJoined = alreadyJoined;
        }

        public String getMatchTitle() {
            return matchTitle;
        }

        public void setMatchTitle(String matchTitle) {
            this.matchTitle = matchTitle;
        }
    }

    public class Bonus {

        private String name;
        private String Incentive;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIncentive() {
            return Incentive;
        }

        public void setIncentive(String incentive) {
            Incentive = incentive;
        }
    }

    public class Rules {

        private String rule;

        public String getRule() {
            return rule;
        }

        public void setRule(String rule) {
            this.rule = rule;
        }
    }


}
