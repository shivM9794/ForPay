package in.forpay.model;

public class LoginHistoryModel {
    private String activeTime="";
    private String imei="";
    private String loginTime="";

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public void setActiveTime(String activeTime) {
        this.activeTime = activeTime;
    }

    public String getImei() {
        return imei;
    }

    public String getActiveTime() {
        return activeTime;
    }
}
