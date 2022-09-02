package in.forpay.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetServiceListResponse {

    private String resText="";
    private String resCode="";

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
    @SerializedName("service")

    private ArrayList<Service> serviceList = new ArrayList<>();

    public void setServiceList(ArrayList<Service> serviceList) {
        this.serviceList = serviceList;
    }

    public ArrayList<Service> getServiceList() {
        return serviceList;
    }

    public static class Service {
        private String id = "";
        private String service = "";
        private String type = "";
        private String smsCode = "";

        public Service() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSmsCode() {
            return smsCode;
        }

        public void setSmsCode(String smsCode) {
            this.smsCode = smsCode;
        }
    }
}
