package in.forpay.model.request;

public class RechargeHistory {
    private String orderId = "";
    private String status = "";
    private String service = "";
    private String TransId = "";
    private String mobile = "";
    private String amount = "";
    private String creditUsed = "";
    private String cusCreditUsed="";
    private String marginAmount = "";
    private String beneficiaryAccountNumber = "";
    private String beneficiaryName = "";
    private String reqTime = "";
    private String operatorId="";
    private String bottomIconUrl ="";
    private String visibleCustomerCopy="";
    private String profit,opValue1,opValue2,opValue3,opValue4,opValue5,ccf,paymentMode,operatorIcon="";
    public RechargeHistory() {
    }

    public RechargeHistory(String orderId, String status, String service, String transId,
                           String mobile, String amount, String creditUsed, String cusCreditUsed, String marginAmount,
                           String beneficiaryAccountNumber, String beneficiaryName, String reqTime , String operatorId,String profit,
                           String opValue1,String opValue2,String opValue3,String opValue4,String opValue5,String ccf,String paymentMode,String visibleCustomerCopy,String operatorIcon) {
        this.orderId = orderId;
        this.status = status;
        this.service = service;
        TransId = transId;
        this.mobile = mobile;
        this.amount = amount;
        this.creditUsed = creditUsed;
        this.cusCreditUsed=cusCreditUsed;
        this.marginAmount = marginAmount;
        this.beneficiaryAccountNumber = beneficiaryAccountNumber;
        this.beneficiaryName = beneficiaryName;
        this.reqTime = reqTime;
        this.operatorId=operatorId;
        this.profit=profit;
        this.opValue1=opValue1;
        this.opValue2=opValue2;
        this.opValue3=opValue3;
        this.opValue4=opValue4;
        this.opValue5=opValue5;
        this.ccf=ccf;
        this.paymentMode=paymentMode;
        this.visibleCustomerCopy=visibleCustomerCopy;
        this.operatorIcon=operatorIcon;

    }

    public String getOperatorIcon() {
        return operatorIcon;
    }

    public void setOperatorIcon(String operatorIcon) {
        this.operatorIcon = operatorIcon;
    }

    public String getVisibleCustomerCopy() {
        return visibleCustomerCopy;
    }

    public void setVisibleCustomerCopy(String visibleCustomerCopy) {
        this.visibleCustomerCopy = visibleCustomerCopy;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public String getCcf() {
        return ccf;
    }

    public String getOpValue5() {
        return opValue5;
    }

    public String getOpValue4() {
        return opValue4;
    }

    public String getOpValue3() {
        return opValue3;
    }

    public String getOpValue2() {
        return opValue2;
    }

    public String getOpValue1() {
        return opValue1;
    }

    public void setCcf(String ccf) {
        this.ccf = ccf;
    }

    public void setOpValue1(String opValue1) {
        this.opValue1 = opValue1;
    }

    public void setOpValue2(String opValue2) {
        this.opValue2 = opValue2;
    }

    public void setOpValue3(String opValue3) {
        this.opValue3 = opValue3;
    }

    public void setOpValue4(String opValue4) {
        this.opValue4 = opValue4;
    }

    public void setOpValue5(String opValue5) {
        this.opValue5 = opValue5;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getBottomIconUrl() {
        return bottomIconUrl;
    }

    public void setBottomIconUrl(String bottomIconUrl) {
        this.bottomIconUrl = bottomIconUrl;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOperatorId(){return operatorId;}

    public void setOperatorId(String operatorId){this.operatorId=operatorId;}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getTransId() {
        return TransId;
    }

    public void setTransId(String transId) {
        TransId = transId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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


    public String getMarginAmount() {
        return marginAmount;
    }

    public void setMarginAmount(String marginAmount) {
        this.marginAmount = marginAmount;
    }

    public String getBeneficiaryAccountNumber() {
        return beneficiaryAccountNumber;
    }

    public void setBeneficiaryAccountNumber(String beneficiaryAccountNumber) {
        this.beneficiaryAccountNumber = beneficiaryAccountNumber;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getReqTime() {
        return reqTime;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }


}
