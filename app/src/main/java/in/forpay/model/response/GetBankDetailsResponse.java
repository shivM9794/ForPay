package in.forpay.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetBankDetailsResponse {

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
