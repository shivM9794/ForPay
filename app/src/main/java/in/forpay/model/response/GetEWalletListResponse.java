package in.forpay.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetEWalletListResponse {
    @SerializedName("brandList")
    private ArrayList<BrandList> brandList = new ArrayList<>();
    private String resCode;
    private String resText;

    public ArrayList<BrandList> getBrandList() {
        return brandList;
    }

    public void setBrandList(ArrayList<BrandList> brandList) {
        this.brandList = brandList;
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

    public class BrandList {

        private String ewalletId, brandName, image;

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
}
