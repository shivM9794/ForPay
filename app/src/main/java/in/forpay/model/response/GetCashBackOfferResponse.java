package in.forpay.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetCashBackOfferResponse {
@SerializedName("data")
    private ArrayList<Data> offerDetails=new ArrayList<>();
    private String resCode="";
    private String resText="";

    public ArrayList<Data> getOfferDetails() {
        return offerDetails;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public void setResText(String resText) {
        this.resText = resText;
    }

    public void setOfferDetails(ArrayList<Data> offerDetails) {
        this.offerDetails = offerDetails;
    }

    public String getResText() {
        return resText;
    }



    public class Data{
        private String id,coupon,amount,details,available,reason,expireDate="";

        public String getAmount() {
            return amount;
        }

        public String getAvailable() {
            return available;
        }

        public String getCoupon() {
            return coupon;
        }

        public String getDetails() {
            return details;
        }

        public String getExpireDate() {
            return expireDate;
        }

        public String getId() {
            return id;
        }

        public String getReason() {
            return reason;
        }
    }
}
