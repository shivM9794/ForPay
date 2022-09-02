package in.forpay.model.response;

public class GetAddBeneficiaryResponse {

    private Data data;
    private String resCode = "";
    private String resText = "";

    public Data getData() {
        return data;
    }

    public String getResCode() {
        return resCode;
    }

    public String getResText() {
        return resText;
    }

    public class Data {
        private String mobile = "";
        private String beneficiaryId = "";

        public String getMobile() {
            return mobile;
        }

        public String getBeneficiaryId() {
            return beneficiaryId;
        }
    }
}
