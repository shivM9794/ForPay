package in.forpay.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetBusListResponse {

    private Data data;
    private String resCode = "";
    private String resText = "";

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        @SerializedName("busStops")
        private ArrayList<BusStops> busStopsList = new ArrayList<>();

        public ArrayList<BusStops> getBusStopsList(){return busStopsList;}

    }


    public class BoardingPoints{
        String bpId,bpName,bpTime,bpAddress,contactNumber="";

        public void setBpAddress(String bpAddress) {
            this.bpAddress = bpAddress;
        }

        public void setBpId(String bpId) {
            this.bpId = bpId;
        }

        public void setBpName(String bpName) {
            this.bpName = bpName;
        }

        public void setBpTime(String bpTime) {
            this.bpTime = bpTime;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public String getBpAddress() {
            return bpAddress;
        }

        public String getBpId() {
            return bpId;
        }

        public String getBpName() {
            return bpName;
        }

        public String getBpTime() {
            return bpTime;
        }

        public String getContactNumber() {
            return contactNumber;
        }
    }

    public class DroppingPoints{
        String dpId,dpName,dpTime,dpAddress,contactNumber="";

        public String getContactNumber() {
            return contactNumber;
        }

        public String getDpAddress() {
            return dpAddress;
        }

        public String getDpId() {
            return dpId;
        }

        public String getDpName() {
            return dpName;
        }

        public String getDpTime() {
            return dpTime;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public void setDpAddress(String dpAddress) {
            this.dpAddress = dpAddress;
        }

        public void setDpId(String dpId) {
            this.dpId = dpId;
        }

        public void setDpName(String dpName) {
            this.dpName = dpName;
        }

        public void setDpTime(String dpTime) {
            this.dpTime = dpTime;
        }
    }

    public class Fares{

    }

    public class BusStops{

        @SerializedName("boardingPoints")
        private ArrayList<BoardingPoints> boardingPoints = new ArrayList<>();

        public ArrayList<BoardingPoints> getBoardingPoints() {
            return boardingPoints;
        }

        @SerializedName("droppingPoints")
        private ArrayList<DroppingPoints> droppingPoints = new ArrayList<>();

        public ArrayList<DroppingPoints> getDroppingPoints() {
            return droppingPoints;
        }

        @SerializedName("fares")
        private String[] fares = new String[]{};
        public String[] getFares() { return fares; }

        private Amenities amenities;
        private String arrivalTime,avlSeats,avlWindowsSeats,busType,departureTime,travelDuration,doj,busName,bookingId,cancellationPolicy,partialCancellationAllowed="";

        public String getAvlSeats() {
            return avlSeats;
        }

        public String getAvlWindowsSeats() {
            return avlWindowsSeats;
        }

        public void setAvlSeats(String avlSeats) {
            this.avlSeats = avlSeats;
        }

        public void setAvlWindowsSeats(String avlWindowsSeats) {
            this.avlWindowsSeats = avlWindowsSeats;
        }

        public void setBookingId(String bookingId) {
            this.bookingId = bookingId;
        }

        public void setBusName(String busName) {
            this.busName = busName;
        }

        public void setBusType(String busType) {
            this.busType = busType;
        }

        public void setCancellationPolicy(String cancellationPolicy) {
            this.cancellationPolicy = cancellationPolicy;
        }

        public void setDepartureTime(String departureTime) {
            this.departureTime = departureTime;
        }

        public void setDoj(String doj) {
            this.doj = doj;
        }

        public void setPartialCancellationAllowed(String partialCancellationAllowed) {
            this.partialCancellationAllowed = partialCancellationAllowed;
        }

        public void setTravelDuration(String travelDuration) {
            this.travelDuration = travelDuration;
        }

        public String getBookingId() {
            return bookingId;
        }

        public String getBusName() {
            return busName;
        }

        public String getBusType() {
            return busType;
        }

        public String getCancellationPolicy() {
            return cancellationPolicy;
        }

        public String getDepartureTime() {
            return departureTime;
        }

        public String getDoj() {
            return doj;
        }

        public String getPartialCancellationAllowed() {
            return partialCancellationAllowed;
        }

        public String getTravelDuration() {
            return travelDuration;
        }

        public String getArrivalTime() {
            return arrivalTime;
        }

        public void setArrivalTime(String arrivalTime) {
            this.arrivalTime = arrivalTime;
        }

        public void setAmenities(Amenities amenities) {
            this.amenities = amenities;
        }

        public Amenities getAmenities() {
            return amenities;
        }


    }

    public class Amenities{

        private String ac,sleeper,puchback,liverTrackingAvailable,chargingPoint="";

        public void setAc(String ac) {
            this.ac = ac;
        }

        public String getAc() {
            return ac;
        }

        public String getChargingPoint() {
            return chargingPoint;
        }

        public void setChargingPoint(String chargingPoint) {
            this.chargingPoint = chargingPoint;
        }

        public String getSleeper() {
            return sleeper;
        }

        public void setSleeper(String sleeper) {
            this.sleeper = sleeper;
        }

        public String getLiverTrackingAvailable() {
            return liverTrackingAvailable;
        }

        public void setLiverTrackingAvailable(String liverTrackingAvailable) {
            this.liverTrackingAvailable = liverTrackingAvailable;
        }

        public void setPuchback(String puchback) {
            this.puchback = puchback;
        }

        public String getPuchback() {
            return puchback;
        }
    }

    public String getResCode() {
        return resCode;
    }

    public String getResText() {
        return resText;
    }
}
