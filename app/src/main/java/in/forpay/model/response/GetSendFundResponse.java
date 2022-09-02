package in.forpay.model.response;

public class GetSendFundResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public class Data {
        private String resCode = "";
        private String resText = "";
        private String mobile = "";
        private String beneficiaryId = "";
        private String beneficiaryName = "";
        private String beneficiaryAccountNumber = "";
        private String orderId = "";
        private String status = "";
        private String amount = "";
        private String operatorId = "";
        private String service = "";
        private String bal = "";
        private String commissionBal = "";
        private String TransId = "";
        private String creditUsed,cusCreditUsed = "";
        private String marginAmount = "";
        private String reqTime = "";
        private String profit,opValue1,opValue2,opValue3,opValue4,opValue5,ccf,paymentMode="";


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

        public String getResCode() {
            return resCode;
        }

        public String getResText() {
            return resText;
        }

        public String getMobile() {
            return mobile;
        }

        public String getBeneficiaryId() {
            return beneficiaryId;
        }

        public String getBeneficiaryName() {
            return beneficiaryName;
        }

        public String getBeneficiaryAccountNumber() {
            return beneficiaryAccountNumber;
        }

        public String getOrderId() {
            return orderId;
        }

        public String getStatus() {
            return status;
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

        public String getTransId() {
            return TransId;
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

        public String getReqTime() {
            return reqTime;
        }


    }
}
