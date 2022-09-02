package in.forpay.model.busbookingModel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class BusBookDetail implements Parcelable {

    /**
     * data : {"bookingDetails":{"bpName":"Tis Hazari","bpAddress":"Amar Travels,Shop No-5, Ambedk","dpName":"Gopal Wadi","sourceName":"Delhi","destinationName":"Jaipur","busContactNumber":"","busType":"A/C Seater / Sleeper (2+1)","busName":"Amar Travels","partialCancellationAllowed":"false","cancellationPolicy":"0:12:100:0;12:-1:20:0;","customerMobile":"9006762209","bookingTime":"2020-02-15 12:53:58","passengerDetails":[{"name":"Nitesh_Rathod","age":"28","gender":"M","seatNumber":"2","seatAmount":"1260.00","status":"BOOKED"}]}}
     * resCode : 200
     * resText : SUCCESS
     */

    private DataBean data;
    private String resCode;
    private String resText;
    private String seatAvailableForCancel;

    public String getSeatAvailableForCancel() {
        return seatAvailableForCancel;
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

    public static class DataBean implements Parcelable {
        /**
         * bookingDetails : {"bpName":"Tis Hazari","bpAddress":"Amar Travels,Shop No-5, Ambedk","dpName":"Gopal Wadi","sourceName":"Delhi","destinationName":"Jaipur","busContactNumber":"","busType":"A/C Seater / Sleeper (2+1)","busName":"Amar Travels","partialCancellationAllowed":"false","cancellationPolicy":"0:12:100:0;12:-1:20:0;","customerMobile":"9006762209","bookingTime":"2020-02-15 12:53:58","passengerDetails":[{"name":"Nitesh_Rathod","age":"28","gender":"M","seatNumber":"2","seatAmount":"1260.00","status":"BOOKED"}]}
         */

        private BookingDetailsBean bookingDetails;

        public BookingDetailsBean getBookingDetails() {
            return bookingDetails;
        }

        public void setBookingDetails(BookingDetailsBean bookingDetails) {
            this.bookingDetails = bookingDetails;
        }

        public static class BookingDetailsBean implements Parcelable {
            /**
             * bpName : Tis Hazari
             * bpAddress : Amar Travels,Shop No-5, Ambedk
             * dpName : Gopal Wadi
             * sourceName : Delhi
             * destinationName : Jaipur
             * busContactNumber :
             * busType : A/C Seater / Sleeper (2+1)
             * busName : Amar Travels
             * partialCancellationAllowed : false
             * cancellationPolicy : 0:12:100:0;12:-1:20:0;
             * customerMobile : 9006762209
             * bookingTime : 2020-02-15 12:53:58
             * boardingTime : 2021-03-24 20:00:00
             * droppingTime : 2021-03-24 05:00:00
             * journyDate : 2021-03-24
             * travelTime": "322"
             * passengerDetails : [{"name":"Nitesh_Rathod","age":"28","gender":"M","seatNumber":"2","seatAmount":"1260.00","status":"BOOKED"}]
             * status: PENDING
             */

            private String bpName;
            private String bpAddress;
            private String dpName;
            private String sourceName;
            private String destinationName;
            private String busContactNumber;
            private String busType;
            private String busName;
            private String partialCancellationAllowed;
            private String cancellationPolicy;
            private String customerMobile;
            private String bookingTime;
            private String boardingTime;
            private String boardingDate;
            private String droppingTime;
            private String droppingDate;
            private String journyDate;
            private String travelTime;
            private String customerPaid;
            private String payableAmount;
            private String commission;
            private String travelDuration;
            private List<PassengerDetailsBean> passengerDetails;
            private String status;

            public String getBoardingDate() {
                return boardingDate;
            }

            public String getDroppingDate() {
                return droppingDate;
            }

            public String getTravelDuration() {
                return travelDuration;
            }

            public void setTravelDuration(String travelDuration) {
                this.travelDuration = travelDuration;
            }

            public void setBoardingDate(String boardingDate) {
                this.boardingDate = boardingDate;
            }

            public void setDroppingDate(String droppingDate) {
                this.droppingDate = droppingDate;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getTravelTime() {
                return travelTime;
            }

            public void setTravelTime(String travelTime) {
                this.travelTime = travelTime;
            }

            public String getBpName() {
                return bpName;
            }

            public void setBpName(String bpName) {
                this.bpName = bpName;
            }

            public String getBpAddress() {
                return bpAddress;
            }

            public void setBpAddress(String bpAddress) {
                this.bpAddress = bpAddress;
            }

            public String getDpName() {
                return dpName;
            }

            public void setDpName(String dpName) {
                this.dpName = dpName;
            }

            public String getSourceName() {
                return sourceName;
            }

            public void setSourceName(String sourceName) {
                this.sourceName = sourceName;
            }

            public String getDestinationName() {
                return destinationName;
            }

            public void setDestinationName(String destinationName) {
                this.destinationName = destinationName;
            }

            public String getBusContactNumber() {
                return busContactNumber;
            }

            public void setBusContactNumber(String busContactNumber) {
                this.busContactNumber = busContactNumber;
            }

            public String getBusType() {
                return busType;
            }

            public void setBusType(String busType) {
                this.busType = busType;
            }

            public String getBusName() {
                return busName;
            }

            public void setBusName(String busName) {
                this.busName = busName;
            }

            public String getPartialCancellationAllowed() {
                return partialCancellationAllowed;
            }

            public void setPartialCancellationAllowed(String partialCancellationAllowed) {
                this.partialCancellationAllowed = partialCancellationAllowed;
            }

            public String getCancellationPolicy() {
                return cancellationPolicy;
            }

            public void setCancellationPolicy(String cancellationPolicy) {
                this.cancellationPolicy = cancellationPolicy;
            }

            public String getCustomerMobile() {
                return customerMobile;
            }

            public void setCustomerMobile(String customerMobile) {
                this.customerMobile = customerMobile;
            }

            public String getBookingTime() {
                return bookingTime;
            }

            public void setBookingTime(String bookingTime) {
                this.bookingTime = bookingTime;
            }

            public String getBoardingTime() {
                return boardingTime;
            }

            public void setBoardingTime(String boardingTime) {
                this.boardingTime = boardingTime;
            }

            public String getDroppingTime() {
                return droppingTime;
            }

            public void setDroppingTime(String droppingTime) {
                this.droppingTime = droppingTime;
            }

            public String getJournyDate() {
                return journyDate;
            }

            public void setJournyDate(String journyDate) {
                this.journyDate = journyDate;
            }

            public String getCustomerPaid() {
                return customerPaid;
            }

            public void setCustomerPaid(String customerPaid) {
                this.customerPaid = customerPaid;
            }

            public String getPayableAmount() {
                return payableAmount;
            }

            public void setPayableAmount(String payableAmount) {
                this.payableAmount = payableAmount;
            }

            public String getCommission() {
                return commission;
            }

            public void setCommission(String commission) {
                this.commission = commission;
            }

            public List<PassengerDetailsBean> getPassengerDetails() {
                return passengerDetails;
            }

            public void setPassengerDetails(List<PassengerDetailsBean> passengerDetails) {
                this.passengerDetails = passengerDetails;
            }

            public static class PassengerDetailsBean implements Parcelable {
                /**
                 * name : Nitesh_Rathod
                 * age : 28
                 * gender : M
                 * seatNumber : 2
                 * seatAmount : 1260.00
                 * status : BOOKED
                 */

                private String name;
                private String age;
                private String gender;
                private String seatNumber;
                private String seatAmount;
                private String status;
                private boolean isSelected;

                public Boolean getSelected() {
                    return isSelected;
                }

                public void setSelected(Boolean selected) {
                    isSelected = selected;
                }


                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getAge() {
                    return age;
                }

                public void setAge(String age) {
                    this.age = age;
                }

                public String getGender() {
                    return gender;
                }

                public void setGender(String gender) {
                    this.gender = gender;
                }

                public String getSeatNumber() {
                    return seatNumber;
                }

                public void setSeatNumber(String seatNumber) {
                    this.seatNumber = seatNumber;
                }

                public String getSeatAmount() {
                    return seatAmount;
                }

                public void setSeatAmount(String seatAmount) {
                    this.seatAmount = seatAmount;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.name);
                    dest.writeString(this.age);
                    dest.writeString(this.gender);
                    dest.writeString(this.seatNumber);
                    dest.writeString(this.seatAmount);
                    dest.writeString(this.status);
                    dest.writeByte((byte) (this.isSelected ? 1 : 0));
                }

                public PassengerDetailsBean() {
                }

                protected PassengerDetailsBean(Parcel in) {
                    this.name = in.readString();
                    this.age = in.readString();
                    this.gender = in.readString();
                    this.seatNumber = in.readString();
                    this.seatAmount = in.readString();
                    this.status = in.readString();
                    this.isSelected = in.readByte() != 0;
                }

                public static final Creator<PassengerDetailsBean> CREATOR = new Creator<PassengerDetailsBean>() {
                    @Override
                    public PassengerDetailsBean createFromParcel(Parcel source) {
                        return new PassengerDetailsBean(source);
                    }

                    @Override
                    public PassengerDetailsBean[] newArray(int size) {
                        return new PassengerDetailsBean[size];
                    }
                };
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.bpName);
                dest.writeString(this.bpAddress);
                dest.writeString(this.dpName);
                dest.writeString(this.sourceName);
                dest.writeString(this.destinationName);
                dest.writeString(this.busContactNumber);
                dest.writeString(this.busType);
                dest.writeString(this.busName);
                dest.writeString(this.partialCancellationAllowed);
                dest.writeString(this.cancellationPolicy);
                dest.writeString(this.customerMobile);
                dest.writeString(this.bookingTime);
                dest.writeString(this.boardingTime);
                dest.writeString(this.droppingTime);
                dest.writeString(this.journyDate);
                dest.writeString(this.travelTime);
                dest.writeString(this.customerPaid);
                dest.writeString(this.commission);
                dest.writeString(this.payableAmount);
                dest.writeList(this.passengerDetails);
                dest.writeString(this.status);
            }

            public BookingDetailsBean() {
            }

            protected BookingDetailsBean(Parcel in) {
                this.bpName = in.readString();
                this.bpAddress = in.readString();
                this.dpName = in.readString();
                this.sourceName = in.readString();
                this.destinationName = in.readString();
                this.busContactNumber = in.readString();
                this.busType = in.readString();
                this.busName = in.readString();
                this.partialCancellationAllowed = in.readString();
                this.cancellationPolicy = in.readString();
                this.customerMobile = in.readString();
                this.bookingTime = in.readString();
                this.boardingTime = in.readString();
                this.droppingTime = in.readString();
                this.journyDate = in.readString();
                this.travelTime = in.readString();
                this.customerPaid = in.readString();
                this.commission = in.readString();
                this.payableAmount = in.readString();
                this.passengerDetails = new ArrayList<PassengerDetailsBean>();
                this.status = in.readString();
                in.readList(this.passengerDetails, PassengerDetailsBean.class.getClassLoader());
            }

            public static final Creator<BookingDetailsBean> CREATOR = new Creator<BookingDetailsBean>() {
                @Override
                public BookingDetailsBean createFromParcel(Parcel source) {
                    return new BookingDetailsBean(source);
                }

                @Override
                public BookingDetailsBean[] newArray(int size) {
                    return new BookingDetailsBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.bookingDetails, flags);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.bookingDetails = in.readParcelable(BookingDetailsBean.class.getClassLoader());
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.data, flags);
        dest.writeString(this.resCode);
        dest.writeString(this.resText);
    }

    public BusBookDetail() {
    }

    protected BusBookDetail(Parcel in) {
        this.data = in.readParcelable(DataBean.class.getClassLoader());
        this.resCode = in.readString();
        this.resText = in.readString();
    }

    public static final Creator<BusBookDetail> CREATOR = new Creator<BusBookDetail>() {
        @Override
        public BusBookDetail createFromParcel(Parcel source) {
            return new BusBookDetail(source);
        }

        @Override
        public BusBookDetail[] newArray(int size) {
            return new BusBookDetail[size];
        }
    };
}
