package in.forpay.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetLoginHistoryResponse {
    private String resText="";
    private String resCode="";

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public void setResText(String resText) {
        this.resText = resText;
    }

    public String getResCode() {
        return resCode;
    }

    public String getResText() {
        return resText;
    }

    @SerializedName("deviceList")

    private ArrayList<DeviceList> deviceList = new ArrayList<>();

    public void setDeviceList(ArrayList<DeviceList> deviceList) {
        this.deviceList = deviceList;
    }

    public ArrayList<DeviceList> getDeviceList() {
        return deviceList;
    }

    public static class DeviceList{

        public DeviceList(){}
        private String activeTime="";
        private String imei="";
        private String loginTime="";

        public void setLoginTime(String loginTime) {
            this.loginTime = loginTime;
        }

        public String getLoginTime() {
            return loginTime;
        }

        public void setActiveTime(String activeTime) {
            this.activeTime = activeTime;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getActiveTime() {
            return activeTime;
        }

        public String getImei() {
            return imei;
        }
    }
}
