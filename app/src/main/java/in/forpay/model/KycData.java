package in.forpay.model;

import android.os.Parcel;
import android.os.Parcelable;

public class KycData implements Parcelable {

    private String name, value, placeHolder, minLength, maxLength, line, inputType;
    private String[] valueArray;

    protected KycData(Parcel in) {
        name = in.readString();
        value = in.readString();
        placeHolder = in.readString();
        minLength = in.readString();
        maxLength = in.readString();
        line = in.readString();
        inputType = in.readString();
        valueArray = in.createStringArray();
    }

    public static final Creator<KycData> CREATOR = new Creator<KycData>() {
        @Override
        public KycData createFromParcel(Parcel in) {
            return new KycData(in);
        }

        @Override
        public KycData[] newArray(int size) {
            return new KycData[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
    }

    public String getMinLength() {
        return minLength;
    }

    public void setMinLength(String minLength) {
        this.minLength = minLength;
    }

    public String getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String[] getValueArray() {
        return valueArray;
    }

    public void setValueArray(String[] valueArray) {
        this.valueArray = valueArray;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(value);
        parcel.writeString(placeHolder);
        parcel.writeString(minLength);
        parcel.writeString(maxLength);
        parcel.writeString(line);
        parcel.writeString(inputType);
        parcel.writeStringArray(valueArray);
    }
}
