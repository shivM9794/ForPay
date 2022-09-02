package in.forpay.model.response;

public class GetRequestFundResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private String resCode = "";
        private String resText = "";

        public String getResCode() {
            return resCode;
        }

        public String getResText() {
            return resText;
        }
    }
}
