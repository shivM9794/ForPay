package in.forpay.model.response;

public class GetChangePasswordResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    private String resCode = "";
    private String resText = "";

    public String getResCode() {
        return resCode;
    }

    public String getResText() {
        return resText;
    }
    public class Data {

    }
}
