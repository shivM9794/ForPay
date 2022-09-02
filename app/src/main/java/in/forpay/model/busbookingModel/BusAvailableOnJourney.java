package in.forpay.model.busbookingModel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class BusAvailableOnJourney {


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

    public static class DataBean implements Parcelable {

        private List<BusStopsBean> busStops;

        public List<BusStopsBean> getBusStops() {
            return busStops;
        }

        public void setBusStops(List<BusStopsBean> busStops) {
            this.busStops = busStops;
        }

        public static class BusStopsBean implements Parcelable {


            private AmenitiesBean amenities;
            private String arrivalTime;
            private String avlSeats;
            private String avlWindowsSeats;
            private String busType;
            private String departureTime;
            private String travelDuration;
            private String doj;
            private String busName;
            private String busId;
            private String cancellationPolicy;
            private String partialCancellationAllowed;
            private String commission;
            private String busAlert;
            private List<String> fares;
            private List<BoardingPointsBean> boardingPoints;
            private List<DroppingPointsBean> droppingPoints;

            private long travelDurationInMill;

            public String getBusAlert() {
                return busAlert;
            }

            public AmenitiesBean getAmenities() {
                return amenities;
            }

            public void setAmenities(AmenitiesBean amenities) {
                this.amenities = amenities;
            }

            public String getArrivalTime() {
                return arrivalTime;
            }

            public void setArrivalTime(String arrivalTime) {
                this.arrivalTime = arrivalTime;
            }

            public String getAvlSeats() {
                return avlSeats;
            }

            public void setAvlSeats(String avlSeats) {
                this.avlSeats = avlSeats;
            }

            public String getAvlWindowsSeats() {
                return avlWindowsSeats;
            }

            public void setAvlWindowsSeats(String avlWindowsSeats) {
                this.avlWindowsSeats = avlWindowsSeats;
            }

            public String getBusType() {
                return busType;
            }

            public void setBusType(String busType) {
                this.busType = busType;
            }

            public String getDepartureTime() {
                return departureTime;
            }

            public void setDepartureTime(String departureTime) {
                this.departureTime = departureTime;
            }

            public String getTravelDuration() {
                return travelDuration;
            }

            public void setTravelDuration(String travelDuration) {
                this.travelDuration = travelDuration;
            }

            public String getDoj() {
                return doj;
            }

            public void setDoj(String doj) {
                this.doj = doj;
            }

            public String getBusName() {
                return busName;
            }

            public void setBusName(String busName) {
                this.busName = busName;
            }

            public String getBusId() {
                return busId;
            }

            public void setBusId(String busId) {
                this.busId = busId;
            }

            public String getCancellationPolicy() {
                return cancellationPolicy;
            }

            public void setCancellationPolicy(String cancellationPolicy) {
                this.cancellationPolicy = cancellationPolicy;
            }

            public String getPartialCancellationAllowed() {
                return partialCancellationAllowed;
            }

            public void setPartialCancellationAllowed(String partialCancellationAllowed) {
                this.partialCancellationAllowed = partialCancellationAllowed;
            }

            public String getCommission() {
                return commission;
            }

            public void setCommission(String commission) {
                this.commission = commission;
            }

            public List<String> getFares() {
                return fares;
            }

            public void setFares(List<String> fares) {
                this.fares = fares;
            }

            public List<BoardingPointsBean> getBoardingPoints() {
                return boardingPoints;
            }

            public void setBoardingPoints(List<BoardingPointsBean> boardingPoints) {
                this.boardingPoints = boardingPoints;
            }

            public List<DroppingPointsBean> getDroppingPoints() {
                return droppingPoints;
            }

            public void setDroppingPoints(List<DroppingPointsBean> droppingPoints) {
                this.droppingPoints = droppingPoints;
            }

            public static class AmenitiesBean implements Parcelable {
                /**
                 * ac : yes
                 * sleeper : yes
                 * pushback :
                 * liveTrackingAvailable :
                 * chargingPoint :
                 */

                private String ac;
                private String sleeper;
                private String pushback;
                private String liveTrackingAvailable;
                private String chargingPoint;

                public String getAc() {
                    return ac;
                }

                public void setAc(String ac) {
                    this.ac = ac;
                }

                public String getSleeper() {
                    return sleeper;
                }

                public void setSleeper(String sleeper) {
                    this.sleeper = sleeper;
                }

                public String getPushback() {
                    return pushback;
                }

                public void setPushback(String pushback) {
                    this.pushback = pushback;
                }

                public String getLiveTrackingAvailable() {
                    return liveTrackingAvailable;
                }

                public void setLiveTrackingAvailable(String liveTrackingAvailable) {
                    this.liveTrackingAvailable = liveTrackingAvailable;
                }

                public String getChargingPoint() {
                    return chargingPoint;
                }

                public void setChargingPoint(String chargingPoint) {
                    this.chargingPoint = chargingPoint;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.ac);
                    dest.writeString(this.sleeper);
                    dest.writeString(this.pushback);
                    dest.writeString(this.liveTrackingAvailable);
                    dest.writeString(this.chargingPoint);
                }

                public AmenitiesBean() {
                }

                protected AmenitiesBean(Parcel in) {
                    this.ac = in.readString();
                    this.sleeper = in.readString();
                    this.pushback = in.readString();
                    this.liveTrackingAvailable = in.readString();
                    this.chargingPoint = in.readString();
                }

                public static final Creator<AmenitiesBean> CREATOR = new Creator<AmenitiesBean>() {
                    @Override
                    public AmenitiesBean createFromParcel(Parcel source) {
                        return new AmenitiesBean(source);
                    }

                    @Override
                    public AmenitiesBean[] newArray(int size) {
                        return new AmenitiesBean[size];
                    }
                };
            }

            public static class BoardingPointsBean implements Parcelable {
                /**
                 * bpId : 17946256
                 * bpName : Tis Hazari
                 * bpTime : 2020-02-03 19:15:00
                 * bpAddress : VIKAS TRAVELS REGD_TIS HAZARI (KHANNA MARKET)  01123974789,01123984789
                 * contactNumber :
                 */

                private String bpId;
                private String bpName;
                private String bpTime;
                private String bpAddress;
                private String contactNumber;


                public boolean isItemSelected() {
                    return isItemSelected;
                }

                public void setItemSelected(boolean itemSelected) {
                    isItemSelected = itemSelected;
                }

                private boolean isItemSelected;

                public static Creator<BoardingPointsBean> getCREATOR() {
                    return CREATOR;
                }


                public String getBpId() {
                    return bpId;
                }

                public void setBpId(String bpId) {
                    this.bpId = bpId;
                }

                public String getBpName() {
                    return bpName;
                }

                public void setBpName(String bpName) {
                    this.bpName = bpName;
                }

                public String getBpTime() {
                    return bpTime;
                }

                public void setBpTime(String bpTime) {
                    this.bpTime = bpTime;
                }

                public String getBpAddress() {
                    return bpAddress;
                }

                public void setBpAddress(String bpAddress) {
                    this.bpAddress = bpAddress;
                }

                public String getContactNumber() {
                    return contactNumber;
                }

                public void setContactNumber(String contactNumber) {
                    this.contactNumber = contactNumber;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.bpId);
                    dest.writeString(this.bpName);
                    dest.writeString(this.bpTime);
                    dest.writeString(this.bpAddress);
                    dest.writeString(this.contactNumber);
                    dest.writeByte((byte) (this.isItemSelected ? 1 : 0));     //if myBoolean == true, byte == 1

                }

                public BoardingPointsBean() {
                }

                protected BoardingPointsBean(Parcel in) {
                    this.bpId = in.readString();
                    this.bpName = in.readString();
                    this.bpTime = in.readString();
                    this.bpAddress = in.readString();
                    this.contactNumber = in.readString();
                    this.isItemSelected = in.readByte() != 0;
                }

                public static final Creator<BoardingPointsBean> CREATOR = new Creator<BoardingPointsBean>() {
                    @Override
                    public BoardingPointsBean createFromParcel(Parcel source) {
                        return new BoardingPointsBean(source);
                    }

                    @Override
                    public BoardingPointsBean[] newArray(int size) {
                        return new BoardingPointsBean[size];
                    }
                };
            }

            public static class DroppingPointsBean implements Parcelable {
                /**
                 * dpId : 448492
                 * dpName : Gopal Wadi
                 * dpTime : 2020-02-04 01:55:00
                 * dpAddress : Sindhi Camp- Shiv Shankar Travels (9413802773 / 7412011009)
                 * contactNumber :
                 */

                private String dpId;
                private String dpName;
                private String dpTime;
                private String dpAddress;
                private String contactNumber;

                public boolean isItemSelected() {
                    return isItemSelected;
                }

                public void setItemSelected(boolean itemSelected) {
                    isItemSelected = itemSelected;
                }

                private boolean isItemSelected;

                public String getDpId() {
                    return dpId;
                }

                public void setDpId(String dpId) {
                    this.dpId = dpId;
                }

                public String getDpName() {
                    return dpName;
                }

                public void setDpName(String dpName) {
                    this.dpName = dpName;
                }

                public String getDpTime() {
                    return dpTime;
                }

                public void setDpTime(String dpTime) {
                    this.dpTime = dpTime;
                }

                public String getDpAddress() {
                    return dpAddress;
                }

                public void setDpAddress(String dpAddress) {
                    this.dpAddress = dpAddress;
                }

                public String getContactNumber() {
                    return contactNumber;
                }

                public void setContactNumber(String contactNumber) {
                    this.contactNumber = contactNumber;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.dpId);
                    dest.writeString(this.dpName);
                    dest.writeString(this.dpTime);
                    dest.writeString(this.dpAddress);
                    dest.writeString(this.contactNumber);
                    dest.writeByte((byte) (this.isItemSelected ? 1 : 0));
                }

                public DroppingPointsBean() {
                }

                protected DroppingPointsBean(Parcel in) {
                    this.dpId = in.readString();
                    this.dpName = in.readString();
                    this.dpTime = in.readString();
                    this.dpAddress = in.readString();
                    this.contactNumber = in.readString();
                    this.isItemSelected = in.readByte() != 0;
                }

                public static final Creator<DroppingPointsBean> CREATOR = new Creator<DroppingPointsBean>() {
                    @Override
                    public DroppingPointsBean createFromParcel(Parcel source) {
                        return new DroppingPointsBean(source);
                    }

                    @Override
                    public DroppingPointsBean[] newArray(int size) {
                        return new DroppingPointsBean[size];
                    }
                };
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeParcelable(this.amenities, flags);
                dest.writeString(this.arrivalTime);
                dest.writeString(this.avlSeats);
                dest.writeString(this.avlWindowsSeats);
                dest.writeString(this.busType);
                dest.writeString(this.departureTime);
                dest.writeString(this.travelDuration);
                dest.writeString(this.doj);
                dest.writeString(this.busName);
                dest.writeString(this.busId);
                dest.writeString(this.cancellationPolicy);
                dest.writeString(this.partialCancellationAllowed);
                dest.writeStringList(this.fares);
                dest.writeList(this.boardingPoints);
                dest.writeList(this.droppingPoints);
            }

            public BusStopsBean() {
            }

            protected BusStopsBean(Parcel in) {
                this.amenities = in.readParcelable(AmenitiesBean.class.getClassLoader());
                this.arrivalTime = in.readString();
                this.avlSeats = in.readString();
                this.avlWindowsSeats = in.readString();
                this.busType = in.readString();
                this.departureTime = in.readString();
                this.travelDuration = in.readString();
                this.doj = in.readString();
                this.busName = in.readString();
                this.busId = in.readString();
                this.cancellationPolicy = in.readString();
                this.partialCancellationAllowed = in.readString();
                this.fares = in.createStringArrayList();
                this.boardingPoints = new ArrayList<BoardingPointsBean>();
                in.readList(this.boardingPoints, BoardingPointsBean.class.getClassLoader());
                this.droppingPoints = new ArrayList<DroppingPointsBean>();
                in.readList(this.droppingPoints, DroppingPointsBean.class.getClassLoader());
            }

            public static final Creator<BusStopsBean> CREATOR = new Creator<BusStopsBean>() {
                @Override
                public BusStopsBean createFromParcel(Parcel source) {
                    return new BusStopsBean(source);
                }

                @Override
                public BusStopsBean[] newArray(int size) {
                    return new BusStopsBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeList(this.busStops);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.busStops = new ArrayList<BusStopsBean>();
            in.readList(this.busStops, BusStopsBean.class.getClassLoader());
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }
}
