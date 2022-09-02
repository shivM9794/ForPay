package in.forpay.model.busbookingModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookingHistoryModel {

    /**
     * bookingList : [{"id":"1002009262","date":"2021-03-30 12:04:05","source":"Hyderabad","destination":"Bangalore","name":"Yash Kalola","amount":"947","status":"PENDING"},{"id":"1001809261","date":"2021-03-30 11:59:46","source":"Hyderabad","destination":"Bangalore","name":"Samir Makadiya","amount":"958","status":"PENDING"},{"id":"1005709260","date":"2021-03-30 11:56:45","source":"Hyderabad","destination":"Bangalore","name":"Samir Makadiya","amount":"606","status":"PENDING"},{"id":"1004009259","date":"2021-03-30 11:45:47","source":"Hyderabad","destination":"Bangalore","name":"Yash Kalola","amount":"742","status":"PENDING"},{"id":"1004209258","date":"2021-03-30 11:42:45","source":"Hyderabad","destination":"Bangalore","name":"Yash Kalola","amount":"858","status":"PENDING"},{"id":"1008609256","date":"2021-03-28 11:42:22","source":"Hyderabad","destination":"Bangalore","name":"Rajdeep Kumar","amount":"897","status":"PENDING"},{"id":"1002109255","date":"2021-03-28 11:38:44","source":"Hyderabad","destination":"Bangalore","name":"Rajdeep Kumar","amount":"888","status":"PENDING"},{"id":"1007409254","date":"2021-03-28 10:48:58","source":"Hyderabad","destination":"Bangalore","name":"Rajdeep Kumar","amount":"596","status":"PENDING"},{"id":"1007509253","date":"2021-03-27 17:22:51","source":"Hyderabad","destination":"Bangalore","name":"Samir Makadiya","amount":"787","status":"BOOKED"},{"id":"1007409252","date":"2021-03-27 17:19:18","source":"Hyderabad","destination":"Bangalore","name":"Yash Kalola","amount":"770","status":"PENDING"}]
     * resCode : 200
     * resText : SUCCESS
     */

    @SerializedName("resCode")
    private String resCode;
    @SerializedName("resText")
    private String resText;
    /**
     * id : 1002009262
     * date : 2021-03-30 12:04:05
     * source : Hyderabad
     * destination : Bangalore
     * name : Yash Kalola
     * amount : 947
     * status : PENDING
     */

    @SerializedName("bookingList")
    private List<BookingListBean> bookingList;

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

    public List<BookingListBean> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<BookingListBean> bookingList) {
        this.bookingList = bookingList;
    }

    public static class BookingListBean {
        @SerializedName("id")
        private String id;
        @SerializedName("date")
        private String date;
        @SerializedName("source")
        private String source;
        @SerializedName("destination")
        private String destination;
        @SerializedName("name")
        private String name;
        @SerializedName("amount")
        private String amount;
        @SerializedName("status")
        private String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
