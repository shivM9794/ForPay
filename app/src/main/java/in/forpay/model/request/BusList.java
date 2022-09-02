package in.forpay.model.request;

public class BusList {
    private String ac,pushBack,liveTracingAvailable,chargingPoint,arrivalTime,avlSeats,avlWindowsSeats,busType,busName,bookingId,cancelPolicy,partialCancelAllowed,departureTime,travelDuration,fare="";

    public BusList(){

    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getTravelDuration() {
        return travelDuration;
    }

    public void setTravelDuration(String travelDuration) {
        this.travelDuration = travelDuration;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getBusType() {
        return busType;
    }

    public String getBusName() {
        return busName;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getAvlWindowsSeats() {
        return avlWindowsSeats;
    }

    public String getAvlSeats() {
        return avlSeats;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getChargingPoint() {
        return chargingPoint;
    }

    public String getAc() {
        return ac;
    }

    public String getCancelPolicy() {
        return cancelPolicy;
    }

    public String getLiveTracingAvailable() {
        return liveTracingAvailable;
    }

    public String getPartialCancelAllowed() {
        return partialCancelAllowed;
    }

    public String getPushBack() {
        return pushBack;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public void setAvlWindowsSeats(String avlWindowsSeats) {
        this.avlWindowsSeats = avlWindowsSeats;
    }

    public void setAvlSeats(String avlSeats) {
        this.avlSeats = avlSeats;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setChargingPoint(String chargingPoint) {
        this.chargingPoint = chargingPoint;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public void setCancelPolicy(String cancelPolicy) {
        this.cancelPolicy = cancelPolicy;
    }

    public void setLiveTracingAvailable(String liveTracingAvailable) {
        this.liveTracingAvailable = liveTracingAvailable;
    }

    public void setPartialCancelAllowed(String partialCancelAllowed) {
        this.partialCancelAllowed = partialCancelAllowed;
    }

    public void setPushBack(String pushBack) {
        this.pushBack = pushBack;
    }
}
