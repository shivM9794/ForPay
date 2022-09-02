package in.forpay.model.request;

public class GetKeyResponse {

        private String resCode = "";
        private String resText = "";
        private String smsKey="";
        private String mobile="";

    public String getMobile() {
        return mobile;
    }

    public String getSmsKey() {
        return smsKey;
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
