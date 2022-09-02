package in.forpay.model.response;

import java.util.ArrayList;

public class GetBalanceResponse {

    private String resCode = "";
    private String resText = "";
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getResCode() {
        return resCode;
    }

    public String getResText() {
        return resText;
    }


    public void setResText(String resText) {
        this.resText = resText;
    }



    public class Data {
        private String bal = "";
        private String commissionBal = "";
        private String offerUrl="";
        private String isPremium="";
        private String isShop="";
        private String additionalData="";
        private String showPaymentHistory="";

        public String getShowPaymentHistory() {
            return showPaymentHistory;
        }

        private ArrayList<PaymentGateway> paymentGateway;

        public ArrayList<PaymentGateway> getPaymentGateway() {
            return paymentGateway;
        }

        public void setPaymentGateway(ArrayList<PaymentGateway> paymentGateway) {
            this.paymentGateway = paymentGateway;
        }



        private ArrayList<PaymentHistory> paymentHistory;

        public ArrayList<PaymentHistory> getPaymentHistory() {
            return paymentHistory;
        }

        public void setPaymentHistories(ArrayList<PaymentHistory> paymentHistory) {
            this.paymentHistory = paymentHistory;
        }

        public class PaymentHistory{
            private String orderId,amount,date,refNumber,method;

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getRefNumber() {
                return refNumber;
            }

            public void setRefNumber(String refNumber) {
                this.refNumber = refNumber;
            }

            public String getMethod() {
                return method;
            }

            public void setMethod(String method) {
                this.method = method;
            }
        }



        public class  PaymentGateway{
            private String name,text,iconUrl,type;

            public String getIconUrl() {
                return iconUrl;
            }

            public String getName() {
                return name;
            }

            public String getText() {
                return text;
            }

            public String getType() {
                return type;
            }
        }

        public String getAdditionalData() {
            return additionalData;
        }

        public String getIsShop(){
            return isShop;
        }

        public String getIsPremium() {
            return isPremium;
        }

        public String getBal() {
            return bal;
        }

        public void setBal(String bal) {
            this.bal = bal;
        }

        public String getCommissionBal() {
            return commissionBal;
        }

        public void setCommissionBal(String commissionBal) {
            this.commissionBal = commissionBal;
        }

        public String getOfferUrl() {
            return offerUrl;
        }

        public void setOfferUrl(String offerUrl) {
            this.offerUrl = offerUrl;
        }
    }

}
