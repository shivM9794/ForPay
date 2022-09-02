package in.forpay.model.response;

public class GetRefundReprocessResponse {

    private Data data;
    private String resText = "";

    public Data getData() {
        return data;
    }

    public class Data {
        private String orderId = "";
        private String status = "";
        private String resCode = "";
        private String resText = "";

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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
}
