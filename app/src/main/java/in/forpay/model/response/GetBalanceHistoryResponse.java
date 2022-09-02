package in.forpay.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetBalanceHistoryResponse {

    @SerializedName("data")
    private ArrayList<Data> dataList = new ArrayList<>();
    private String resCode = "";
    private String resText = "";
    private String balance = "";

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

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
        private String openingBal = "";
        private String closingBal = "";
        private String type = "";
        private String remark = "";
        private String amount = "";
        @SerializedName("orderid")
        private String orderId = "";
        private String username = "";
        private String status = "";
        @SerializedName("optid")
        private String optId = "";
        private String mobile = "";
        @SerializedName("ramount")
        private String rAmount = "";
        @SerializedName("creditused")
        private String creditUsed = "";
        private String service = "";
        private String bankAccount = "";
        private String bankName = "";
        private String marginAmount = "";
        private String operatorId = "";
        private String operatorIcon = "";

        public String getOperatorIcon() {
            return operatorIcon;
        }

        public void setOperatorIcon(String operatorIcon) {
            this.operatorIcon = operatorIcon;
        }

        public String getDate() {
            return date;
        }

        public String getOpeningBal() {
            return openingBal;
        }

        public String getClosingBal() {
            return closingBal;
        }

        public String getType() {
            return type;
        }

        public String getRemark() {
            return remark;
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

        public String getOptId() {
            return optId;
        }

        public String getMobile() {
            return mobile;
        }

        public String getrAmount() {
            return rAmount;
        }

        public String getCreditUsed() {
            return creditUsed;
        }

        public String getService() {
            return service;
        }

        public String getBankAccount() {
            return bankAccount;
        }

        public String getBankName() {
            return bankName;
        }

        public String getMarginAmount() {
            return marginAmount;
        }

        public String getOperatorId() {
            return operatorId;
        }
    }
}
