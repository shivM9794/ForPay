package in.forpay.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetViewRetailerResponse {

    @SerializedName("data")
    private ArrayList<GetViewRetailerResponse.Data> dataList;
    private String resCode = "";
    private String resText = "";

    public ArrayList<Data> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<Data> dataList) {
        this.dataList = dataList;
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
        private String userName = "";
        private String name = "";
        private String balance = "";
        private String packageName = "";
        private String status = "";

        public String getUserName() {
            return userName;
        }

        public String getName() {
            return name;
        }

        public String getBalance() {
            return balance;
        }

        public String getPackageName() {
            return packageName;
        }

        public String getStatus() {
            return status;
        }


    }

}
