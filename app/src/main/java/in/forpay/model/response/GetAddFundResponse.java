package in.forpay.model.response;

public class GetAddFundResponse {

    private Data data;
    private String resCode = "";
    private String resText = "";

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private String checkSum = "";
        private String orderId = "";
        private String amount = "";
        private String mid = "";
        private String cusId = "";
        private String email="";
        private String userName="";
        private String channelId,website,cbUrl,indType,bal,gateway,upiId,var1,var2,var3,intentType,paymentMethod,action="";

        public String getAction() {
            return action;
        }

        public String getVar1() {
            return var1;
        }

        public String getVar2() {
            return var2;
        }

        public String getVar3() {
            return var3;
        }

        public String getIntentType() {
            return intentType;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }


        public String getUserName() {
            return userName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUpiId() {
            return upiId;
        }

        public void setUpiId(String upiId) {
            this.upiId = upiId;
        }

        public String getGateway() {
            return gateway;
        }

        public void setGateway(String gateway) {
            this.gateway = gateway;
        }

        public String getCheckSum(){
            return checkSum;
        }

        public String getBal(){return bal;}

        public String getOrderId(){
            return orderId;
        }

        public String getAmount(){
            return amount;
        }

        public String getMid(){
            return mid;
        }


        public String getCusId() {
            return cusId;
        }

        public String getChannelId() {
            return channelId;
        }


        public String getWebsite() {
            return website;
        }

        public String getCbUrl() {
            return cbUrl;
        }


        public String getIndType() {
            return indType;
        }


    }

    public String getResCode() {
        return resCode;
    }

    public String getResText() {
        return resText;
    }
}
