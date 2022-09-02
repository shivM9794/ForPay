package in.forpay.database;

public class BalanceHistoryModel1 {


    public String date = "";
    public String openingBal = "";
    public String closingBal = "";
    public String type = "";

    private String amount = "";

    private String orderId = "";

    private String status = "";

    private String optId = "";
    private String mobile = "";

    private String creditUsed = "";
    private String service = "";
    private String bankAccount = "";
    private String bankName = "";
    private String marginAmount = "";


    public String getDate() {
        return date;
    }


    public void setDate(String date) {
        this.type = date;
    }

    public String getOpeningBal() {
        return openingBal;
    }
    public void setOpeningBal(String openingBal){this.openingBal=openingBal;}

    public String getClosingBal() {
        return closingBal;
    }
    public void setClosingBal(String closingBal){this.closingBal=closingBal;}


    public String getType() {
        return type;
    }
    public void setType(String type){this.type=type;}

    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount){this.amount=amount;}

    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId){this.orderId=orderId;}

    public String getStatus() {
        return status;
    }
    public void setStatus(String status){this.status=status;}

    public String getOptId() {
        return optId;
    }
    public void setOptId(String optId){this.optId=optId;}

    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile){this.mobile=mobile;}

    public String getCreditUsed() {
        return creditUsed;
    }
    public void setCreditUsed(String creditUsed){this.creditUsed=creditUsed;}

    public String getService() {
        return service;
    }
    public void setService(String service){this.service=service;}

    public String getBankAccount() {
        return bankAccount;
    }
    public void setBankAccount(String bankAccount){this.bankAccount=bankAccount;}

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName){this.bankName=bankName;}

    public String getMarginAmount() {
        return marginAmount;
    }
    public void setMarginAmount(String marginAmount){this.marginAmount=marginAmount;}

    public BalanceHistoryModel1(){

    };

    public BalanceHistoryModel1(String date, String openingBal, String closingBal, String type, String amount, String orderId,
                                String status, String optId , String mobile, String creditUsed, String service, String bankAccount,
                                String bankName , String marginAmount) {

        this.date = date;
        this.openingBal = openingBal;
        this.closingBal = closingBal;
        this.type = type;
        this.amount = amount;
        this.orderId = orderId;
        this.status = status;
        this.optId=optId;
        this.mobile=mobile;
        this.creditUsed=creditUsed;
        this.service=service;
        this.bankAccount=bankAccount;
        this.bankName=bankName;
        this.marginAmount=marginAmount;
    }


}
