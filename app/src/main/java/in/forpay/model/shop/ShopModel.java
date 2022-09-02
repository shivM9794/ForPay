package in.forpay.model.shop;

import java.util.List;

public class ShopModel {


    /**
     * data : [{"shopId":"5","shopName":"acv","imageId":"0","imgUrl":"","contactNumber":"7678787878","deliveryRange":"20","latitude":"21.963013","longitude":"70.796104","products":[{"productId":"2","productName":"Mouse"}],"distance":"1.115205035528697","avgDeliveryTime":"2 Hour","avgRating":"4.2","discription":"This is test shop where you can buy anything online within your market","lastActive":"1 Hr Ago"},{"shopId":"6","shopName":"def","imageId":"0","imgUrl":"","contactNumber":"7777777777","deliveryRange":"50","latitude":"21.978273","longitude":"70.805664","products":[{"productId":"2","productName":"Mouse"}],"distance":"2.934248924447557","avgDeliveryTime":"2 Hour","avgRating":"4.2","discription":"This is test shop where you can buy anything online within your market","lastActive":"1 Hr Ago"},{"shopId":"7","shopName":"ssd","imageId":"0","imgUrl":"","contactNumber":"9988909234","deliveryRange":"50","latitude":"21.994230","longitude":"70.788254","products":[{"productId":"2","productName":"Mouse"}],"distance":"4.673784996367106","avgDeliveryTime":"2 Hour","avgRating":"4.2","discription":"This is test shop where you can buy anything online within your market","lastActive":"1 Hr Ago"},{"shopId":"8","shopName":"asf","imageId":"0","imgUrl":"","contactNumber":"8999898789","deliveryRange":"50","latitude":"22.008356","longitude":"70.802017","products":[{"productId":"2","productName":"Mouse"}],"distance":"6.169225555337974","avgDeliveryTime":"2 Hour","avgRating":"4.2","discription":"This is test shop where you can buy anything online within your market","lastActive":"1 Hr Ago"},{"shopId":"4","shopName":"cde","imageId":"0","imgUrl":"","contactNumber":"7777777777","deliveryRange":"100","latitude":"22.028402","longitude":"70.788101","products":[{"productId":"2","productName":"Mouse"}],"distance":"8.432870844899503","avgDeliveryTime":"2 Hour","avgRating":"4.2","discription":"This is test shop where you can buy anything online within your market","lastActive":"1 Hr Ago"},{"shopId":"3","shopName":"Test","imageId":"0","imgUrl":"","contactNumber":"8888888888","deliveryRange":"20","latitude":"22.048151","longitude":"70.812027","products":[{"productId":"2","productName":"Mouse"}],"distance":"10.683353807727132","avgDeliveryTime":"2 Hour","avgRating":"4.2","discription":"This is test shop where you can buy anything online within your market","lastActive":"1 Hr Ago"},{"shopId":"1","shopName":"ForPay","imageId":"1","imgUrl":"https://fdn2.gsmarena.com/vv/bigpic/motorola-moto-g6-r1.jpg","contactNumber":"900000000","deliveryRange":"1.2","latitude":"23.978792","longitude":"85.381721","products":[{"productId":"1","productName":"Phone"},{"productId":"2","productName":"Mouse"}],"distance":"1509.336401005945","avgDeliveryTime":"2 Hour","avgRating":"4.2","discription":"This is test shop where you can buy anything online within your market","lastActive":"1 Hr Ago"},{"shopId":"2","shopName":"Abc","imageId":"0","imgUrl":"","contactNumber":"9999999999","deliveryRange":"1","latitude":"22.050808","longitude":"22.050808","products":[{"productId":"2","productName":"Mouse"}],"distance":"5002.967797707162","avgDeliveryTime":"2 Hour","avgRating":"4.2","discription":"This is test shop where you can buy anything online within your market","lastActive":"1 Hr Ago"},{"shopId":"10","shopName":"afg","imageId":"0","imgUrl":"","contactNumber":"8888888888","deliveryRange":"20","latitude":"22.048151","longitude":"22.048151","products":[{"productId":"2","productName":"Mouse"}],"distance":"5003.286712154195","avgDeliveryTime":"2 Hour","avgRating":"4.2","discription":"This is test shop where you can buy anything online within your market","lastActive":"1 Hr Ago"},{"shopId":"9","shopName":"jk","imageId":"0","imgUrl":"","contactNumber":"8989787889","deliveryRange":"1","latitude":"22.035433","longitude":"22.035433","products":[{"productId":"2","productName":"Mouse"}],"distance":"5004.813550161866","avgDeliveryTime":"2 Hour","avgRating":"4.2","discription":"This is test shop where you can buy anything online within your market","lastActive":"1 Hr Ago"}]
     * resCode : 200
     * resText :
     */

    private String resCode;
    private String resText;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * shopId : 5
         * shopName : acv
         * imageId : 0
         * imgUrl :
         * contactNumber : 7678787878
         * deliveryRange : 20
         * latitude : 21.963013
         * longitude : 70.796104
         * products : [{"productId":"2","productName":"Mouse"}]
         * distance : 1.115205035528697
         * avgDeliveryTime : 2 Hour
         * avgRating : 4.2
         * discription : This is test shop where you can buy anything online within your market
         * lastActive : 1 Hr Ago
         */

        private String shopId;
        private String shopName;
        private String imageId;
        private String imgUrl;
        private String contactNumber;
        private String deliveryRange;
        private String latitude;
        private String longitude;
        private String distance;
        private String avgDeliveryTime;
        private String avgRating;
        private String discription;
        private String lastActive;
        private List<ProductsBean> products;

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

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

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getAvgDeliveryTime() {
            return avgDeliveryTime;
        }

        public void setAvgDeliveryTime(String avgDeliveryTime) {
            this.avgDeliveryTime = avgDeliveryTime;
        }

        public String getAvgRating() {
            return avgRating;
        }

        public void setAvgRating(String avgRating) {
            this.avgRating = avgRating;
        }

        public String getDiscription() {
            return discription;
        }

        public void setDiscription(String discription) {
            this.discription = discription;
        }

        public String getLastActive() {
            return lastActive;
        }

        public void setLastActive(String lastActive) {
            this.lastActive = lastActive;
        }

        public List<ProductsBean> getProducts() {
            return products;
        }

        public void setProducts(List<ProductsBean> products) {
            this.products = products;
        }

        public static class ProductsBean {
            /**
             * productId : 2
             * productName : Mouse
             */

            private String productId;
            private String productName;

            public String getProductId() {
                return productId;
            }

            public void setProductId(String productId) {
                this.productId = productId;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }
        }
    }
}
