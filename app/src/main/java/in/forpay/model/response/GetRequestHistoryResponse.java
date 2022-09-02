package in.forpay.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetRequestHistoryResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        @SerializedName("fundReqDet")
        private ArrayList<FundData> fundList = new ArrayList<>();
        @SerializedName("banks")
        private ArrayList<Bank> bankList = new ArrayList<>();
        private String resCode = "";
        private String resText = "";

        public ArrayList<FundData> getFundList() {
            return fundList;
        }

        public void setFundList(ArrayList<FundData> fundList) {
            this.fundList = fundList;
        }

        public ArrayList<Bank> getBankList() {
            return bankList;
        }

        public void setBankList(ArrayList<Bank> bankList) {
            this.bankList = bankList;
        }

        public String getResCode() {
            return resCode;
        }

        public String getResText() {
            return resText;
        }
    }

    public class FundData {
        private String id = "";
        @SerializedName("before_bal")
        private String beforeBalance = "";
        @SerializedName("after_bal")
        private String afterBalance = "";
        private String amount = "";
        private String reqTime = "";
        private String mode = "";
        private String yourAccount = "";
        private String refNumber = "";
        private String remark = "";
        private String toBank = "";
        private String status = "";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBeforeBalance() {
            return beforeBalance;
        }

        public void setBeforeBalance(String beforeBalance) {
            this.beforeBalance = beforeBalance;
        }

        public String getAfterBalance() {
            return afterBalance;
        }

        public void setAfterBalance(String afterBalance) {
            this.afterBalance = afterBalance;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getReqTime() {
            return reqTime;
        }

        public void setReqTime(String reqTime) {
            this.reqTime = reqTime;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getYourAccount() {
            return yourAccount;
        }

        public void setYourAccount(String yourAccount) {
            this.yourAccount = yourAccount;
        }

        public String getRefNumber() {
            return refNumber;
        }

        public void setRefNumber(String refNumber) {
            this.refNumber = refNumber;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getToBank() {
            return toBank;
        }

        public void setToBank(String toBank) {
            this.toBank = toBank;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public class Bank {
        private String bankName = "";
        private String accountNo = "";
        private String acName = "";
        private String branchName = "";
        private String ifscCode = "";

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getAccountNo() {
            return accountNo;
        }

        public void setAccountNo(String accountNo) {
            this.accountNo = accountNo;
        }

        public String getAcName() {
            return acName;
        }

        public void setAcName(String acName) {
            this.acName = acName;
        }

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public String getIfscCode() {
            return ifscCode;
        }

        public void setIfscCode(String ifscCode) {
            this.ifscCode = ifscCode;
        }
    }
}
