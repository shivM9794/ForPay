package in.forpay.model.shop;

public class GetShopModel {


    /**
     * data : {"shopName":"","imageId":"0","imgUrl":"","contactNumber":"8888888888","deliveryRange":"20","latitude":"22.048151","longitude":"70.812027","catIds":"002,","productIds":"002,"}
     * resCode : 200
     * resText :
     */

    private DataBean data;
    private String resCode;
    private String resText;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
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

    public static class DataBean {
        /**
         * shopName :
         * imageId : 0
         * imgUrl :
         * contactNumber : 8888888888
         * deliveryRange : 20
         * latitude : 22.048151
         * longitude : 70.812027
         * catIds : 002,
         * productIds : 002,
         */

        private String shopName;
        private String imageId;
        private String imgUrl;
        private String contactNumber;
        private String deliveryRange;
        private String latitude;
        private String longitude;
        private String catIds;
        private String productIds;

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getImageId() {
            return imageId;
        }

        public void setImageId(String imageId) {
            this.imageId = imageId;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public String getDeliveryRange() {
            return deliveryRange;
        }

        public void setDeliveryRange(String deliveryRange) {
            this.deliveryRange = deliveryRange;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getCatIds() {
            return catIds;
        }

        public void setCatIds(String catIds) {
            this.catIds = catIds;
        }

        public String getProductIds() {
            return productIds;
        }

        public void setProductIds(String productIds) {
            this.productIds = productIds;
        }
    }
}
