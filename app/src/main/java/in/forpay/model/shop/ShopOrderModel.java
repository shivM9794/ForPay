package in.forpay.model.shop;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ShopOrderModel {


    /**
     * data : {"shopName":"Nikunjbhai","imageId":"0","imgUrl":"","contactNumber":"9090909090","deliveryRange":"10","latitude":"22.008224","longitude":"22.008224","products":[{"productId":"3","productName":"Keyboards"}],"distance":"","avgDeliveryTime":"2 Hour","avgRating":"4.2","discription":"This is test shop where you can buy anything online within your market","lastActive":"1 Hr Ago","orderHistory":[{"orderId":"2","orderTime":"2020-07-03 09:30:14","deliveryCharge":"200","status":"PENDING","totalCost":"20200","address":"","rating":"","items":[{"itemId":"3","itemName":"TVV","quantity":"1","amount":"20000.00"}]}]}
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
         * shopName : Nikunjbhai
         * imageId : 0
         * imgUrl :
         * contactNumber : 9090909090
         * deliveryRange : 10
         * latitude : 22.008224
         * longitude : 22.008224
         * products : [{"productId":"3","productName":"Keyboards"}]
         * distance :
         * avgDeliveryTime : 2 Hour
         * avgRating : 4.2
         * discription : This is test shop where you can buy anything online within your market
         * lastActive : 1 Hr Ago
         * orderHistory : [{"orderId":"2","orderTime":"2020-07-03 09:30:14","deliveryCharge":"200","status":"PENDING","totalCost":"20200","address":"","rating":"","items":[{"itemId":"3","itemName":"TVV","quantity":"1","amount":"20000.00"}]}]
         */

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
        private List<OrderHistoryBean> orderHistory;

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

        public List<OrderHistoryBean> getOrderHistory() {
            return orderHistory;
        }

        public void setOrderHistory(List<OrderHistoryBean> orderHistory) {
            this.orderHistory = orderHistory;
        }

        public static class ProductsBean {
            /**
             * productId : 3
             * productName : Keyboards
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

        public static class OrderHistoryBean implements Parcelable{
            /**
             * orderId : 2
             * orderTime : 2020-07-03 09:30:14
             * deliveryCharge : 200
             * status : PENDING
             * totalCost : 20200
             * address :
             * rating :
             * items : [{"itemId":"3","itemName":"TVV","quantity":"1","amount":"20000.00"}]
             */

            private String orderId;
            private String orderTime;
            private String deliveryCharge;
            private String status;
            private String totalCost;
            private String address;
            private String rating;
            private List<ItemsBean> items;

            protected OrderHistoryBean(Parcel in) {
                orderId = in.readString();
                orderTime = in.readString();
                deliveryCharge = in.readString();
                status = in.readString();
                totalCost = in.readString();
                address = in.readString();
                rating = in.readString();
                items = in.createTypedArrayList(ItemsBean.CREATOR);
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
                parcel.writeString(orderTime);
                parcel.writeString(deliveryCharge);
                parcel.writeString(status);
                parcel.writeString(totalCost);
                parcel.writeString(address);
                parcel.writeString(rating);
                parcel.writeTypedList(items);
            }

            public static class ItemsBean implements Parcelable {
                /**
                 * itemId : 3
                 * itemName : TVV
                 * quantity : 1
                 * amount : 20000.00
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
