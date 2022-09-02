package in.forpay.model.response;

public class GetDeactivateDeviceResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private String imei = "";
        private String token = "";
        private String resCode = "";
        private String resText = "";

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
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
