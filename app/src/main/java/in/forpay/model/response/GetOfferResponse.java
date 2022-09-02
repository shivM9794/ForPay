package in.forpay.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetOfferResponse {

    @SerializedName("data")
    private ArrayList<Data> offerList = new ArrayList<>();
    private String resCode = "";
    private String resText = "";

    public ArrayList<Data> getOfferList() {
        return offerList;
    }

    public void setOfferList(ArrayList<Data> offerList) {
        this.offerList = offerList;
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

    public class Data {
        private String type = "";
        private String amount = "";
        private String detail = "";
        private String validity = "";
        private String talktime = "";
        private String extraOffer = "";
        private String commission = "";
        private String data = "";

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
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
    }
}
