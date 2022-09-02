package in.forpay.model.shop;

import java.util.List;

public class OrderStatusModel {


    /**
     * data : {"shopName":"","imageId":"","imgUrl":"","contactNumber":"","deliveryRange":"","latitude":"","longitude":"","orderTime":"","deliveryCode":"","deliveryExpectedDays":"","deliveredTime":"2020-07-16 14:23:02","deliveryType":"","trackingId":"","deliveryAddress":"","statusArray":[{"status":"DELIVERED","statusText":"Order Delivered","time":"2020-07-16 14:23:02","checked":"yes"},{"status":"DISPATCHED","statusText":"Order Dispatched , Check Delivery Status","time":"2020-07-15 10:26:55","checked":"yes"},{"status":"PROCESSING","statusText":"Order Now Processing, Click to Dispatch","time":"2020-07-14 19:31:32","checked":"yes"},{"status":"ACCEPTED","statusText":"Order Accepted , Process it now","time":"2020-07-14 19:22:11","checked":"yes"},{"status":"PAID","statusText":"User paid , Click to Accept order","time":"2020-07-07 10:18:59","checked":"yes"},{"status":"PENDING","statusText":"Waiting for user to accept it","time":"2020-06-26 12:51:07","checked":"yes"}],"rating":"4","ratingText":"buyer review"}
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
         * imageId :
         * imgUrl :
         * contactNumber :
         * deliveryRange :
         * latitude :
         * longitude :
         * orderTime :
         * deliveryCode :
         * deliveryExpectedDays :
         * deliveredTime : 2020-07-16 14:23:02
         * deliveryType :
         * trackingId :
         * deliveryAddress :
         * statusArray : [{"status":"DELIVERED","statusText":"Order Delivered","time":"2020-07-16 14:23:02","checked":"yes"},{"status":"DISPATCHED","statusText":"Order Dispatched , Check Delivery Status","time":"2020-07-15 10:26:55","checked":"yes"},{"status":"PROCESSING","statusText":"Order Now Processing, Click to Dispatch","time":"2020-07-14 19:31:32","checked":"yes"},{"status":"ACCEPTED","statusText":"Order Accepted , Process it now","time":"2020-07-14 19:22:11","checked":"yes"},{"status":"PAID","statusText":"User paid , Click to Accept order","time":"2020-07-07 10:18:59","checked":"yes"},{"status":"PENDING","statusText":"Waiting for user to accept it","time":"2020-06-26 12:51:07","checked":"yes"}]
         * rating : 4
         * ratingText : buyer review
         */

        private String shopName;
        private String imageId;
        private String imgUrl;
        private String contactNumber;
        private String deliveryRange;
        private String latitude;
        private String longitude;
        private String orderTime;
        private String deliveryCode;
        private String deliveryExpectedDays;
        private String deliveredTime;
        private String deliveryType;
        private String trackingId;
        private String deliveryAddress;
        private String rating;
        private String ratingText;
        private List<StatusArrayBean> statusArray;

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

        public String getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(String orderTime) {
            this.orderTime = orderTime;
        }

        public String getDeliveryCode() {
            return deliveryCode;
        }

        public void setDeliveryCode(String deliveryCode) {
            this.deliveryCode = deliveryCode;
        }

        public String getDeliveryExpectedDays() {
            return deliveryExpectedDays;
        }

        public void setDeliveryExpectedDays(String deliveryExpectedDays) {
            this.deliveryExpectedDays = deliveryExpectedDays;
        }

        public String getDeliveredTime() {
            return deliveredTime;
        }

        public void setDeliveredTime(String deliveredTime) {
            this.deliveredTime = deliveredTime;
        }

        public String getDeliveryType() {
            return deliveryType;
        }

        public void setDeliveryType(String deliveryType) {
            this.deliveryType = deliveryType;
        }

        public String getTrackingId() {
            return trackingId;
        }

        public void setTrackingId(String trackingId) {
            this.trackingId = trackingId;
        }

        public String getDeliveryAddress() {
            return deliveryAddress;
        }

        public void setDeliveryAddress(String deliveryAddress) {
            this.deliveryAddress = deliveryAddress;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getRatingText() {
            return ratingText;
        }

        public void setRatingText(String ratingText) {
            this.ratingText = ratingText;
        }

        public List<StatusArrayBean> getStatusArray() {
            return statusArray;
        }

        public void setStatusArray(List<StatusArrayBean> statusArray) {
            this.statusArray = statusArray;
        }

        public static class StatusArrayBean {
            /**
             * status : DELIVERED
             * statusText : Order Delivered
             * time : 2020-07-16 14:23:02
             * checked : yes
             */

            private String status;
            private String statusText;
            private String time;
            private String checked;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getStatusText() {
                return statusText;
            }

            public void setStatusText(String statusText) {
                this.statusText = statusText;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getChecked() {
                return checked;
            }

            public void setChecked(String checked) {
                this.checked = checked;
            }
        }
    }
}
