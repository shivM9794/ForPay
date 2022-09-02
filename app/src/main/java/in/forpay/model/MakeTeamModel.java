package in.forpay.model;

import java.util.ArrayList;

public class MakeTeamModel {
    public String os;
    public String latitude;
    public String longitude;
    public String activityName;
    public String imei;
    public String versionCode;
    public String token;
    public ArrayList<Player> players;
    public String groupId;

    public void setOs(String os) {
        this.os = os;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}

