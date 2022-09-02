package in.forpay.model.response;

public class GetLogoutResponse {
    private String resText="";
    private String resCode="";

    public void setResText(String resText) {
        this.resText = resText;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResText() {
        return resText;
    }

    public String getResCode() {
        return resCode;
    }
}
