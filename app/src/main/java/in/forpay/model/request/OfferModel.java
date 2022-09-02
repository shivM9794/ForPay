package in.forpay.model.request;

import androidx.databinding.BaseObservable;

public class OfferModel extends BaseObservable {
    private String type = "";
    private String amount = "";
    private String detail = "";
    private String validity = "";
    private String talktime = "";
    private String extraoffer = "";
    private String commission = "";
    private String data = "";


    public OfferModel(String type, String amount, String detail, String validity, String talktime,String extraoffer,String commission,String data) {
        this.type = type;
        this.amount = amount;
        this.detail = detail;
        this.validity = validity;
        this.talktime = talktime;
        this.extraoffer = extraoffer;
        this.commission = commission;
        this.data = data;
    }

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


    public String getExtraoffer() {
        return extraoffer;
    }

    public void setExtraoffer(String extraoffer) {
        this.extraoffer = extraoffer;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }


}
