package in.forpay.model.busbookingModel;

import android.os.Parcel;
import android.os.Parcelable;

public class PassengerInfo implements Parcelable {


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String firstName;
    private String lastName;
    private String seatNo;
    private String email;
    private String pinCode;
    private String address;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLandMark() {
        return landMark;
    }

    public void setLandMark(String landMark) {
        this.landMark = landMark;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static Creator<PassengerInfo> getCREATOR() {
        return CREATOR;
    }

    private String gender;
    private String mobileNumber;
    private String age;
    private String landMark;
    private String city;
    private String pincode;
    private String title;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.seatNo);
        dest.writeString(this.email);
        dest.writeString(this.pinCode);
        dest.writeString(this.address);
        dest.writeString(this.gender);
        dest.writeString(this.mobileNumber);
        dest.writeString(this.age);
        dest.writeString(this.landMark);
        dest.writeString(this.city);
        dest.writeString(this.title);

    }

    public PassengerInfo() {
    }

    protected PassengerInfo(Parcel in) {
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.seatNo = in.readString();
        this.email = in.readString();
        this.pinCode = in.readString();
        this.address = in.readString();


        this.gender = in.readString();
        this.mobileNumber = in.readString();
        this.age = in.readString();
        this.landMark = in.readString();
        this.city = in.readString();
        this.title = in.readString();


    }

    public static final Creator<PassengerInfo> CREATOR = new Creator<PassengerInfo>() {
        @Override
        public PassengerInfo createFromParcel(Parcel source) {
            return new PassengerInfo(source);
        }

        @Override
        public PassengerInfo[] newArray(int size) {
            return new PassengerInfo[size];
        }
    };
}
