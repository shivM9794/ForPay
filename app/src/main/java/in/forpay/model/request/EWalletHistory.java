package in.forpay.model.request;

public class EWalletHistory {

//    private String ewalletId, brandName, image;

    public String ewalletId = "";
    public String brandName = "";
    public String image = "";

    public EWalletHistory(String ewalletId, String brandName, String image) {
        this.ewalletId = ewalletId;
        this.brandName = brandName;
        this.image = image;
    }

    public String getEwalletId() {
        return ewalletId;
    }

    public void setEwalletId(String ewalletId) {
        this.ewalletId = ewalletId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
