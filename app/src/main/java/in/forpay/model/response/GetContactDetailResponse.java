package in.forpay.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class GetContactDetailResponse {

    @SerializedName("data")
    private ArrayList<Data> dataList;
    private String resCode = "";
    private String resText,email = "";
    private String gotoChat="";

    public ArrayList<Data> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<Data> dataList) {
        this.dataList = dataList;
    }

    public void setGotoChat(String gotoChat) {
        this.gotoChat = gotoChat;
    }

    public String getGotoChat() {
        return gotoChat;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public class Data {
        private String mobile = "";
        private String name = "";
        private String language = "";
        private String department,status = "";

        public String getStatus() {
            return status;
        }

        public String getLanguage() {
            return language;
        }

        public String getDepartment() {
            return department;
        }

        public String getMobile() {
            return mobile;
        }

        public String getName() {
            return name;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
