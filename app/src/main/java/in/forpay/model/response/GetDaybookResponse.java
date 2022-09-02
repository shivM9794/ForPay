package in.forpay.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetDaybookResponse {
    @SerializedName("data")
    private ArrayList<Data> dataList;
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



    public static class Data{
        private String operatorId,success,pending,disputed,creditUsed,cusCreditUsed,date,profit;
        public Data(){

        }

        public String getOperatorId() {
            return operatorId;
        }

        public void setOperatorId(String operatorId) {
            this.operatorId = operatorId;
        }

        public String getCreditUsed() {
            return creditUsed;
        }

        public void setCreditUsed(String creditUsed) {
            this.creditUsed = creditUsed;
        }

        public String getCusCreditUsed() {
            return cusCreditUsed;
        }

        public void setCusCreditUsed(String cusCreditUsed) {
            this.cusCreditUsed = cusCreditUsed;
        }

        public String getDisputed() {
            return disputed;
        }

        public void setDisputed(String disputed) {
            this.disputed = disputed;
        }

        public String getPending() {
            return pending;
        }

        public void setPending(String pending) {
            this.pending = pending;
        }

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getProfit() {
            return profit;
        }

        public void setProfit(String profit) {
            this.profit = profit;
        }
    }

}
