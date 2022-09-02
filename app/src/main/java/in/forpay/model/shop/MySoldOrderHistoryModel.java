package in.forpay.model.shop;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class MySoldOrderHistoryModel {


    /**
     * data : [{"shopName":"Abc","imageId":"0","imgUrl":"","contactNumber":"9999999999","deliveryRange":"1","latitude":"22.050808","longitude":"22.050808","orderTime":"2020-06-26 17:30:41","deliveryCode":"0","deliveryExpectedDays":"2","deliveredTime":"","deliveryType":"","trackingId":"","deliveryAddress":"","orderHistory":{"orderId":"1","toUserId":"3","orderTime":"2020-06-26 17:30:41","deliveryCharge":"200","status":"PENDING","statusText":"Waiting for user to accept it","totalCost":"79150","address":"","rating":"","items":[{"itemId":"1","itemName":"Mobile","quantity":"2","amount":"78900.00"},{"itemId":"2","itemName":"Cable","quantity":"5","amount":"250.00"}]}}]
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

    public static class DataBean implements Parcelable{
        /**
         * shopName : Abc
         * imageId : 0
         * imgUrl :
         * contactNumber : 9999999999
         * deliveryRange : 1
         * latitude : 22.050808
         * longitude : 22.050808
         * orderTime : 2020-06-26 17:30:41
         * deliveryCode : 0
         * deliveryExpectedDays : 2
         * deliveredTime :
         * deliveryType :
         * trackingId :
         * deliveryAddress :
         * orderHistory : {"orderId":"1","toUserId":"3","orderTime":"2020-06-26 17:30:41","deliveryCharge":"200","status":"PENDING","statusText":"Waiting for user to accept it","totalCost":"79150","address":"","rating":"","items":[{"itemId":"1","itemName":"Mobile","quantity":"2","amount":"78900.00"},{"itemId":"2","itemName":"Cable","quantity":"5","amount":"250.00"}]}
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
        private OrderHistoryBean orderHistory;

        protected DataBean(Parcel in) {
            shopName = in.readString();
            imageId = in.readString();
            imgUrl = in.readString();
            contactNumber = in.readString();
            deliveryRange = in.readString();
            latitude = in.readString();
            longitude = in.readString();
            orderTime = in.readString();
            deliveryCode = in.readString();
            deliveryExpectedDays = in.readString();
            deliveredTime = in.readString();
            deliveryType = in.readString();
            trackingId = in.readString();
            deliveryAddress = in.readString();
            orderHistory = in.readParcelable(OrderHistoryBean.class.getClassLoader());
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel in) {
                return new DataBean(in);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };

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

        public OrderHistoryBean getOrderHistory() {
            return orderHistory;
        }

        public void setOrderHistory(OrderHistoryBean orderHistory) {
            this.orderHistory = orderHistory;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(shopName);
            parcel.writeString(imageId);
            parcel.writeString(imgUrl);
            parcel.writeString(contactNumber);
            parcel.writeString(deliveryRange);
            parcel.writeString(latitude);
            parcel.writeString(longitude);
            parcel.writeString(orderTime);
            parcel.writeString(deliveryCode);
            parcel.writeString(deliveryExpectedDays);
            parcel.writeString(deliveredTime);
            parcel.writeString(deliveryType);
            parcel.writeString(trackingId);
            parcel.writeString(deliveryAddress);
            parcel.writeParcelable(orderHistory, i);
        }

        public static class OrderHistoryBean implements Parcelable {
            /**
             * orderId : 1
             * toUserId : 3
             * orderTime : 2020-06-26 17:30:41
             * deliveryCharge : 200
             * status : PENDING
             * statusText : Waiting for user to accept it
             * totalCost : 79150
             * address :
             * rating :
             * items : [{"itemId":"1","itemName":"Mobile","quantity":"2","amount":"78900.00"},{"itemId":"2","itemName":"Cable","quantity":"5","amount":"250.00"}]
             */

            private String orderId;
            private String toUserId;
            private String orderTime;
            private String deliveryCharge;
            private String status;
            private String statusText;
            private String totalCost;
            private String address;
            private String rating;
            private List<ItemsBean> items;

            protected OrderHistoryBean(Parcel in) {
                orderId = in.readString();
                toUserId = in.readString();
                orderTime = in.readString();
                deliveryCharge = in.readString();
                status = in.readString();
                statusText = in.readString();
                totalCost = in.readString();
                address = in.readString();
                rating = in.readString();
            }

            public static final Creator<OrderHistoryBean> CREATOR = new Creator<OrderHistoryBean>() {
                @Override
                public OrderHistoryBean createFromParcel(Parcel in) {
                    return new OrderHistoryBean(in);
                }

                @Override
                public OrderHistoryBean[] newArray(int size) {
                    return new OrderHistoryBean[size];
                }
            };

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getToUserId() {
                return toUserId;
            }

            public void setToUserId(String toUserId) {
                this.toUserId = toUserId;
            }

            public String getOrderTime() {
                return orderTime;
            }

            public void setOrderTime(String orderTime) {
                this.orderTime = orderTime;
            }

            public String getDeliveryCharge() {
                return deliveryCharge;
            }

            public void setDeliveryCharge(String deliveryCharge) {
                this.deliveryCharge = deliveryCharge;
            }

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

            public String getTotalCost() {
                return totalCost;
            }

            public void setTotalCost(String totalCost) {
                this.totalCost = totalCost;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getRating() {
                return rating;
            }

            public void setRating(String rating) {
                this.rating = rating;
            }

            public List<ItemsBean> getItems() {
                return items;
            }

            public void setItems(List<ItemsBean> items) {
                this.items = items;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
                parcel.writeString(orderId);
                parcel.writeString(toUserId);
                parcel.writeString(orderTime);
                parcel.writeString(deliveryCharge);
                parcel.writeString(status);
                parcel.writeString(statusText);
                parcel.writeString(totalCost);
                parcel.writeString(address);
                parcel.writeString(rating);
            }

            public static class ItemsBean implements Parcelable{
                /**
                 * itemId : 1
                 * itemName : Mobile
                 * quantity : 2
                 * amount : 78900.00
                 */

                private String itemId;
                private String itemName;
                private String quantity;
                private String amount;

                protected ItemsBean(Parcel in) {
                    itemId = in.readString();
                    itemName = in.readString();
                    quantity = in.readString();
                    amount = in.readString();
                }

                public static final Creator<ItemsBean> CREATOR = new Creator<ItemsBean>() {
                    @Override
                    public ItemsBean createFromParcel(Parcel in) {
                        return new ItemsBean(in);
                    }

                    @Override
                    public ItemsBean[] newArray(int size) {
                        return new ItemsBean[size];
                    }
                };

                public String getItemId() {
                    return itemId;
                }

                public void setItemId(String itemId) {
                    this.itemId = itemId;
                }

                public String getItemName() {
                    return itemName;
                }

                public void setItemName(String itemName) {
                    this.itemName = itemName;
                }

                public String getQuantity() {
                    return quantity;
                }

                public void setQuantity(String quantity) {
                    this.quantity = quantity;
                }

                public String getAmount() {
                    return amount;
                }

                public void setAmount(String amount) {
                    this.amount = amount;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel parcel, int i) {
                    parcel.writeString(itemId);
                    parcel.writeString(itemName);
                    parcel.writeString(quantity);
                    parcel.writeString(amount);
                }
            }
        }
    }
}
