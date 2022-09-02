package in.forpay.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetPayoutBankDetails {

    @SerializedName("data")
    private ArrayList<Data> bankDetails=new ArrayList<>();
    private String resCode="";
    private String resText="";
    private String balance="";
    private String showAddButton="";
    private String paymentMethod="";

    public ArrayList<Data> bankDetails() {
        return bankDetails;
    }

    public String getBalance() {
        return balance;
    }

    public String getShowAddButton() {
        return showAddButton;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void setShowAddButton(String showAddButton) {
        this.showAddButton = showAddButton;
    }

    public ArrayList<Data> getBankDetails() {
        return bankDetails;
    }


    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public void setResText(String resText) {
        this.resText = resText;
    }

    public void setBankDetails(ArrayList<Data> bankDetails) {
        this.bankDetails = bankDetails;
    }

    public String getResText() {
        return resText;
    }



    public class Data{
        private String name,accountNumber,ifscCode,status="";

        public String getAccountNumber() {
            return accountNumber;
        }

        public String getIfscCode() {
            return ifscCode;
        }

        public String getName() {
            return name;
        }

        public String getStatus() {
            return status;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public void setIfscCode(String ifscCode) {
            this.ifscCode = ifscCode;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

}
