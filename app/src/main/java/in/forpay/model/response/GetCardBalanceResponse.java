package in.forpay.model.response;

public class GetCardBalanceResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private String cardBal = "";
        private String resCode = "";
        private String resText = "";

        public String getCardBal() {
            return cardBal;
        }

        public void setCardBal(String cardBal) {
            this.cardBal = cardBal;
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
