package in.forpay.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class GiftVoucherResponse implements Parcelable {

    private List<GiftVoucherResponse.ServiceBean> brandList;
    private String resCode;
    private String resText;


    protected GiftVoucherResponse(Parcel in) {
        brandList = in.createTypedArrayList(ServiceBean.CREATOR);
        resCode = in.readString();
        resText = in.readString();
    }



    public static final Creator<GiftVoucherResponse> CREATOR = new Creator<GiftVoucherResponse>() {
        @Override
        public GiftVoucherResponse createFromParcel(Parcel in) {
            return new GiftVoucherResponse(in);
        }

        @Override
        public GiftVoucherResponse[] newArray(int size) {
            return new GiftVoucherResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(resCode);
        parcel.writeString(resText);
    }
    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }


    public List<ServiceBean> getBrandList() {
        return brandList;
    }

    public static class ServiceBean implements Parcelable{


        private String brandName;
        private String category;
        private String discount;
        private String discountType;
        private String productId;
        private String image;


        protected ServiceBean(Parcel in) {
            brandName = in.readString();
            category = in.readString();
            discount = in.readString();
            discountType = in.readString();
            productId=in.readString();
            image=in.readString();
        }

        public static final Creator<ServiceBean> CREATOR = new Creator<ServiceBean>() {
            @Override
            public ServiceBean createFromParcel(Parcel in) {
                return new ServiceBean(in);
            }

            @Override
            public ServiceBean[] newArray(int size) {
                return new ServiceBean[size];
            }
        };

        public String getBrandName() {
            return brandName;
        }

        public String getCategory() {
            return category;
        }

        public String getDiscount() {
            return discount;
        }

        public String getImage() {
            return image;
        }

        public String getDiscountType() {
            return discountType;
        }

        public String getProductId() {
            return productId;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public void setDiscountType(String discountType) {
            this.discountType = discountType;
        }

        public void setImage(String image) {
            this.image = image;
        }


        public void setProductId(String productId) {
            this.productId = productId;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(brandName);
            parcel.writeString(category);
            parcel.writeString(discount);
            parcel.writeString(discountType);
            parcel.writeString(productId);
            parcel.writeString(image);
        }

    }



}
