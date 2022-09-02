package in.forpay.database;

public class DataPlansModel {

    String type;
    String amount;
    String detail;
    String validity;
    String talktime;
    String extraOffer;
    String commission;
    String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getTalktime() {
        return talktime;
    }

    public void setTalktime(String talktime) {
        this.talktime = talktime;
    }

    public String getExtraOffer() {
        return extraOffer;
    }

    public void setExtraOffer(String extraOffer) {
        this.extraOffer = extraOffer;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public DataPlansModel() {
    }

    public DataPlansModel( String type, String amount, String detail, String validity, String talktime, String extraOffer, String commission, String data) {


        this.type = type;
        this.amount = amount;
        this.detail = detail;
        this.validity = validity;
        this.talktime = talktime;
        this.extraOffer = extraOffer;
        this.commission = commission;
        this.data = data;
    }
}
