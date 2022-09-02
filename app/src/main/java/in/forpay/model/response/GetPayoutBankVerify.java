package in.forpay.model.response;

public class GetPayoutBankVerify {


    private String resCode="";
    private String resText="";
    private String accountName="";
    private String bankName="";
    private String branch="";

    public String getAccountName() {
        return accountName;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBranch() {
        return branch;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setBranch(String branch) {
        this.branch = branch;
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
}
