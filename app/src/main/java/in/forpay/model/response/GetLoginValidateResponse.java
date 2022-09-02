package in.forpay.model.response;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetLoginValidateResponse {


    private String status = "";
    private String resCode = "";
    private String resText = "";
    private Data data;
    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResCode() {
        return resCode;
    }

    public String getResText() {
        return resText;
    }

    public void setResText(String resText) {
        this.resText = resText;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private String mobile = "";
        private String token = "";
        private String distributorName="";

        private String name = "";
        private String bal = "";

        public String getBal() {
            return bal;
        }

        public void setBal(String bal) {
            this.bal = bal;
        }

        public String getDistributorName() {
            return distributorName;
        }

        public void setDistributorName(String distributorName) {
            this.distributorName = distributorName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }




        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        @SerializedName("circle")
        private ArrayList<GetLoginValidateResponse.Circle> circleList = new ArrayList<>();
        @SerializedName("service")
        private ArrayList<GetLoginValidateResponse.Service> serviceList = new ArrayList<>();

        public ArrayList<Circle> getCircleList() {
            return circleList;
        }

        public void setCircleList(ArrayList<Circle> circleList) {
            this.circleList = circleList;
        }


        public ArrayList<Service> getServiceList() {
            return serviceList;
        }

        public void setServiceList(ArrayList<Service> serviceList) {
            this.serviceList = serviceList;
        }

    }


    public class Circle {
        private String id = "";
        private String circle = "";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCircle() {
            return circle;
        }

        public void setCircle(String circle) {
            this.circle = circle;
        }
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
