package in.forpay.model.response;

public class GetRechargeNowResponse {

    private String resCode = "";
    private String resText = "";

    public String getResCode() {
        return resCode;
    }

    public String getResText() {
        return resText;
    }
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private String orderId = "";
        private String status = "";
        private String mobile = "";
        private String amount = "";
        private String operatorId = "";
        private String service = "";
        private String bal = "";
        private String commissionBal = "";
        private String creditUsed = "";
        private String cusCreditUsed = "";
        private String marginAmount = "";
        private String beneficiaryAccountNumber = "";
        private String beneficiaryName = "";
        private String TransId = "";
        private String profit,opValue1,opValue2,opValue3,opValue4,opValue5,ccf,paymentMode="";

        private String billAmount = "";
        private String billName = "";

        public String getOpValue1() {
            return opValue1;
        }

        public String getOpValue2() {
            return opValue2;
        }

        public String getOpValue3() {
            return opValue3;
        }

        public String getOpValue4() {
            return opValue4;
        }

        public String getOpValue5() {
            return opValue5;
        }

        public String getCcf() {
            return ccf;
        }

        public String getPaymentMode() {
            return paymentMode;
        }
        public String getProfit() {
            return profit;
        }

        public String getBillAmount() {
            return billAmount;
        }

        public void setBillAmount(String billAmount) {
            this.billAmount = billAmount;
        }

        public String getBillName() {
            return billName;
        }

        public void setBillName(String billName) {
            this.billName = billName;
        }

        private String reqTime = "";


        public String getOrderId() {
            return orderId;
        }

        public String getStatus() {
            return status;
        }

        public String getMobile() {
            return mobile;
        }

        public String getAmount() {
            return amount;
        }

        public String getOperatorId() {
            return operatorId;
        }

        public String getService() {
            return service;
        }

        public String getBal() {
            return bal;
        }

        public String getCommissionBal() {
            return commissionBal;
        }

        public String getCreditUsed() {
            return creditUsed;
        }

        public String getCusCreditUsed() {
            return cusCreditUsed;
        }

        public String getMarginAmount() {
            return marginAmount;
        }

        public String getBeneficiaryAccountNumber() {
            return beneficiaryAccountNumber;
        }

        public String getBeneficiaryName() {
            return beneficiaryName;
        }

        public String getTransId() {
            return TransId;
        }

        public String getReqTime() {
            return reqTime;
        }

        public void setReqTime(String reqTime) {
            this.reqTime = reqTime;
        }




    }
}
