package in.forpay.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetStockHistoryResponse {

    @SerializedName("data")
    private ArrayList<Data> dataList = new ArrayList<>();
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
        private String date = "";

        private String type = "";

        private String amount = "";
        private String paidAmount="";
        private String toUser="";
        private String fromUser="";

        private String orderId = "";
        private String username = "";
        private String status = "";


        public String getDate() {
            return date;
        }

        public String getPaidAmount(){return paidAmount;}

        public String getToUser(){ return toUser;}

        public String getFromUser(){return fromUser;}



        public String getType() {
            return type;
        }



        public String getAmount() {
            return amount;
        }

        public String getOrderId() {
            return orderId;
        }

        public String getUsername() {
            return username;
        }

        public String getStatus() {
            return status;
        }



    }
}
