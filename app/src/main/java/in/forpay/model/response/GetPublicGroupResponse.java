package in.forpay.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetPublicGroupResponse {

    @SerializedName("publicGroups")
    private ArrayList<Data> data = new ArrayList<>();
    private String resCode = "";
    private String resText = "";

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
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

    public class Data{

        private String groupName;
        private String groupId;
        private String joiningPoints;
        private String players;

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getJoiningPoints() {
            return joiningPoints;
        }

        public void setJoiningPoints(String joiningPoints) {
            this.joiningPoints = joiningPoints;
        }

        public String getPlayers() {
            return players;
        }

        public void setPlayers(String players) {
            this.players = players;
        }
    }




}
