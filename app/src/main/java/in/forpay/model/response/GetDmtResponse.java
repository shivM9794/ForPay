package in.forpay.model.response;

public class GetDmtResponse {

    private String resCode,resText,availableLimit;

    public String getAvailableLimit() {
        return availableLimit;
    }

    public void setAvailableLimit(String availableLimit) {
        this.availableLimit = availableLimit;
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

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }
}
