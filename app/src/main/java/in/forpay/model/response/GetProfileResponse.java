package in.forpay.model.response;

import java.util.ArrayList;

public class GetProfileResponse {

    private ArrayList<Data> dataList = new ArrayList<>();

    private String resCode = "";
    private String resText = "";
    private String offerUrl="";
    private String mlm="";
    private String isDistributor="";
    private String distributorHeader="";
    private String downloadUrl="";
    private Data data;


    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getIsDistributor() {
        return isDistributor;
    }

    public String getDistributorHeader() {
        return distributorHeader;
    }

    private ArrayList<IncentiveHistory> incentiveHistory;
    public ArrayList<IncentiveHistory> getIncentiveHistory(){
        return incentiveHistory;
    }

    public void setIncentiveHistory(ArrayList<IncentiveHistory> incentiveHistory) {
        this.incentiveHistory = incentiveHistory;
    }

    private ArrayList<PremiumBanner> premiumBanner;
    private String premiumHeader;

    public ArrayList<PremiumBanner> getPremiumBanner() {
        return premiumBanner;
    }

    public void setPremiumBanner(ArrayList<PremiumBanner> premiumBanner) {
        this.premiumBanner = premiumBanner;
    }

    public String getPremiumHeader() {
        return premiumHeader;
    }

    public void setPremiumHeader(String premiumHeader) {
        this.premiumHeader = premiumHeader;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMlm() {
        return mlm;
    }
    public String getOfferUrl() {
        return offerUrl;
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

    public static class Data {
        private String name = "";
        private String mobile = "";
        private String email = "";
        private String pincode = "";
        private String pan = "";
        private String bal,commissionBal,referralUrl,referCount,homeLandmark="";
        public String getBal(){
            return bal;
        }
        public void setBal(String bal){this.bal=bal;}

        public String getCommissionBal(){
            return commissionBal;
        }
        public void setCommissionBal(String commissionBal){this.bal=commissionBal;}

        public String getReferralUrl(){
            return referralUrl;
        }
        public void setReferralUrl(String referralUrl){this.referralUrl=referralUrl;}

        public String getReferCount(){
            return referCount;
        }
        public void setReferCount(String referCount){this.referCount=referCount;}


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        public String getPan() {
            return pan;
        }

        public void setPan(String pan) {
            this.pan = pan;
        }

        public String getHomeLandmark(){return homeLandmark;}

        public void setHomeLandmark(String homeLandmark){this.homeLandmark=homeLandmark;}

    }

    public static class PremiumBanner{

        String url, text;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class IncentiveHistory{

        String iconUrl,date,closingBal,description,amount;

        public String getAmount() {
            return amount;
        }

        public String getClosingBal() {
            return closingBal;
        }

        public String getDate() {
            return date;
        }

        public String getDescription() {
            return description;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public void setClosingBal(String closingBal) {
            this.closingBal = closingBal;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }
    }
}
