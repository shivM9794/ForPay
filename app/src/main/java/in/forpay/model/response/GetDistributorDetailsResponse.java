package in.forpay.model.response;

public class GetDistributorDetailsResponse {
    private String resCode = "";
    private String resText,status,refer,distributorName,distance,continueBtn = "";


    public String getDistance() {
        return distance;
    }

    public String getContinueBtn() {
        return continueBtn;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public String getResCode() {
        return resCode;
    }

    public String getStatus() {
        return status;
    }

    public String getRefer() {
        return refer;
    }

    public String getResText() {
        return resText;
    }
}
