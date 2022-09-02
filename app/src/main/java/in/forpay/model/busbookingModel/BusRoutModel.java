package in.forpay.model.busbookingModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BusRoutModel {


    @SerializedName("data")
    private DataBean data;
    /**
     * data : {"routeList":[{"isSelected":"yes","stopName":"VRL Work Shop","date":"2021-03-27 05:00:00","type":"droppingPoint"},{"isSelected":"","stopName":"Yelahanka","date":"2021-03-27 05:10:00","type":"droppingPoint"},{"isSelected":"","stopName":"Hubli Office","date":"2021-03-27 06:05:00","type":"droppingPoint"},{"isSelected":"yes","stopName":"KPHB COLONY","date":"2021-03-27 20:00:00","type":"boardingPoint"},{"isSelected":"","stopName":"KUKATPALLY VILLAGE","date":"2021-03-27 20:10:00","type":"boardingPoint"},{"isSelected":"","stopName":"SR Nagar Minibus","date":"2021-03-27 20:53:00","type":"boardingPoint"}]}
     * resCode : 200
     * resText : Ticket Details Successfully Fetched
     */

    @SerializedName("resCode")
    private String resCode;
    @SerializedName("resText")
    private String resText;

    @SerializedName("busName")
    private String busName;

    @SerializedName("busType")
    private String busType;

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

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
         * isSelected : yes
         * stopName : VRL Work Shop
         * date : 2021-03-27 05:00:00
         * type : droppingPoint
         */

        @SerializedName("routeList")
        private List<RouteListBean> routeList;

        public List<RouteListBean> getRouteList() {
            return routeList;
        }

        public void setRouteList(List<RouteListBean> routeList) {
            this.routeList = routeList;
        }

        public static class RouteListBean {
            @SerializedName("isSelected")
            private String isSelected;
            @SerializedName("stopName")
            private String stopName;
            @SerializedName("date")
            private String date;
            @SerializedName("type")
            private String type;

            public String getIsSelected() {
                return isSelected;
            }

            public void setIsSelected(String isSelected) {
                this.isSelected = isSelected;
            }

            public String getStopName() {
                return stopName;
            }

            public void setStopName(String stopName) {
                this.stopName = stopName;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
